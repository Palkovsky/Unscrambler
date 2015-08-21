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

import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 21.08.15.
 */
public class DictionaryListAdapter extends BaseAdapter {

    private static final int FIRST_WORDS_TO_LOAD = 5;

    private Context context;
    private List<Dictionary> mDataset;
    private LayoutInflater inflater;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        DictionaryItemViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dictionary_list_item, parent, false);
            mViewHolder = new DictionaryItemViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (DictionaryItemViewHolder) convertView.getTag();
        }

        Dictionary dictionary = getItem(position);

        /*
        if(position%2 == 0)
            setViewBackground(mViewHolder.rootLayout, ContextCompat.getDrawable(getContext(), R.drawable.item_selector_white));
        else
            setViewBackground(mViewHolder.rootLayout, ContextCompat.getDrawable(getContext(), R.drawable.item_selector_gray));
    */


        mViewHolder.tvTitle.setText(dictionary.getName());
        mViewHolder.tvWordsCount.setText(String.valueOf(dictionary.getWordsCount()));
        mViewHolder.tvFirstWords.setText(dictionary.getFirstNWordsInString(FIRST_WORDS_TO_LOAD));
        

        return convertView;
    }

    private Context getContext() {
        return context;
    }

    private class DictionaryItemViewHolder {

        RelativeLayout rootLayout;
        TextView tvTitle;
        TextView tvWordsCount;
        TextView tvFirstWords;

        public DictionaryItemViewHolder(View item) {
            rootLayout = (RelativeLayout) item.findViewById(R.id.rootLayout);
            tvTitle = (TextView) item.findViewById(R.id.titleTv);
            tvWordsCount = (TextView) item.findViewById(R.id.wordsCountTv);
            tvFirstWords = (TextView) item.findViewById(R.id.firstWordsTv);
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
}
