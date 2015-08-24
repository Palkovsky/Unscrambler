package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.views.ExpandableRelativeLayout;


public class UnscrambleFragment extends Fragment {

    public static final String TAG = "UNSCRAMBLE_FRAGMENT_TAG";


    //UI elements declaration

    public UnscrambleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unscramble, container, false);

        //UI elements construction



        //Listeners


        //View setup


        return v;
    }



}
