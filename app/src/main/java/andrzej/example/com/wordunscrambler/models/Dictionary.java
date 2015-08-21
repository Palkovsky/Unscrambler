package andrzej.example.com.wordunscrambler.models;

import java.io.File;

import andrzej.example.com.wordunscrambler.utils.Converter;

/**
 * Created by andrzej on 21.08.15.
 */
public class Dictionary {

    private String name;
    private File file;
    private boolean current = false;

    public Dictionary(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public int getWordsCount() {
        return getContentChunks().length;
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
                firstWords += ", " + words[i];
        }

        if (nCopy < words.length)
            firstWords += "...";

        return firstWords;
    }

    public String[] getContentChunks() {
        return Converter.getTextFileContents(getFile()).split("\\s+");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}