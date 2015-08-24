package andrzej.example.com.wordunscrambler.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.config.ViewsConfig;
import andrzej.example.com.wordunscrambler.interfaces.ItemActionsListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;

/**
 * Created by andrzej on 24.08.15.
 */
public class SwipeDictionaryListAdapter extends BaseSwipeAdapter {

    public static final int FIRST_WORDS_TO_LOAD = ViewsConfig.FIRST_WORDS_TO_LOAD;

    private Context context;
    private List<Dictionary> mDataset;
    private LayoutInflater inflater;

    ItemActionsListener itemActionsListener;


    public SwipeDictionaryListAdapter(Context context, List<Dictionary> mDataset) {
        this.mDataset = mDataset;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.rootLayout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        return inflater.inflate(R.layout.dictionary_list_item, null);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        SwipeLayout rootLayout = (SwipeLayout) convertView.findViewById(R.id.rootLayout);
        RelativeLayout topWrapper = (RelativeLayout) convertView.findViewById(R.id.top_wrapper);
        BootstrapButton  deleteBtn = (BootstrapButton) convertView.findViewById(R.id.deleteBtn);
        BootstrapButton setBtn = (BootstrapButton) convertView.findViewById(R.id.setBtn);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.titleTv);
        TextView tvWordsCount = (TextView) convertView.findViewById(R.id.wordsCountTv);
        TextView tvFirstWords = (TextView) convertView.findViewById(R.id.firstWordsTv);

        //Swipe Layout config
        rootLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        rootLayout.addDrag(SwipeLayout.DragEdge.Right, convertView.findViewById(R.id.bottom_wrapper));


        TabsConfig.CURRENT_DICTIONARY_POSITION = position;

        final Dictionary dictionary = getItem(position);

        if (DictionaryUtils.isCurrentDictionary(context, dictionary)) {
            setViewBackground(topWrapper, ContextCompat.getDrawable(context, R.drawable.item_selector_highlited));
            setBtn.setBootstrapButtonEnabled(false);
            setBtn.setBootstrapType("default");
        } else {
            setViewBackground(topWrapper, ContextCompat.getDrawable(context, R.drawable.item_selector_white));
            setBtn.setBootstrapButtonEnabled(true);
            setBtn.setBootstrapType("success");
        }

        int wordCount = dictionary.getWordCount();
        tvTitle.setText(dictionary.getName());
        tvWordsCount.setText(String.valueOf(wordCount));

        if (wordCount == 0)
            tvFirstWords.setText("...");
        else
            tvFirstWords.setText(dictionary.getFirstNWordsInString(FIRST_WORDS_TO_LOAD));

        topWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.onItemClick(v, position);
            }
        });

        topWrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.onLongItemClick(v, position);
                return false;
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.deleteItemAction(position);
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.setItemAction(position);
            }
        });
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public Dictionary getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Context getContext() {
        return context;
    }

    private void setViewBackground(View iv, Drawable drawable) {
        int currentVersion = Build.VERSION.SDK_INT;

        if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            iv.setBackground(drawable);
        } else {
            iv.setBackgroundDrawable(drawable);
        }
    }

    public void registerItemActionsListener(ItemActionsListener itemActionsListener) {
        this.itemActionsListener = itemActionsListener;
    }
}
