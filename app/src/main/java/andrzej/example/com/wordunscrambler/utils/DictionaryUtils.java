package andrzej.example.com.wordunscrambler.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import andrzej.example.com.wordunscrambler.config.SharedPreferenceKeys;
import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 21.08.15.
 */
public class DictionaryUtils {

    public static final String DEFAULT_DICTIONARY_PATH = "";

    private static SharedPreferences prefs;

    public static void setDictionaryPreference(Context context, Dictionary dictionary) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        if (dictionary != null)
            editor.putString(SharedPreferenceKeys.KEY_CURRENT_DICTIONARY_PATH, dictionary.getFile().toString());
        else
            editor.putString(SharedPreferenceKeys.KEY_CURRENT_DICTIONARY_PATH, DEFAULT_DICTIONARY_PATH);
        editor.commit();
    }

    public static boolean isCurrentDictionary(Context context, Dictionary dictionary) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String currentPath = prefs.getString(SharedPreferenceKeys.KEY_CURRENT_DICTIONARY_PATH, DEFAULT_DICTIONARY_PATH);

        return dictionary.getFile().toString().equals(currentPath);
    }


    public static Dictionary getCurrentDictionary(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currentPath = prefs.getString(SharedPreferenceKeys.KEY_CURRENT_DICTIONARY_PATH, DEFAULT_DICTIONARY_PATH);

        if (currentPath != null && !currentPath.equals("")) {
            File file = new File(currentPath);
            return new Dictionary(FilenameUtils.removeExtension(file.getName()), file);
        }
        return null;
    }

    public static Dictionary getDictionaryFromPath(String path){
        File file = new File(path);

        if(file != null){
            return new Dictionary(file.getName(), file);
        }
        return null;
    }
}
