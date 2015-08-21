package andrzej.example.com.wordunscrambler.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.models.FileItem;

/**
 * Created by andrzej on 20.08.15.
 */
public class FileListAdapter extends BaseAdapter {

    private Context context;
    private List<FileItem> mDataset;
    private ListView listView;

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private List<FileItem> selectedItems = new ArrayList<FileItem>();

    LayoutInflater inflater;

    public FileListAdapter(Context context, List<FileItem> mDataset, ListView listView) {
        this.context = context;
        this.mDataset = mDataset;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public FileItem getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FileItemViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_explorer_item, parent, false);
            mViewHolder = new FileItemViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (FileItemViewHolder) convertView.getTag();
        }

        final FileItem fileItem = getItem(position);

        if (mSelection.get(position) != null)
            mViewHolder.rootLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        else
            setViewBackground(mViewHolder.rootLayout, ContextCompat.getDrawable(getContext(), R.drawable.item_selector));


        mViewHolder.tvName.setText(fileItem.getName());


        if (selectedItems.contains(fileItem) /*&& !fileItem.isDirectory() */) {
            mViewHolder.addCheckbox.setChecked(true);
        }else {
            mViewHolder.addCheckbox.setChecked(false);
        }

        if(fileItem.isDirectory()){
            mViewHolder.ivIcon.setImageResource(R.drawable.ic_folder_grey600_24dp);
        }else
            mViewHolder.ivIcon.setImageResource(android.R.color.transparent);

        return convertView;
    }

    public List<FileItem> getSelectedItems() {
        return selectedItems;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        if (!selectedItems.contains(mDataset.get(position)))
            selectedItems.add(mDataset.get(position));
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        selectedItems.remove(mDataset.get(position));
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        selectedItems = new ArrayList<FileItem>();
        notifyDataSetChanged();
    }


    private Context getContext() {
        return context;
    }

    private class FileItemViewHolder {
        RelativeLayout rootLayout;
        TextView tvName;
        ImageView ivIcon;
        CheckedTextView addCheckbox;

        public FileItemViewHolder(View item) {
            rootLayout = (RelativeLayout) item.findViewById(R.id.rootLayout);
            tvName = (TextView) item.findViewById(R.id.nameTv);
            ivIcon = (ImageView) item.findViewById(R.id.iconIv);
            addCheckbox = (CheckedTextView) item.findViewById(R.id.addCheckBox);
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
