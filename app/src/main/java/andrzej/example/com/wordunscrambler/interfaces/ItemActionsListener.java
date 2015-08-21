package andrzej.example.com.wordunscrambler.interfaces;

import android.view.View;

/**
 * Created by andrzej on 21.08.15.
 */
public interface ItemActionsListener {
    void onItemClick(View v, int position);
    void onLongItemClick(View v, int position);
    void deleteItemAction(int position);
}
