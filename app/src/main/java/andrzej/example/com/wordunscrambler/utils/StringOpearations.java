package andrzej.example.com.wordunscrambler.utils;

/**
 * Created by andrzej on 26.08.15.
 */
public class StringOpearations {
    public static boolean doesStringContainCharacters(String baseString, String comparableString){
        char[] chunkedString = comparableString.toCharArray();

        for(char chunk : chunkedString){
            if(!baseString.contains(String.valueOf(chunk))){
                return false;
            }
        }

        return true;
    }
}
