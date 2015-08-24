package andrzej.example.com.wordunscrambler.utils;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import andrzej.example.com.wordunscrambler.adapters.SwipeDictionaryListAdapter;
import andrzej.example.com.wordunscrambler.interfaces.DictionariesInitEndListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 24.08.15.
 */
public class InitDictionaryListAsync extends AsyncTask<Void, Dictionary, Void>{

    private DictionariesInitEndListener dictionariesInitEndListener;
    private List<Dictionary> dictionaries;
    private ProgressBar progressBar;
    File[] listOfLocalFilesArray;

    public InitDictionaryListAsync(List<Dictionary> dictionaries, File[] listOfLocalFilesArray, ProgressBar progressBar) {
        this.dictionaries = dictionaries;
        this.listOfLocalFilesArray = listOfLocalFilesArray;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dictionaries.clear();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        // get the names of files
        for (File file : listOfLocalFilesArray) {
            Dictionary dictionary = new Dictionary(file.getName(), file);
            publishProgress(dictionary);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Dictionary... values) {
        super.onProgressUpdate(values);
        dictionaries.add(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(dictionariesInitEndListener != null)
            dictionariesInitEndListener.onInitialize(dictionaries);
    }

    public void setDictionariesInitEndListener(DictionariesInitEndListener dictionariesInitEndListener) {
        this.dictionariesInitEndListener = dictionariesInitEndListener;

    }
}
