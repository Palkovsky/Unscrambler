package andrzej.example.com.wordunscrambler.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.interfaces.ItemActionsListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;

/**
 * Created by andrzej on 21.08.15.
 */
public class DictionaryListAdapter extends BaseAdapter {

    public static final int FIRST_WORDS_TO_LOAD = 4;

    private Context context;
    private List<Dictionary> mDataset;
    private LayoutInflater inflater;

    ItemActionsListener itemActionsListener;

    public DictionaryListAdapter(Context context, List<Dictionary> mDataset) {
        this.context = context;
        this.mDataset = mDataset;
        inflater = LayoutInflater.from(this.context);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DictionaryItemViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dictionary_list_item, parent, false);
            mViewHolder = new DictionaryItemViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (DictionaryItemViewHolder) convertView.getTag();
        }

        TabsConfig.CURRENT_DICTIONARY_POSITION = position;

        final Dictionary dictionary = getItem(position);

        if(DictionaryUtils.isCurrentDictionary(context, dictionary)){
            setViewBackground(mViewHolder.topWrapper, ContextCompat.getDrawable(context, R.drawable.item_selector_highlited));
        }else{
            setViewBackground(mViewHolder.topWrapper, ContextCompat.getDrawable(context, R.drawable.item_selector_white));
        }

        mViewHolder.tvTitle.setText(dictionary.getName());
        mViewHolder.tvWordsCount.setText(String.valueOf(dictionary.getWordCount()));
        mViewHolder.tvFirstWords.setText(dictionary.getFirstNWordsInString(FIRST_WORDS_TO_LOAD));

        mViewHolder.topWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.onItemClick(v, position);
            }
        });

        mViewHolder.topWrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.onLongItemClick(v, position);
                return false;
            }
        });

        mViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemActionsListener != null)
                    itemActionsListener.deleteItemAction(position);
            }
        });

        mViewHolder.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemActionsListener!=null)
                    itemActionsListener.setItemAction(position);
            }
        });


        return convertView;
    }



    private Context getContext() {
        return context;
    }

    private class DictionaryItemViewHolder {

        SwipeLayout rootLayout;
        RelativeLayout topWrapper;
        BootstrapButton deleteBtn;
        BootstrapButton setBtn;
        TextView tvTitle;
        TextView tvWordsCount;
        TextView tvFirstWords;

        public DictionaryItemViewHolder(View item) {
            rootLayout = (SwipeLayout) item.findViewById(R.id.rootLayout);
            topWrapper = (RelativeLayout) item.findViewById(R.id.top_wrapper);
            deleteBtn = (BootstrapButton) item.findViewById(R.id.deleteBtn);
            setBtn = (BootstrapButton) item.findViewById(R.id.setBtn);
            tvTitle = (TextView) item.findViewById(R.id.titleTv);
            tvWordsCount = (TextView) item.findViewById(R.id.wordsCountTv);
            tvFirstWords = (TextView) item.findViewById(R.id.firstWordsTv);

            //Swipe Layout config
            rootLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            rootLayout.addDrag(SwipeLayout.DragEdge.Right, item.findViewById(R.id.bottom_wrapper));
        }
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
