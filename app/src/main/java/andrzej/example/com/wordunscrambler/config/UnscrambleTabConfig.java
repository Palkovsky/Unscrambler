package andrzej.example.com.wordunscrambler.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrzej on 27.08.15.
 */
public class UnscrambleTabConfig {
    //Bunch of vars that help to keep views in same state
    public static boolean formExpanded = false;
    public static String scrambledWordInputted = "";
    public static String startsWithInputted = "";
    public static String endsWithInputted = "";
    public static String containsInputted = "";
    public static String lengthInputted = "";
    public static String currentDictionaryName = "";
    public static int orderingPosition = 0;
    public static int foundWordsCount = 0;
    public static String noMatchingFor = "";
    public static boolean noMatchingWords = false;
    public static List<String> headers = new ArrayList<>();
    public static HashMap<String, List<String>> childItems  = new HashMap<>();
}
