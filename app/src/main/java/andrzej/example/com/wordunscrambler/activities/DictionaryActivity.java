package andrzej.example.com.wordunscrambler.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.commonsware.cwac.richedit.RichEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.fragments.tabs.DictionariesFragment;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.Converter;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;
import andrzej.example.com.wordunscrambler.utils.PathObject;
import andrzej.example.com.wordunscrambler.views.MaterialEditText;

public class DictionaryActivity extends AppCompatActivity {

    public static final String DICTIONARY_PATH_BUNDLE_KEY = "dictionary_path";

    //UI elements declaration
    Toolbar toolbar;
    MaterialEditText dictionaryNameEditor;
    RichEditText dictionaryContentEditor;

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
        if (dictionary == null)
            dictionary = new Dictionary("", null);

        //UI init
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dictionaryNameEditor = (MaterialEditText) findViewById(R.id.dictionaryNameEditor);
        dictionaryContentEditor = (RichEditText) findViewById(R.id.dictionaryContentEditor);

        //Init
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        update();
    }

    private void update() {
        dictionaryNameEditor.setText(dictionary.getName());

        if (dictionary.getFile() != null)
            dictionaryContentEditor.setText(Converter.getTextFileContents(dictionary.getFile()));
        else
            dictionaryNameEditor.requestFocus();
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

        switch (id) {

            case R.id.menu_deleteDictionary:

                new MaterialDialog.Builder(this)
                        .title(R.string.delete)
                        .content(R.string.are_you_sure_to_delete)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                TabsConfig.CURRENT_DICTIONARY_POSITION = 0;
                                dictionary.getFile().delete();
                                finish();
                            }
                        })
                        .show();
                break;

            case R.id.menu_saveDictionary:
                //Save logic
                TabsConfig.CURRENT_TAB_NUM = 1;
                if (dictionary.getFile() == null) {
                    String name = ensureName(dictionaryNameEditor.getText().toString()).trim();
                    String content = dictionaryContentEditor.getText().toString().trim();

                    FileOutputStream fos = null;

                    boolean freeToEdit = true;

                    File localDirectory = new File(getApplicationContext().getApplicationInfo().dataDir + "/" + DictionariesFragment.FILES_DIR);
                    File[] listOfLocalFilesArray = localDirectory.listFiles();
                    for (File file : listOfLocalFilesArray) {
                        if (file.getName().equals(name)) {
                            freeToEdit = false;
                            break;
                        }
                    }

                    if (dictionaryNameEditor.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), R.string.empty_name, Toast.LENGTH_SHORT).show();
                    else if (dictionaryNameEditor.getText().toString().startsWith("."))
                        Toast.makeText(getApplicationContext(), R.string.dot_name, Toast.LENGTH_SHORT).show();
                    else if (freeToEdit) {
                        File newFile = new File(name);
                        try {
                            fos = getApplicationContext().openFileOutput(newFile.getName(), Context.MODE_PRIVATE);
                            fos.write(content.getBytes("UTF-8"));
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        File directory = new File(getApplicationContext().getApplicationInfo().dataDir + "/" + DictionariesFragment.FILES_DIR + "/" + name);

                        dictionary.setFile(directory);

                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), R.string.file_already_exsists, Toast.LENGTH_SHORT).show();

                } else {



                    String name = ensureName(dictionaryNameEditor.getText().toString()).trim();
                    String content = dictionaryContentEditor.getText().toString().trim();


                    //Need to check if there is a file with that name
                    FileOutputStream fos = null;
                    try {

                        boolean freeToEdit = true;

                        File localDirectory = new File(getApplicationContext().getApplicationInfo().dataDir + "/" + DictionariesFragment.FILES_DIR);
                        File[] listOfLocalFilesArray = localDirectory.listFiles();
                        for (File file : listOfLocalFilesArray) {
                            if (file.getName().equals(name) && !file.getName().equals(dictionary.getFile().getName())) {
                                freeToEdit = false;
                                break;
                            }
                        }

                        if (dictionaryNameEditor.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), R.string.empty_name, Toast.LENGTH_SHORT).show();
                        else if (dictionaryNameEditor.getText().toString().startsWith("."))
                            Toast.makeText(getApplicationContext(), R.string.dot_name, Toast.LENGTH_SHORT).show();
                        else if (freeToEdit) {

                            boolean updatePrefs = false;
                            if (DictionaryUtils.isCurrentDictionary(getApplicationContext(), dictionary))
                                updatePrefs = true;

                            File newFile = new File(name);
                            fos = getApplicationContext().openFileOutput(newFile.getName(), Context.MODE_PRIVATE);
                            fos.write(content.getBytes("UTF-8"));
                            fos.close();


                            File directory = new File(getApplicationContext().getApplicationInfo().dataDir + "/" + DictionariesFragment.FILES_DIR + "/" + name);


                            dictionary.setName(name);
                            dictionary.getFile().renameTo(directory);
                            dictionary.setFile(directory);


                            if (updatePrefs)
                                DictionaryUtils.setDictionaryPreference(getApplicationContext(), dictionary);

                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), R.string.file_already_exsists, Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String ensureName(String name) {

        if (!stringEndsWithItemFromList(name, PathObject.WHITELISTED_EXTENSIONS))
            name += ".txt";

        return name;
    }

    private boolean stringEndsWithItemFromList(String inputString, String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (inputString.endsWith("." + items[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        TabsConfig.CURRENT_TAB_NUM = 1;
    }
}
