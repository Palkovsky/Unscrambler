package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import andrzej.example.com.wordunscrambler.R;


public class UnscrambleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UNSCRAMBLE_FRAGMENT_TAG";


    //UI elements declaration
    FrameLayout editFormSceneContainer;
    Button testExpandBtn;

    //Scene
    Scene baseFormScene;
    Scene extendedFormScene;
    Scene currentScene;

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
        editFormSceneContainer = (FrameLayout) v.findViewById(R.id.editFormSceneContainer);
        testExpandBtn = (Button) v.findViewById(R.id.expandFormBtn);


        //Scenes setup
        baseFormScene = Scene.getSceneForLayout(editFormSceneContainer, R.layout.fragment_edit_form_scene_base, getActivity());
        extendedFormScene = Scene.getSceneForLayout(editFormSceneContainer, R.layout.fragment_edit_from_scene_expanded, getActivity());
        currentScene = baseFormScene;


        //Listeners
        //expandFormBtn_1.setOnClickListener(this);
        //expandFormBtn_2.setOnClickListener(this);

        //View setup
        TransitionManager.go(baseFormScene);

        return v;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.expandFormBtn:
                TransitionSet set = new TransitionSet();
                Slide slide = new Slide(Gravity.TOP);
                slide.addTarget(R.id.extendedFormRootLayout);
                set.addTransition(slide);
                set.addTransition(new ChangeBounds());
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set.setDuration(350);

                if (currentScene == baseFormScene) {
                    TransitionManager.go(extendedFormScene, set);
                    currentScene = extendedFormScene;
                } else {
                    TransitionManager.go(baseFormScene, set);
                    currentScene = baseFormScene;
                }
                break;
        }
    }
}
