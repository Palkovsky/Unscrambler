package andrzej.example.com.wordunscrambler.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.ResultSortingMethod;
import andrzej.example.com.wordunscrambler.views.AnimatedExpandableListView;

/**
 * Created by andrzej on 27.08.15.
 */
public class WordResultsAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context context;

    private List<String> headers = new ArrayList<>();
    private HashMap<String, List<String>> words = new HashMap<>();

    private LayoutInflater inflater;

    public WordResultsAdapter(Context context, List<String> headers, HashMap<String, List<String>> words) {
        this.context = context;
        this.headers = headers;
        this.words = words;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return words.get(headers.get(groupPosition)).size();
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return words.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.found_word_group, null);
        }

        String title = (String) this.getGroup(groupPosition);
        TextView titleTv = (TextView) convertView.findViewById(R.id.groupTitle);

        titleTv.setText(title);

        if (isExpanded)
            titleTv.setTextColor(getContext().getResources().getColor(R.color.ColorPrimaryDark));
        else
            titleTv.setTextColor(getContext().getResources().getColor(android.R.color.white));

        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.found_word_item, null);
        }

        String word = (String) this.getChild(groupPosition, childPosition);

        TextView wordTv = (TextView) convertView.findViewById(R.id.itemTitle);
        wordTv.setText(word);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public Context getContext() {
        return context;
    }
}
