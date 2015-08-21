package andrzej.example.com.wordunscrambler.interfaces;

import android.view.View;

/**
 * Created by andrzej on 21.08.15.
 */
public interface ItemCheckedListener {
    void onItemCheckStateChange(int position, View v, boolean isChecked);
    void onItemClick(int position, View v);
    void onLongItemClick(int position, View v);
}
