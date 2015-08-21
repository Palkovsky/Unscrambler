package andrzej.example.com.wordunscrambler.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.fragments.BackHandledFragment;
import andrzej.example.com.wordunscrambler.fragments.BrowseFragment;
import andrzej.example.com.wordunscrambler.fragments.TabsFragment;
import andrzej.example.com.wordunscrambler.fragments.tabs.DictionariesFragment;
import andrzej.example.com.wordunscrambler.fragments.tabs.UnscrambleFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BackHandledFragment.BackHandlerInterface {

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    //UI elements
    Toolbar toolbar;

    //Fragments
    TabsFragment tabsFragment;
    public static BrowseFragment browseFragment;
    DictionariesFragment dictionariesFragment;
    UnscrambleFragment unscrambleFragment;

    private BackHandledFragment selectedFragment;

    //Utils
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment init
        browseFragment = new BrowseFragment();
        tabsFragment = new TabsFragment();
        unscrambleFragment = new UnscrambleFragment();
        dictionariesFragment = new DictionariesFragment();


        //Utils init
        fragmentManager = getSupportFragmentManager();

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setFragment(tabsFragment);

    }

    @Override
    public void onClick(View v) {

    }

    private void setFragment(BackHandledFragment fragment) {

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, TabsFragment.TAG)
                .commit();

        setSelectedFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment == null || !selectedFragment.onBackPressed())
            moveTaskToBack(true);

    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }
}
