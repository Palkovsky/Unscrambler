package andrzej.example.com.wordunscrambler.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.fragments.tabs.DictionariesFragment;
import andrzej.example.com.wordunscrambler.fragments.tabs.UnscrambleFragment;

/**
 * Created by andrzej on 20.08.15.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[] = {"Unscrambler", "Dictionaries"}; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int numOfTabs = 2;
    public static final int[] imageResId = {R.drawable.ic_puzzle_white_24dp,
            R.drawable.ic_library_books_white_24dp};


    private Context context;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            UnscrambleFragment unscrambleFragment = new UnscrambleFragment();
            return unscrambleFragment;
        } else {
            DictionariesFragment dictionariesFragment = new DictionariesFragment();
            return dictionariesFragment;
        }


    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(getContext(), imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    private Context getContext() {
        return context;
    }
}