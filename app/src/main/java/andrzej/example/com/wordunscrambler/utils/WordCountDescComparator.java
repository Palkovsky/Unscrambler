package andrzej.example.com.wordunscrambler.utils;

import java.util.Comparator;

import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 22.08.15.
 */
public class WordCountDescComparator implements Comparator<Dictionary> {
    @Override
    public int compare(Dictionary lhs, Dictionary rhs) {
        return rhs.getWordCount() - lhs.getWordCount();
    }
}
