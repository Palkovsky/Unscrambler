package andrzej.example.com.wordunscrambler.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import andrzej.example.com.wordunscrambler.config.ResultSortingMethod;

/**
 * Created by andrzej on 27.08.15.
 */
public class ResultListUtils {

    public static List<String> generateHeaders(List<String> unscrambledWords, int sortingMethod) {

        List<String> headers = new ArrayList<>();

        switch (sortingMethod) {
            case ResultSortingMethod.REVERSE_ALPHABETICAL:
            case ResultSortingMethod.ALPHABETICAL:

                for (int i = 0; i < unscrambledWords.size(); i++) {

                    String currentWord = unscrambledWords.get(i);
                    String currentWorldFirstChar = currentWord.substring(0, 1);

                    if (i + 1 < unscrambledWords.size() && !currentWorldFirstChar.equals(unscrambledWords.get(i + 1).substring(0, 1))) {
                        if (!headers.contains(currentWorldFirstChar.toUpperCase()))
                            headers.add(currentWorldFirstChar.toUpperCase());
                    } else if (i + 1 == unscrambledWords.size()) {
                        if (!headers.contains(currentWorldFirstChar.toUpperCase()))
                            headers.add(currentWorldFirstChar.toUpperCase());
                    }
                }

                break;
            default: //Ascending & Descending

                for (int i = 0; i < unscrambledWords.size(); i++) {

                    String currentWord = unscrambledWords.get(i);

                    if (i + 1 < unscrambledWords.size() && currentWord.length() != unscrambledWords.get(i + 1).length()) {
                        if (!headers.contains(String.valueOf(currentWord.length())))
                            headers.add(String.valueOf(currentWord.length()));
                    } else if (i + 1 == unscrambledWords.size()) {
                        if (!headers.contains(String.valueOf(currentWord.length())))
                            headers.add(String.valueOf(currentWord.length()));
                    }
                }
                break;
        }

        return headers;
    }

    public static HashMap<String, List<String>> generateChildrenHashMap(List<String> orginalHeaders, List<String> orginalUnscrambledWords, int sortingMethod) {

        HashMap<String, List<String>> childrenTitles = new HashMap<>();
        List<String> headers = orginalHeaders;
        List<String> unscrambledWords = orginalUnscrambledWords;

        switch (sortingMethod) {

            case ResultSortingMethod.REVERSE_ALPHABETICAL:
            case ResultSortingMethod.ALPHABETICAL:

                for (String header : headers) {
                    List<String> currentHeaderWords = new ArrayList<>();

                    for (String word : unscrambledWords) {


                        if (word.substring(0, 1).toUpperCase().equals(header.substring(0, 1))) {
                            currentHeaderWords.add(word);
                        }
                    }

                    unscrambledWords.removeAll(currentHeaderWords);
                    childrenTitles.put(header, currentHeaderWords);
                }

                break;
            default:

                for (String header : headers) {

                    List<String> currentHeaderWords = new ArrayList<>();

                    for (String word : unscrambledWords) {
                        if (word.length() == Integer.parseInt(header)) {
                            currentHeaderWords.add(word);
                        }else
                            break;
                    }

                    unscrambledWords.removeAll(currentHeaderWords);
                    childrenTitles.put(header, currentHeaderWords);
                }

                break;
        }

        return childrenTitles;
    }
}
