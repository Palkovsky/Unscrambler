package andrzej.example.com.wordunscrambler.utils;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andrzej on 26.08.15.
 */
public class StringOpearations {
    public static boolean doesStringContainCharacters(String baseString, String comparableString) {
        char[] chunkedString = comparableString.toCharArray();

        for (char chunk : chunkedString) {
            if (!baseString.contains(String.valueOf(chunk))) {
                return false;
            }
        }

        return true;
    }

    public static List<String> findScrambledWords(String scrambledWord, String[] chunkedDictionary) {
        List<String> wordsList = new ArrayList<>();

        int maxLength = scrambledWord.length();
        String[] scrambledCharacters = scrambledWord.trim().split("(?!^)");

        for (String dictionaryWord : chunkedDictionary) {

            List<String> scrambledCharactersCopy = new LinkedList<String>(Arrays.asList(scrambledCharacters));

            if (dictionaryWord.length() > 0 && dictionaryWord.length() <= maxLength) {

                String[] dictionaryWordCharacters = dictionaryWord.trim().split("(?!^)");

                for (int i = 0; i < dictionaryWordCharacters.length; i++) {
                    if (scrambledCharactersCopy.contains(dictionaryWordCharacters[i])) {


                        scrambledCharactersCopy.remove(dictionaryWordCharacters[i]);
                        if (i == dictionaryWordCharacters.length - 1) {
                            if (!wordsList.contains(dictionaryWord))
                                wordsList.add(dictionaryWord);
                            break;
                        }


                    } else
                        break;
                }
            }
        }
        return wordsList;
    }

    public static List<String> startsWithFiltering(List<String> wordList, String filter) {

        List<String> filteredWords = new LinkedList<>();

        for (String word : wordList) {
            if (word.startsWith(filter.trim()))
                filteredWords.add(word);
        }

        return filteredWords;
    }

    public static List<String> endsWithFiltering(List<String> wordList, String filter) {

        List<String> filteredWords = new LinkedList<>();

        for (String word : wordList) {
            if (word.endsWith(filter.trim()))
                filteredWords.add(word);
        }

        return filteredWords;
    }

    public static List<String> containsFiltering(List<String> wordList, String filter) {

        List<String> filteredWords = new LinkedList<>();

        for (String word : wordList) {
            if (word.contains(filter.trim()))
                filteredWords.add(word);
        }

        return filteredWords;
    }

    public static List<String> lengthFiltering(List<String> wordList, int length) {

        List<String> filteredWords = new LinkedList<>();

        for (String word : wordList) {
            if (word.length() == length)
                filteredWords.add(word);
        }

        return filteredWords;
    }
}
