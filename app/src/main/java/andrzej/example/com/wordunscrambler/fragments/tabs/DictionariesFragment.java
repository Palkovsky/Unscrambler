package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;


public class DictionariesFragment extends Fragment {

    public static final String TAG = "DICTIONARIES_FRAGMENT_TAG";
    private static final String FILES_DIR = "files";

    //Array
    List<File> filesDirectory = new ArrayList<>();

    public DictionariesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dictionaries, container, false);

        //UI init


        //Listeners

        return v;
    }

    private void update(){
        // Get local files
        Log.e(null, "data dir: " + getActivity().getApplicationInfo().dataDir + "/" + FILES_DIR);
        filesDirectory.clear();
        File directory = new File(getActivity().getApplicationInfo().dataDir + "/" + FILES_DIR);

        if (directory.listFiles() != null && directory.listFiles().length > 0) {
            File[] listOfLocalFilesArray = directory.listFiles();

            // get the names of files
            for (File file : listOfLocalFilesArray) {
                filesDirectory.add(file);
            }
        }

        Log.e(null, "Files Directory size: " + filesDirectory.size());

        if(filesDirectory.size()>0) {
            Log.e(null, "Lines count of: " + filesDirectory.get(0).getName());
        }
    }
}
