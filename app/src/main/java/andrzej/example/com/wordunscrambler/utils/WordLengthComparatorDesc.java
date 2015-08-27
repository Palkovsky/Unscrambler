package andrzej.example.com.wordunscrambler.utils;

import java.util.Comparator;

/**
 * Created by andrzej on 27.08.15.
 */
public class WordLengthComparatorDesc implements Comparator<String> {
    @Override
    public int compare(String lhs, String rhs) {
        return rhs.length() - lhs.length();
    }
}
