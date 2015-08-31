package andrzej.example.com.wordunscrambler.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.commonsware.cwac.richedit.RichEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.fragments.tabs.DictionariesFragment;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.Converter;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;
import andrzej.example.com.wordunscrambler.utils.PathObject;
import andrzej.example.com.wordunscrambler.views.MaterialEditText;

public class DictionaryActivity extends AppCompatActivity implements TextWatcher {

    public static final String DICTIONARY_PATH_BUNDLE_KEY = "dictionary_path";
    private static final int MAX_NAME_LEN = 20;

    private static final String[] disallowedChars = {"/", "\\", "?", ",", "&", "^", "!", ">", "<", "..", "~"};

    //UI elements declaration
    Toolbar toolbar;
    TextView wordsCountTextView;
    MaterialEditText dictionaryNameEditor;
    RichEditText dictionaryContentEditor;

    Dictionary dictionary;

    private int wordsCount = 0;

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
        wordsCountTextView = (TextView) findViewById(R.id.wordsCountTextView);
        dictionaryNameEditor = (MaterialEditText) findViewById(R.id.dictionaryNameEditor);
        dictionaryContentEditor = (RichEditText) findViewById(R.id.dictionaryContentEditor);


        //Init
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        dictionaryContentEditor.addTextChangedListener(this);

        update();
    }

    private void update() {
        dictionaryNameEditor.setText(dictionary.getName());

        if (dictionary.getFile() != null)
            dictionaryContentEditor.setText(Converter.getTextFileContents(dictionary.getFile()), TextView.BufferType.EDITABLE);
        else
            dictionaryNameEditor.requestFocus();

        updateWordsCount();
    }

    private void updateWordsCount() {
        String[] chunks = dictionaryContentEditor.getText().toString().split("\\s+");
        int len = chunks.length;
        if (len == 1) {
            if (chunks[0].trim().equals(""))
                wordsCountTextView.setText("(" + String.valueOf(0) + ")");
            else
                wordsCountTextView.setText("(" + String.valueOf(len) + ")");
        } else
            wordsCountTextView.setText("(" + String.valueOf(len) + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (dictionary.getFile() == null)
            getMenuInflater().inflate(R.menu.menu_dictionary_new, menu);
        else
            getMenuInflater().inflate(R.menu.menu_dictionary, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DictionariesFragment.otherWindowOpened = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        DictionariesFragment.paused = false;

        switch (id) {

            case R.id.menu_deleteDictionary:

                DictionariesFragment.paused = false;
                TabsConfig.CURRENT_TAB_NUM = 1;

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
                                overridePendingTransition(R.anim.dummy_animation_back, R.anim.right_to_left_animation);
                            }
                        })
                        .show();

                break;

            case R.id.menu_saveDictionary:
                //Save logic
                DictionariesFragment.paused = false;

                String nameString = dictionaryNameEditor.getText().toString();

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
                    else if (stringContainsItemFromList(dictionaryNameEditor.getText().toString(), disallowedChars))
                        Toast.makeText(getApplicationContext(), R.string.name_contains_disallowed_char, Toast.LENGTH_SHORT).show();
                    else if (nameString.startsWith(".") || nameString.startsWith("/") || nameString.toString().startsWith("\\") || nameString.toString().startsWith("~"))
                        Toast.makeText(getApplicationContext(), R.string.name_starts_with_wrong_name, Toast.LENGTH_SHORT).show();
                    else if (nameString.trim().length() > MAX_NAME_LEN)
                        Toast.makeText(getApplicationContext(), R.string.name_too_long, Toast.LENGTH_SHORT).show();
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

                        TabsConfig.CURRENT_TAB_NUM = 1;
                        finish();
                        overridePendingTransition(R.anim.dummy_animation_back, R.anim.right_to_left_animation);
                    } else
                        Toast.makeText(getApplicationContext(), R.string.file_already_exsists, Toast.LENGTH_SHORT).show();

                } else {

                    DictionariesFragment.updateCurrent = true;

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
                        else if (stringContainsItemFromList(dictionaryNameEditor.getText().toString(), disallowedChars))
                            Toast.makeText(getApplicationContext(), R.string.name_contains_disallowed_char, Toast.LENGTH_SHORT).show();
                        else if (nameString.startsWith(".") || nameString.startsWith("/") || nameString.toString().startsWith("\\") || nameString.toString().startsWith("~"))
                            Toast.makeText(getApplicationContext(), R.string.name_starts_with_wrong_name, Toast.LENGTH_SHORT).show();
                        else if (nameString.trim().length() > MAX_NAME_LEN)
                            Toast.makeText(getApplicationContext(), R.string.name_too_long, Toast.LENGTH_SHORT).show();

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
                            if (name.equals(dictionary.getFile().getName()))
                                dictionary.getFile().renameTo(directory);
                            else
                                dictionary.getFile().delete();
                            dictionary.setFile(directory);


                            if (updatePrefs)
                                DictionaryUtils.setDictionaryPreference(getApplicationContext(), dictionary);

                            TabsConfig.CURRENT_TAB_NUM = 1;
                            finish();
                            overridePendingTransition(R.anim.dummy_animation_back, R.anim.right_to_left_animation);
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
                overridePendingTransition(R.anim.dummy_animation_back, R.anim.right_to_left_animation);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dictionaryNameEditor.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(dictionaryContentEditor.getWindowToken(), 0);
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

    private boolean stringContainsItemFromList(String inputString, String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (inputString.contains(items[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateWordsCount();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.dummy_animation_back, R.anim.right_to_left_animation);
    }
}
