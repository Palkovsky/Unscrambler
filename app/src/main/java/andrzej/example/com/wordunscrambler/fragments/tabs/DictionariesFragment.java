package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.daimajia.swipe.SwipeLayout;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.activities.DictionaryActivity;
import andrzej.example.com.wordunscrambler.adapters.DictionaryListAdapter;
import andrzej.example.com.wordunscrambler.config.OrderingMethods;
import andrzej.example.com.wordunscrambler.config.SortingMethods;
import andrzej.example.com.wordunscrambler.config.TabsConfig;
import andrzej.example.com.wordunscrambler.interfaces.ItemActionsListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.Converter;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;
import andrzej.example.com.wordunscrambler.utils.NameComparator;
import andrzej.example.com.wordunscrambler.utils.NameComparatorDesc;
import andrzej.example.com.wordunscrambler.utils.WordCountComparator;
import andrzej.example.com.wordunscrambler.utils.WordCountDescComparator;


public class DictionariesFragment extends Fragment implements ItemActionsListener, View.OnClickListener {

    public static final String TAG = "DICTIONARIES_FRAGMENT_TAG";
    public static final String FILES_DIR = "files";


    private static int sortingMethod = SortingMethods.NO_SORTING;


    //UI Init
    LinearLayout noDictionariesLayout;
    CoordinatorLayout snackbarCoordinatorLayout;
    RelativeLayout mainContentWrapper;
    RelativeLayout currentDictionaryLayout;
    TextView currentName;
    TextView currentWordCount;
    TextView currentFirstWords;
    TextView dictionariesSizeTv;
    TextView dictionariesSortingMethodTv;
    BootstrapButton sortBtn;
    BootstrapButton removeCurrentDictBtn;
    BootstrapButton removeAllDictionaries;
    ParallaxListView dictionariesListView;
    SwipeLayout navigationBarSwipeLayout;

    //Adapter
    DictionaryListAdapter mAdapter;

    //Array
    List<Dictionary> dictionaries = new ArrayList<>();
    List<File> filesDirectory = new ArrayList<>();

