package andrzej.example.com.wordunscrambler.utils;

import java.util.Comparator;

import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 22.08.15.
 */
public class NameComparatorDesc implements Comparator<Dictionary> {
    @Override
    public int compare(Dictionary lhs, Dictionary rhs) {
        return rhs.getName().compareTo(lhs.getName());
    }
}
