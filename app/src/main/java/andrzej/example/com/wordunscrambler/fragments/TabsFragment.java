package andrzej.example.com.wordunscrambler.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.activities.DictionaryActivity;
import andrzej.example.com.wordunscrambler.activities.MainActivity;
import andrzej.example.com.wordunscrambler.adapters.TabsAdapter;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.views.SlidingTabLayout;


public class TabsFragment extends BackHandledFragment implements ViewPager.OnPageChangeListener {

    public static final String TAG = "TABS_FRAGMENT_TAG";

    ViewPager pager;
    TabsAdapter adapter;
    SlidingTabLayout tabs;

    public TabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_tabs, container, false);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new TabsAdapter(getActivity(), getActivity().getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.tabsViewPager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.tabsLayout);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setCustomTabView(R.layout.custom_tab, 0);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        pager.setCurrentItem(TabsConfig.CURRENT_TAB_NUM);
        TabsConfig.CURRENT_TAB_NUM = pager.getCurrentItem();
    }

    @Override
    public boolean onBackPressed() {
        if (pager.getCurrentItem() == 1) {
            pager.setCurrentItem(0);
            TabsConfig.CURRENT_TAB_NUM = pager.getCurrentItem();
        } else
            getActivity().moveTaskToBack(true);

        return true;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabsConfig.CURRENT_TAB_NUM = position;

        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        switch (position) {
            //Unscramble fragment
            case 0:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.unscrambler);
                break;

            //Dictionaries fragment
            case 1:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.dictionaries);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.tabs_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_createDictionary:
                Intent dictionaryIntent = new Intent(getActivity(), DictionaryActivity.class);
                startActivity(dictionaryIntent);
                break;

            case R.id.menu_browseDicts:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BrowseFragment(), BrowseFragment.TAG)
                        .commit();
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
