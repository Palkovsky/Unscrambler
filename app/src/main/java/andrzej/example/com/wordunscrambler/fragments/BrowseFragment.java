package andrzej.example.com.wordunscrambler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andrzej.example.com.wordunscrambler.R;


public class BrowseFragment extends BackHandledFragment {

    public static final String TAG = "BROWSE_FRAGMENT_TAG";

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_browse, container, false);

        return v;
    }



    @Override
    public boolean onBackPressed() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new TabsFragment(), TabsFragment.TAG)
                .commit();
        return true;
    }

    @Override
    public String getTagText() {
        return TAG;
    }
}
