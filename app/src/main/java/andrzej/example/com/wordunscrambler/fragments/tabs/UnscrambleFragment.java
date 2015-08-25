package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.views.ExpandableLayout;


public class UnscrambleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UNSCRAMBLE_FRAGMENT_TAG";


    //UI elements declaration
    ExpandableLayout expandableFormLayout;
    private Button expandBtn;

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
        expandableFormLayout = (ExpandableLayout) v.findViewById(R.id.formExpandableLayout);
        expandBtn = (Button) v.findViewById(R.id.expandFormBtn);


        //Listeners
        expandBtn.setOnClickListener(this);

        //View setup


        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.expandFormBtn:
                if(expandableFormLayout.isOpened())
                    expandableFormLayout.hide();
                else
                    expandableFormLayout.show();
                break;
        }

    }
}
