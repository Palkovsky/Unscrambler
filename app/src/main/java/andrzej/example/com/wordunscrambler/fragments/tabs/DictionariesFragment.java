package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.activities.MainActivity;
import andrzej.example.com.wordunscrambler.fragments.BrowseFragment;
import andrzej.example.com.wordunscrambler.fragments.TabsFragment;


public class DictionariesFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DICTIONARIES_FRAGMENT_TAG";


    private Button browseFilesBtn;

    public DictionariesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dictionaries, container, false);

        //UI init
        browseFilesBtn = (Button) v.findViewById(R.id.browseFilesBtn);

        //Listeners
        browseFilesBtn.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browseFilesBtn:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BrowseFragment(), BrowseFragment.TAG)
                        .commit();
                break;
        }
    }


}
