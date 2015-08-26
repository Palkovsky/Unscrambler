package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.utils.StringOpearations;
import andrzej.example.com.wordunscrambler.views.ExpandableLayout;
import andrzej.example.com.wordunscrambler.views.MaterialEditText;


public class UnscrambleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UNSCRAMBLE_FRAGMENT_TAG";


    //UI elements declaration
    ExpandableLayout expandableFormLayout;
    private ImageButton expandBtn;
    private ImageButton unscrambleBtn;
    private MaterialEditText scrambledWordEditor;
    private MaterialEditText startsWithEditor;
    private MaterialEditText endsWithEditor;
    private MaterialEditText containsEditor;
    private MaterialEditText lengthEditor;
    private Spinner spinnerSortingTypes;

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
        expandBtn = (ImageButton) v.findViewById(R.id.expandFormBtn);
        unscrambleBtn = (ImageButton) v.findViewById(R.id.unscrambleWordBtn);
        scrambledWordEditor = (MaterialEditText) v.findViewById(R.id.scrambledWordEditor);
        startsWithEditor = (MaterialEditText) v.findViewById(R.id.startsWithEditor);
        endsWithEditor = (MaterialEditText) v.findViewById(R.id.endsWithEditor);
        containsEditor = (MaterialEditText) v.findViewById(R.id.containsEditor);
        lengthEditor = (MaterialEditText) v.findViewById(R.id.lengthEditor);
        spinnerSortingTypes = (Spinner) v.findViewById(R.id.spinnerSortingTypes);

        //Listeners
        expandBtn.setOnClickListener(this);
        unscrambleBtn.setOnClickListener(this);

        //View setup


        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.expandFormBtn:
                if (expandableFormLayout.isOpened()) {
                    expandableFormLayout.hide();
                } else {
                    expandableFormLayout.show();
                }
                break;


            case R.id.unscrambleWordBtn:
                String scrambledWord = scrambledWordEditor.getText().toString().trim();
                String startsWith = startsWithEditor.getText().toString().trim();
                String endsWith = endsWithEditor.getText().toString().trim();
                String contains = containsEditor.getText().toString().trim();
                int length = 0;
                if (lengthEditor.getText().length() > 0)
                    length = Integer.parseInt(lengthEditor.getText().toString().trim());


                if (isInputProper(scrambledWord, startsWith, endsWith, contains, length))
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private boolean isInputProper(String scrambledWord, String startsWith, String endsWith, String contains, int length) {

        if (scrambledWord.length() == 0) {
            Log.e(null, "Enter scrambled word");
            return false;
        } else if (startsWith.length() > scrambledWord.length()) {
            Log.e(null, "Starts wth za długie");
            return false;
        } else if (!startsWith.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, startsWith)) {
            Log.e(null, "Starts with zawiara nieodpowiednie znaków.");
            return false;
        } else if (endsWith.length() > scrambledWord.length()) {
            Log.e(null, "Ends with za długie");
            return false;
        } else if (!endsWith.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, endsWith)) {
            Log.e(null, "Ends with zawiara nieodpowiednie znaków.");
            return false;
        } else if (contains.length() > scrambledWord.length()) {
            Log.e(null, "Contains za długie");
            return false;
        } else if (!contains.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, contains)) {
            Log.e(null, "Contains zawiara nieodpowiednie znaków.");
            return false;
        } else if (length == 0) {
            Log.e(null, "Len cannot be 0");
            return false;
        } else if (length > scrambledWord.length()) {
            Log.e(null, "Len cannot be bigr than scrambled word len");
            return false;
        }

        return true;
    }
}
