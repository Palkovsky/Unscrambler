package andrzej.example.com.wordunscrambler.interfaces;

import java.util.List;

/**
 * Created by andrzej on 27.08.15.
 */
public interface UnscrambleWordsAsyncListener {
    void onUnscrambled(List<String> unscrambledWords);
}
