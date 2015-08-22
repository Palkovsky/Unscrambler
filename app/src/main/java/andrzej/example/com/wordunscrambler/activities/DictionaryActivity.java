package andrzej.example.com.wordunscrambler.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;

public class DictionaryActivity extends AppCompatActivity {

    public static final String DICTIONARY_PATH_BUNDLE_KEY = "dictionary_path";

    //UI Init
    Toolbar toolbar;

    Dictionary dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        //Grab data passed via intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String dictionaryPath = extras.getString(DICTIONARY_PATH_BUNDLE_KEY);
            dictionary = DictionaryUtils.getDictionaryFromPath(dictionaryPath);
        }

        //If we want to create new dictionary
        if(dictionary == null)
            dictionary = new Dictionary("", null);

        //UI init
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Init
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
