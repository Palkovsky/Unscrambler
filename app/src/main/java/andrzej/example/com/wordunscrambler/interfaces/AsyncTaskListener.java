package andrzej.example.com.wordunscrambler.interfaces;

import java.io.File;
import java.util.List;

/**
 * Created by andrzej on 21.08.15.
 */
public interface AsyncTaskListener {
    void onPostExecute(List<File> files);
}
