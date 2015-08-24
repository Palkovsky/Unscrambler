package andrzej.example.com.wordunscrambler.models;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import andrzej.example.com.wordunscrambler.adapters.SwipeDictionaryListAdapter;
import andrzej.example.com.wordunscrambler.utils.Converter;

/**
 * Created by andrzej on 21.08.15.
 */
public class Dictionary {

    private String name;
    private File file;
    private int wordCount;
    private boolean current = false;

    private String firstWords;

    public Dictionary(String name, File file) {
        this.name = FilenameUtils.removeExtension(name);
        this.file = file;
        if (file != null)
            this.wordCount = getWordsCount();
    }

    private int getWordsCount() {
        String[] chunks = getContentChunks();

        int wordsCount = 0;

        if (chunks.length == 1) {
            if (chunks[0].trim().equals(""))
                wordsCount = 0;
            else
                wordsCount = chunks.length;
        } else
            wordsCount = chunks.length;

        String firstWords = "";

        int n = SwipeDictionaryListAdapter.FIRST_WORDS_TO_LOAD;
        int nCopy = n;
        if (n >= chunks.length)
            n = chunks.length;


        if (wordsCount == 0) {
            firstWords = "...";
        } else {
            for (int i = 0; i < n; i++) {
                if (i == 0)
                    firstWords += chunks[i];
                else
                    firstWords += ", " + chunks[i];
            }

            if (nCopy < chunks.length)
                firstWords += "...";
        }

        setFirstWords(firstWords);

        return wordsCount;
    }

    public String[] getFirstNWords(int n) {
        String[] words = getContentChunks();
        String[] firstNWords = new String[n];

        for (int i = 0; i < n; i++) {
            firstNWords[i] = words[i];
        }

        return firstNWords;
    }

    public String getFirstNWordsInString(int n) {
        String[] words = getContentChunks();
        String firstWords = "";

        int nCopy = n;

        if (n >= words.length)
            n = words.length;

        for (int i = 0; i < n; i++) {
            if (i == 0)
                firstWords += words[i];
            else
                firstWords += words[i];
        }

        if (nCopy < words.length)
            firstWords += "...";

        return firstWords;
    }

    public String[] getContentChunks() {
        return Converter.getTextFileContents(getFile()).split("\\s+");
    }

    public void removeFile() {
        getFile().delete();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FilenameUtils.removeExtension(name);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        wordCount = getWordsCount();
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public int getWordCount() {
        return wordCount;
    }

    private void setFirstWords(String firstWords) {
        this.firstWords = firstWords;
    }

    public String getFirstWords() {
        return firstWords;
    }
}
