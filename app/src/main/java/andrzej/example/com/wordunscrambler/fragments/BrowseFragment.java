package andrzej.example.com.wordunscrambler.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.adapters.FileListAdapter;
import andrzej.example.com.wordunscrambler.interfaces.AsyncTaskListener;
import andrzej.example.com.wordunscrambler.interfaces.ItemCheckedListener;
import andrzej.example.com.wordunscrambler.models.FileItem;
import andrzej.example.com.wordunscrambler.utils.FilesFinder;
import andrzej.example.com.wordunscrambler.utils.PathObject;


public class BrowseFragment extends BackHandledFragment implements View.OnClickListener, AbsListView.MultiChoiceModeListener, ItemCheckedListener {

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
        setHasOptionsMenu(true);

        //Initializing utils
        path = new PathObject(PathObject.DEFAULT_PATH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_browse, container, false);


        //Initializing UI elements
        upDirectoryContainer = (LinearLayout) v.findViewById(R.id.goUpDirectoryButton);
        noFilesLayout = (LinearLayout) v.findViewById(R.id.noFilesLayout);
        currentDirectoryName = (TextView) v.findViewById(R.id.currentDirectoryTv);
        filesListView = (ListView) v.findViewById(R.id.filesListView);

        //Init Adapter
        mAdapter = new FileListAdapter(getActivity(), files, filesListView);

        //Setting up listeners
        upDirectoryContainer.setOnClickListener(this);
        filesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mAdapter.setOnItemCheckedListener(this);


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
    public void onItemCheckStateChange(int position, View v, boolean isChecked) {
        //((CheckedTextView) v).setChecked(isChecked);
        filesListView.setItemChecked(position, isChecked);
    }

    @Override
    public void onItemClick(int position, View v) {
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

    @Override
    public void onLongItemClick(int position, View v) {

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

                if (mActionMode != null)
                    mActionMode.finish();

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
            case R.id.menu_import:

                SearchForDictionaries searchTask = new SearchForDictionaries(mAdapter.getSelectedItems());
                searchTask.registerAsyncTaskListener(new AsyncTaskListener() {
                    @Override
                    public void onPostExecute(final List<File> files) {

                        if (files.size() > 0) {
                            Toast.makeText(getActivity(), "Count: " + files.size(), Toast.LENGTH_SHORT).show();

                            String[] stringFiles = new String[files.size()];
                            Integer[] preselectedIndexes = new Integer[files.size()];

                            for (int i = 0; i < files.size(); i++) {
                                stringFiles[i] = files.get(i).getName();
                                preselectedIndexes[i] = i;
                            }

                            final List<File> chosenFiles = new ArrayList<File>();

                            new MaterialDialog.Builder(getActivity())
                                    .title(R.string.importing)
                                    .items(stringFiles)
                                    .itemsCallbackMultiChoice(preselectedIndexes, new MaterialDialog.ListCallbackMultiChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                            for(Integer index : which){
                                                chosenFiles.add(files.get(index));
                                            }

                                            if(chosenFiles.size()>0){

                                            }


                                            return true;
                                        }
                                    })
                                    .positiveText(R.string.importString)
                                    .negativeText(R.string.back)
                                    .show();

                        }else
                            Toast.makeText(getContext(), R.string.no_files_to_import, Toast.LENGTH_SHORT).show();

                    }
                });
                searchTask.execute();
                mActionMode.finish();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.browser_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_selectAll:
                for(int i = 0; i<files.size(); i++){
                    filesListView.setItemChecked(i, true);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SearchForDictionaries extends AsyncTask<Void, String, List<File>> {

        MaterialDialog progressDialog;
        private List<FileItem> selectedFiles;
        AsyncTaskListener asyncTaskListener;

        public SearchForDictionaries(List<FileItem> selectedItems) {
            this.selectedFiles = selectedItems;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog =
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.progress_dialog)
                            .content(R.string.please_wait)
                            .progress(true, 0)
                            .show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected List<File> doInBackground(Void... params) {

            List<String> paths = new ArrayList<>();

            List<File> files = new ArrayList<>();

            for (FileItem file : selectedFiles) {
                if (file.isDirectory())
                    paths.add(path.getDirectory(file.getName()));
                else
                    files.add(new File(file.getName()));
            }

            FilesFinder finder = new FilesFinder(path.getPath());
            for (String pathString : paths) {
                finder.setPath(path.getPath());
                files.addAll(finder.parseDirectory(pathString));
            }

            return files;
        }


        @Override
        protected void onPostExecute(List<File> files) {
            super.onPostExecute(files);
            progressDialog.setCancelable(true);
            progressDialog.hide();

            if (asyncTaskListener != null)
                asyncTaskListener.onPostExecute(files);
        }

        public void registerAsyncTaskListener(AsyncTaskListener asyncTaskListener) {
            this.asyncTaskListener = asyncTaskListener;
        }
    }
}