    public DictionariesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init adapter
        mAdapter = new DictionaryListAdapter(getActivity(), dictionaries);
    }

    @Override
    public void onResume() {
        super.onResume();

        update();

        if (dictionaries.size() > 0)
            dictionariesListView.setSelection(TabsConfig.CURRENT_DICTIONARY_POSITION);
        else
            TabsConfig.CURRENT_DICTIONARY_POSITION = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dictionaries, container, false);

        //UI init
        noDictionariesLayout = (LinearLayout) v.findViewById(R.id.noDictionariesLayout);
        mainContentWrapper = (RelativeLayout) v.findViewById(R.id.mainContentWrapper);
        currentDictionaryLayout = (RelativeLayout) v.findViewById(R.id.currentDictionaryInfo);
        dictionariesListView = (ParallaxListView) v.findViewById(R.id.dictionaryListView);
        currentName = (TextView) v.findViewById(R.id.currentDictionaryTitle);
        currentWordCount = (TextView) v.findViewById(R.id.currentDictionaryWordCount);
        currentFirstWords = (TextView) v.findViewById(R.id.currentDictionaryFirstWords);
        dictionariesSizeTv = (TextView) v.findViewById(R.id.dictionariesCountTextView);
        dictionariesSortingMethodTv = (TextView) v.findViewById(R.id.dictionariesSortingMethodTextView);
        snackbarCoordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.snackbarCoordinatorLayout);
        sortBtn = (BootstrapButton) v.findViewById(R.id.sortBtn);
        removeCurrentDictBtn = (BootstrapButton) v.findViewById(R.id.removeCurrentDictBtn);
        removeAllDictionaries = (BootstrapButton) v.findViewById(R.id.deleteAllDictionariesBtn);
        navigationBarSwipeLayout = (SwipeLayout) v.findViewById(R.id.navigationBarSwipeLayout);

        //Init
        dictionariesListView.setAdapter(mAdapter);
        navigationBarSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        navigationBarSwipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, v.findViewById(R.id.navigationSwipeBottomLayout));

        //Listeners
        mAdapter.registerItemActionsListener(this);
        currentDictionaryLayout.setOnClickListener(this);
        sortBtn.setOnClickListener(this);
        removeCurrentDictBtn.setOnClickListener(this);
        removeAllDictionaries.setOnClickListener(this);

        //Logic init
        update();

        return v;
    }


    private void update() {

        // Get local files
        File directory = new File(getActivity().getApplicationInfo().dataDir + "/" + FILES_DIR);

        dictionaries.clear();
        filesDirectory.clear();

        Dictionary currentDictionary = DictionaryUtils.getCurrentDictionary(getActivity());
        setDictionary(currentDictionary);


        if (currentDictionary == null) {
            removeCurrentDictBtn.setVisibility(View.GONE);
            currentDictionaryLayout.setClickable(false);
        } else {
            removeCurrentDictBtn.setVisibility(View.VISIBLE);
            currentDictionaryLayout.setClickable(true);

            if (!currentDictionary.getFile().exists())
                setDictionary(null);
        }

        if (directory.listFiles() != null && directory.listFiles().length > 0) {
            noDictionariesLayout.setVisibility(View.GONE);
            mainContentWrapper.setVisibility(View.VISIBLE);
            File[] listOfLocalFilesArray = directory.listFiles();

            // get the names of files
            for (File file : listOfLocalFilesArray) {
                filesDirectory.add(file);
                dictionaries.add(new Dictionary(file.getName(), file));
            }
        } else {
            mainContentWrapper.setVisibility(View.GONE);
            noDictionariesLayout.setVisibility(View.VISIBLE);
        }

        dictionariesSizeTv.setText(getResources().getString(R.string.dictionaries_colon) + " " + dictionaries.size());

        if (dictionaries.size() > 0) {
            switch (sortingMethod) {

                case SortingMethods.NO_SORTING:
                    dictionariesSortingMethodTv.setText(R.string.no_sorting);
                    break;

                case SortingMethods.SORTING_BY_WORDS_COUNT:
                    Collections.sort(dictionaries, new WordCountComparator());
                    dictionariesSortingMethodTv.setText(R.string.wordsAscCount);
                    break;

                case SortingMethods.SORTING_BY_WORDS_COUNT_DESC:
                    Collections.sort(dictionaries, new WordCountDescComparator());
                    dictionariesSortingMethodTv.setText(R.string.wordsDescCount);
                    break;

                case SortingMethods.SORTING_BY_NAME:
                    Collections.sort(dictionaries, new NameComparator());
                    dictionariesSortingMethodTv.setText(R.string.fromAtoZsorting);
                    break;

                case SortingMethods.SORTING_BY_NAME_DESC:
                    Collections.sort(dictionaries, new NameComparatorDesc());
                    dictionariesSortingMethodTv.setText(R.string.fromZtoAsorting);
                    break;
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void setDictionary(Dictionary dictionary) {
        if (dictionary == null) {
            currentName.setText("---");
            currentWordCount.setText("0");
            currentFirstWords.setText("...");
            removeCurrentDictBtn.setVisibility(View.GONE);
            removeCurrentDictBtn.setVisibility(View.GONE);
            currentDictionaryLayout.setClickable(false);
            DictionaryUtils.setDictionaryPreference(getActivity(), null);
        } else {
            int wordsCount = dictionary.getWordCount();

            currentName.setText(dictionary.getName());
            currentWordCount.setText(String.valueOf(wordsCount));

            if (wordsCount == 0)
                currentFirstWords.setText("...");
            else
                currentFirstWords.setText(dictionary.getFirstNWordsInString(DictionaryListAdapter.FIRST_WORDS_TO_LOAD));

            removeCurrentDictBtn.setVisibility(View.VISIBLE);
            removeCurrentDictBtn.setVisibility(View.VISIBLE);
            currentDictionaryLayout.setClickable(true);
            DictionaryUtils.setDictionaryPreference(getActivity(), dictionary);
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        TabsConfig.CURRENT_DICTIONARY_POSITION = position;
        transferToDictionaryActivity(dictionaries.get(position));
    }

    @Override
    public void onLongItemClick(View v, int position) {
        //Don't have idea to put here
    }

    @Override
    public void setItemAction(int position) {
        setDictionary(dictionaries.get(position));
        mAdapter.notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(
                snackbarCoordinatorLayout,
                getResources().getString(R.string.current_dictionary_colon) + " " + dictionaries.get(position).getName(),
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void deleteItemAction(int position) {
        Dictionary dictionaryToRemove = dictionaries.get(position);
        if (DictionaryUtils.isCurrentDictionary(getActivity(), dictionaryToRemove))
            setDictionary(null);
        dictionaryToRemove.removeFile();
        dictionaries.remove(position);
        update();
    }

    private void transferToDictionaryActivity(Dictionary dictionary) {
        Intent dictionaryIntent = new Intent(getActivity(), DictionaryActivity.class);
        dictionaryIntent.putExtra(DictionaryActivity.DICTIONARY_PATH_BUNDLE_KEY, dictionary.getFile().toString());
        startActivity(dictionaryIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentDictionaryInfo:
                Dictionary currentDictionary = DictionaryUtils.getCurrentDictionary(getActivity());

                if (currentDictionary != null)
                    transferToDictionaryActivity(currentDictionary);
                break;

            case R.id.removeCurrentDictBtn:
                setDictionary(null);
                removeCurrentDictBtn.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.deleteAllDictionariesBtn:

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.delete)
                        .content(R.string.are_you_sure_to_delete_all)
                        .positiveText(R.string.yes)
                        .negativeText(R.string.no)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                for (Dictionary dictionary : dictionaries) {
                                    dictionary.getFile().delete();
                                }
                                setDictionary(null);
                                update();
                            }
                        })
                        .show();

                break;

            case R.id.sortBtn:

                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int finalCurrentSorting = sortingMethod;


                        switch (item.getItemId()) {
                            case R.id.item_noSort:
                                sortingMethod = SortingMethods.NO_SORTING;
                                break;

                            case R.id.item_AZ:
                                sortingMethod = SortingMethods.SORTING_BY_NAME;
                                break;

                            case R.id.item_ZA:
                                sortingMethod = SortingMethods.SORTING_BY_NAME_DESC;
                                break;

                            case R.id.item_wordCountAsc:
                                sortingMethod = SortingMethods.SORTING_BY_WORDS_COUNT;
                                break;

                            case R.id.item_wordCountDesc:
                                sortingMethod = SortingMethods.SORTING_BY_WORDS_COUNT_DESC;
                                break;
                        }

                        if (finalCurrentSorting != sortingMethod) {
                            update();
                            dictionariesListView.setSelection(0);
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.sort_popup);
                popupMenu.show();


                /*
                final int finalCurrentSorting = sortingMethod;

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.sorting)
                        .items(R.array.sortingDialogItems)
                        .itemsCallbackSingleChoice(sortingMethod, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        sortingMethod = SortingMethods.NO_SORTING;
                                        break;

                                    case 1:
                                        sortingMethod = SortingMethods.SORTING_BY_NAME;
                                        break;

                                    case 2:
                                        sortingMethod = SortingMethods.SORTING_BY_NAME_DESC;
                                        break;

                                    case 3:
                                        sortingMethod = SortingMethods.SORTING_BY_WORDS_COUNT;
                                        break;

                                    case 4:
                                        sortingMethod = SortingMethods.SORTING_BY_WORDS_COUNT_DESC;
                                        break;
                                }

                                if (finalCurrentSorting != sortingMethod) {
                                    update();
                                    dictionariesListView.setSelection(0);
                                }
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();

                        */
                break;
        }
    }

}
