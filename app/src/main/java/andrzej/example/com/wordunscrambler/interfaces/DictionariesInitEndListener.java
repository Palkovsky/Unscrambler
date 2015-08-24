package andrzej.example.com.wordunscrambler.interfaces;

import java.util.List;

import andrzej.example.com.wordunscrambler.models.Dictionary;

/**
 * Created by andrzej on 24.08.15.
 */
public interface DictionariesInitEndListener {
    void onInitialize(List<Dictionary> dictionaryList);
}
