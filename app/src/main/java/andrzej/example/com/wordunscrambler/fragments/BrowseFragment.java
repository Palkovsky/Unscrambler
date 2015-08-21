package andrzej.example.com.wordunscrambler.fragments;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.adapters.FileListAdapter;
import andrzej.example.com.wordunscrambler.models.FileItem;
import andrzej.example.com.wordunscrambler.utils.PathObject;


public class BrowseFragment extends BackHandledFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

    public static final String TAG = "BROWSE_FRAGMENT_TAG";

    //UI elements
    LinearLayout upDirectoryContainer;
    LinearLayout noFilesLayout;
    TextView currentDirectoryName;
    ListView filesListView;

    //Arrays
    List<FileItem> files = new ArrayList<>();

    //Adapters
    FileListAdapter mAdapter;

    //Utils
    PathObject path;
    private ActionMode mActionMode;

    //Logic vars
    private int nr = 0;

    public BrowseFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing utils
        path = new PathObject(PathObject.DEFAULT_PATH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_browse, container, false);

        //((AppCompatActivity) getActivity()).getSupportActionBar();

        //Initializing UI elements
        upDirectoryContainer = (LinearLayout) v.findViewById(R.id.goUpDirectoryButton);
        noFilesLayout = (LinearLayout) v.findViewById(R.id.noFilesLayout);
        currentDirectoryName = (TextView) v.findViewById(R.id.currentDirectoryTv);
        filesListView = (ListView) v.findViewById(R.id.filesListView);

        //Setting up listeners
        upDirectoryContainer.setOnClickListener(this);
        filesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        filesListView.setOnItemClickListener(this);

        //Init Adapter
        mAdapter = new FileListAdapter(getActivity(), files, filesListView);

        filesListView.setMultiChoiceModeListener(this);

        //Few inits
        filesListView.setAdapter(mAdapter);
        update();

        return v;
    }

    private void update() {
        currentDirectoryName.setText(path.currentDirectory());

        files.clear();

        List<String> contents = path.getContents();
        for (String file : contents) {
            File fileItem = new File(path.getDirectory(file));

            if (!fileItem.isDirectory()) {
                if (path.testFileExtension(file))
                    files.add(new FileItem(file, fileItem.isDirectory()));
            } else
                files.add(new FileItem(file, fileItem.isDirectory()));
        }

        if (files.size() == 0)
            noFilesLayout.setVisibility(View.VISIBLE);
        else
            noFilesLayout.setVisibility(View.GONE);

        mAdapter.notifyDataSetChanged();
        filesListView.setSelectionAfterHeaderView();
    }

    @Override
    public boolean onBackPressed() {
        if (path.hasOverridingDirectory()) {
            path.goUp();
            update();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new TabsFragment(), TabsFragment.TAG)
                    .commit();
        }
        return true;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goUpDirectoryButton:
                if (path.hasOverridingDirectory()) {
                    path.goUp();
                    update();
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new TabsFragment(), TabsFragment.TAG)
                            .commit();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileItem item = files.get(position);
        if (item.isDirectory()) {
            path.goToDirectory(item.getName());
            update();
        } else {
            //Do sth with file
            Toast.makeText(getActivity(), "File", Toast.LENGTH_SHORT).show();
        }

        mAdapter.notifyDataSetChanged();

        if (mActionMode != null)
            mActionMode.finish();
    }

    private String pluralizeModeTitle(int count) {
        if (count == 1)
            return count + " " + getActivity().getResources().getString(R.string.singularcab);
        else
            return count + " " + getActivity().getResources().getString(R.string.pluralcab);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            nr++;
            mAdapter.setNewSelection(position, checked);
        } else {
            nr--;
            mAdapter.removeSelection(position);
        }
        mode.setTitle(pluralizeModeTitle(nr));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        nr = 0;
        mActionMode = mode;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.delete_selection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_import:
                Toast.makeText(getActivity(), "Count: " + mAdapter.getSelectedItems().size(), Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mAdapter.clearSelection();
        mActionMode = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActionMode != null)
            mActionMode.finish();
    }
}
