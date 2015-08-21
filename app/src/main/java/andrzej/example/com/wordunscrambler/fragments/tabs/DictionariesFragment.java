package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.activities.DictionaryActivity;
import andrzej.example.com.wordunscrambler.adapters.DictionaryListAdapter;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.interfaces.ItemActionsListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.Converter;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;


public class DictionariesFragment extends Fragment implements ItemActionsListener {

    public static final String TAG = "DICTIONARIES_FRAGMENT_TAG";
    private static final String FILES_DIR = "files";

    //UI Init
    LinearLayout noDictionariesLayout;
    ParallaxListView dictionariesListView;

    //Adapter
    DictionaryListAdapter mAdapter;

    //Array
    List<Dictionary> dictionaries = new ArrayList<>();
    List<File> filesDirectory = new ArrayList<>();

    public DictionariesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init adapter
        mAdapter = new DictionaryListAdapter(getActivity(), dictionaries);
    }

    @Override
    public void onResume() {
        super.onResume();

        update();

        if (dictionaries.size() > 0) {
            dictionariesListView.setSelection(TabsConfig.CURRENT_DICTIONARY_POSITION);
        } else
            TabsConfig.CURRENT_DICTIONARY_POSITION = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dictionaries, container, false);

        //UI init
        noDictionariesLayout = (LinearLayout) v.findViewById(R.id.noDictionariesLayout);
        dictionariesListView = (ParallaxListView) v.findViewById(R.id.dictionaryListView);

        //Init
        dictionariesListView.setAdapter(mAdapter);

        //Listeners
        mAdapter.registerItemActionsListener(this);

        mAdapter.notifyDataSetChanged();

        return v;
    }

    private void update() {
        // Get local files
        Log.e(null, "data dir: " + getActivity().getApplicationInfo().dataDir + "/" + FILES_DIR);
        File directory = new File(getActivity().getApplicationInfo().dataDir + "/" + FILES_DIR);

        dictionaries.clear();
        filesDirectory.clear();

        if (directory.listFiles() != null && directory.listFiles().length > 0) {
            noDictionariesLayout.setVisibility(View.GONE);
            dictionariesListView.setVisibility(View.VISIBLE);
            File[] listOfLocalFilesArray = directory.listFiles();

            // get the names of files
            for (File file : listOfLocalFilesArray) {
                filesDirectory.add(file);
                dictionaries.add(new Dictionary(file.getName(), file));
            }
        } else {
            noDictionariesLayout.setVisibility(View.VISIBLE);
            dictionariesListView.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int position) {
        TabsConfig.CURRENT_DICTIONARY_POSITION = position;
        Intent dictionaryIntent = new Intent(getActivity(), DictionaryActivity.class);
        startActivity(dictionaryIntent);
    }

    @Override
    public void onLongItemClick(View v, int position) {
        //Don't have idea to put here
    }

    @Override
    public void deleteItemAction(int position) {
        Dictionary dictionaryToRemove = dictionaries.get(position);
        dictionaryToRemove.removeFile();
        dictionaries.remove(position);
        update();
    }
}
