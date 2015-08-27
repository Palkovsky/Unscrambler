package andrzej.example.com.wordunscrambler.fragments.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import andrzej.example.com.wordunscrambler.R;
import andrzej.example.com.wordunscrambler.adapters.WordResultsAdapter;
import andrzej.example.com.wordunscrambler.config.ResultSortingMethod;
import andrzej.example.com.wordunscrambler.config.UnscrambleTabConfig;
import andrzej.example.com.wordunscrambler.interfaces.UnscrambleWordsAsyncListener;
import andrzej.example.com.wordunscrambler.models.Dictionary;
import andrzej.example.com.wordunscrambler.utils.Converter;
import andrzej.example.com.wordunscrambler.utils.DictionaryUtils;
import andrzej.example.com.wordunscrambler.utils.ResultListUtils;
import andrzej.example.com.wordunscrambler.utils.StringOpearations;
import andrzej.example.com.wordunscrambler.utils.ViewUtils;
import andrzej.example.com.wordunscrambler.utils.WordAlphabetComparator;
import andrzej.example.com.wordunscrambler.utils.WordAlphabetReverseComparator;
import andrzej.example.com.wordunscrambler.utils.WordLengthComparatorAsc;
import andrzej.example.com.wordunscrambler.utils.WordLengthComparatorDesc;
import andrzej.example.com.wordunscrambler.views.AnimatedExpandableListView;
import andrzej.example.com.wordunscrambler.views.ExpandableLayout;
import andrzej.example.com.wordunscrambler.views.MaterialEditText;


public class UnscrambleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UNSCRAMBLE_FRAGMENT_TAG";
    private int sortingMethod = ResultSortingMethod.ASCENDING;


    //UI elements declaration
    ExpandableLayout expandableFormLayout;
    private ImageButton expandBtn;
    private ImageButton unscrambleBtn;
    private AnimatedExpandableListView resultsExpandableListView;
    private MaterialEditText scrambledWordEditor;
    private MaterialEditText startsWithEditor;
    private MaterialEditText endsWithEditor;
    private MaterialEditText containsEditor;
    private MaterialEditText lengthEditor;
    private TextView noWordsFoundTextView;
    private Spinner spinnerSortingTypes;

    //Lists
    private List<String> resultWords = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    HashMap<String, List<String>> childItems  = new HashMap<>();

    //Adapter
    WordResultsAdapter mAdapter;

    public UnscrambleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unscramble, container, false);

        //UI elements construction
        expandableFormLayout = (ExpandableLayout) v.findViewById(R.id.formExpandableLayout);
        expandBtn = (ImageButton) v.findViewById(R.id.expandFormBtn);
        unscrambleBtn = (ImageButton) v.findViewById(R.id.unscrambleWordBtn);
        resultsExpandableListView = (AnimatedExpandableListView) v.findViewById(R.id.resultsExpandableListView);
        scrambledWordEditor = (MaterialEditText) v.findViewById(R.id.scrambledWordEditor);
        startsWithEditor = (MaterialEditText) v.findViewById(R.id.startsWithEditor);
        endsWithEditor = (MaterialEditText) v.findViewById(R.id.endsWithEditor);
        containsEditor = (MaterialEditText) v.findViewById(R.id.containsEditor);
        lengthEditor = (MaterialEditText) v.findViewById(R.id.lengthEditor);
        noWordsFoundTextView = (TextView) v.findViewById(R.id.noWordsFoundTextView);
        spinnerSortingTypes = (Spinner) v.findViewById(R.id.spinnerSortingTypes);

        //Listeners
        expandBtn.setOnClickListener(this);
        unscrambleBtn.setOnClickListener(this);

        //Bunch of szit
        scrambledWordEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnscrambleTabConfig.scrambledWordInputted = scrambledWordEditor.getText().toString();
            }
        });
        scrambledWordEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    unscramble();
                    return true;
                }
                return false;
            }
        });
        startsWithEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnscrambleTabConfig.startsWithInputted = startsWithEditor.getText().toString();
            }
        });
        endsWithEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnscrambleTabConfig.endsWithInputted = endsWithEditor.getText().toString();
            }
        });
        containsEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnscrambleTabConfig.containsInputted = containsEditor.getText().toString();
            }
        });
        lengthEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnscrambleTabConfig.lengthInputted = lengthEditor.getText().toString();
            }
        });
        spinnerSortingTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UnscrambleTabConfig.orderingPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //END OF BUNCZ OF SZIT

        //Adapter setup

        //View setup

        return v;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.expandFormBtn:
                if (expandableFormLayout.isOpened()) {
                    expandableFormLayout.hide();
                    UnscrambleTabConfig.formExpanded = false;
                } else {
                    expandableFormLayout.show();
                    UnscrambleTabConfig.formExpanded = true;
                }
                break;


            case R.id.unscrambleWordBtn:
                unscramble();
                break;

        }

    }

    /**
     * UTILS BEG
     */

    private void unscramble() {
        final String scrambledWord = scrambledWordEditor.getText().toString().trim();
        String startsWith = startsWithEditor.getText().toString().trim();
        String endsWith = endsWithEditor.getText().toString().trim();
        String contains = containsEditor.getText().toString().trim();
        sortingMethod = getSortingMethodFromSpinner();
        int length = -1;
        if (lengthEditor.getText().length() > 0)
            length = Integer.parseInt(lengthEditor.getText().toString().trim());


        if (isInputProper(scrambledWord, startsWith, endsWith, contains, length)) {

            //Load dictionary
            Dictionary dictionary = DictionaryUtils.getCurrentDictionary(getActivity());
            if (isProperDicitonary(dictionary)) {
                UnscrambleWordAsync unscrambleWordAsync = new UnscrambleWordAsync(scrambledWord, dictionary.getContentChunks());

                unscrambleWordAsync.registerResultListener(new UnscrambleWordsAsyncListener() {
                    @Override
                    public void onUnscrambled(List<String> unscrambledWords) {
                        hideKeyboard();

                        resultWords = unscrambledWords;

                        if (resultWords.size() > 0) {
                            headers = ResultListUtils.generateHeaders(resultWords, sortingMethod);
                            childItems  = ResultListUtils.generateChildrenHashMap(headers, unscrambledWords, sortingMethod);

                            mAdapter = new WordResultsAdapter(getActivity(), headers, childItems);
                            resultsExpandableListView.setAdapter(mAdapter);

                            UnscrambleTabConfig.headers = headers;
                            UnscrambleTabConfig.childItems = childItems;
                            UnscrambleTabConfig.noMatchingWords = false;

                            noWordsFoundTextView.setVisibility(View.GONE);
                            resultsExpandableListView.setVisibility(View.VISIBLE);
                        } else {
                            UnscrambleTabConfig.noMatchingWords = true;
                            UnscrambleTabConfig.noMatchingFor = scrambledWord;
                            noWordsFoundTextView.setVisibility(View.VISIBLE);
                            resultsExpandableListView.setVisibility(View.GONE);
                            noWordsFoundTextView.setText(getString(R.string.no_words_found_for) + " '" + scrambledWord + "'");
                        }
                    }
                });

                unscrambleWordAsync.execute();
            }

        }
    }

    private boolean isProperDicitonary(Dictionary dictionary) {
        if (dictionary == null) {
            Toast.makeText(getActivity(), R.string.no_dictionary_mounted, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isInputProper(String scrambledWord, String startsWith, String endsWith, String contains, int length) {

        if (scrambledWord.length() == 0) {
            Toast.makeText(getActivity(), R.string.enter_scrambled_word, Toast.LENGTH_SHORT).show();
            return false;
        }else if (scrambledWord.length() > 15) {
            Toast.makeText(getActivity(), R.string.scrambled_word_longer_than_limit, Toast.LENGTH_SHORT).show();
            return false;
        } else if (startsWith.length() > scrambledWord.length()) {
            Toast.makeText(getActivity(), R.string.starts_with_longer_than_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!startsWith.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, startsWith)) {
            Toast.makeText(getActivity(), R.string.starts_with_contains_chars_which_not_in_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (endsWith.length() > scrambledWord.length()) {
            Toast.makeText(getActivity(), R.string.ends_with_longer_than_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!endsWith.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, endsWith)) {
            Toast.makeText(getActivity(), R.string.ends_with_contains_chars_which_not_in_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (contains.length() > scrambledWord.length()) {
            Toast.makeText(getActivity(), R.string.contains_longer_than_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!contains.equals("") && !StringOpearations.doesStringContainCharacters(scrambledWord, contains)) {
            Toast.makeText(getActivity(), R.string.contains_contains_chars_which_not_in_scrambledWord, Toast.LENGTH_SHORT).show();
            return false;
        } else if (length == 0) {
            Toast.makeText(getActivity(), R.string.length_cannot_be_zero, Toast.LENGTH_SHORT).show();
            return false;
        } else if (length > scrambledWord.length()) {
            Toast.makeText(getActivity(), R.string.length_bigger_than_scrambledWord_length, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private int getSortingMethodFromSpinner() {
        int sorting = ResultSortingMethod.ASCENDING;

        switch (spinnerSortingTypes.getSelectedItemPosition()) {
            case 0: //Ascending
                sorting = ResultSortingMethod.ASCENDING;
                break;

            case 1:
                sorting = ResultSortingMethod.DESCENDING;
                break;

            case 2:
                sorting = ResultSortingMethod.ALPHABETICAL;
                break;

            case 3:
                sorting = ResultSortingMethod.REVERSE_ALPHABETICAL;
                break;
        }

        return sorting;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(scrambledWordEditor.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(startsWithEditor.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(endsWithEditor.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(containsEditor.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(lengthEditor.getWindowToken(), 0);
    }

    /**
     * UTILS END
     */

    @Override
    public void onResume() {
        super.onResume();

        if (UnscrambleTabConfig.formExpanded) {
            if (!expandableFormLayout.isOpened())
                expandableFormLayout.show();
        } else {
            if (expandableFormLayout.isOpened())
                expandableFormLayout.hide();
        }
        expandableFormLayout.clearAnimation();

        spinnerSortingTypes.setSelection(UnscrambleTabConfig.orderingPosition);
        scrambledWordEditor.setText(UnscrambleTabConfig.scrambledWordInputted);
        startsWithEditor.setText(UnscrambleTabConfig.startsWithInputted);
        endsWithEditor.setText(UnscrambleTabConfig.endsWithInputted);
        containsEditor.setText(UnscrambleTabConfig.containsInputted);
        lengthEditor.setText(UnscrambleTabConfig.lengthInputted);

        if(UnscrambleTabConfig.headers != null && UnscrambleTabConfig.headers.size()>0){
            mAdapter = new WordResultsAdapter(getActivity(), UnscrambleTabConfig.headers, UnscrambleTabConfig.childItems);
            resultsExpandableListView.setAdapter(mAdapter);

            noWordsFoundTextView.setVisibility(View.GONE);
            resultsExpandableListView.setVisibility(View.VISIBLE);
        }else if(UnscrambleTabConfig.noMatchingWords){
            noWordsFoundTextView.setVisibility(View.VISIBLE);
            resultsExpandableListView.setVisibility(View.GONE);
            noWordsFoundTextView.setText(getString(R.string.no_words_found_for) + " '" + UnscrambleTabConfig.noMatchingFor + "'");
        }

        hideKeyboard();
    }

    /**
     * ASYNC TASKS BEG
     */

    private class UnscrambleWordAsync extends AsyncTask<Void, Void, List<String>> {

        MaterialDialog progressDialog;
        List<String> unscrambledWords = new ArrayList<>();
        UnscrambleWordsAsyncListener unscrambleWordsAsyncListener;
        private String scrambledWord;
        private String[] chunkedDictionary;

        private String startsWith;
        private String endsWith;
        private String contains;
        private int length = -1;
        private int sorting;

        private boolean running = false;

        public UnscrambleWordAsync(String scrambledWord, String[] chunkedDictionary) {
            this.scrambledWord = scrambledWord;
            this.chunkedDictionary = chunkedDictionary;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startsWith = startsWithEditor.getText().toString().trim();
            endsWith = endsWithEditor.getText().toString().trim();
            contains = containsEditor.getText().toString().trim();
            sorting = getSortingMethodFromSpinner();
            if (lengthEditor.getText().length() > 0)
                length = Integer.parseInt(lengthEditor.getText().toString().trim());

            progressDialog =
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.progress_dialog)
                            .content(R.string.unscrambling_word)
                            .progress(true, 0)
                            .cancelable(false)
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            })
                            .show();

            running = true;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            while (running) {

                if (chunkedDictionary != null) {
                    unscrambledWords = StringOpearations.findScrambledWords(scrambledWord, chunkedDictionary);

                    /**
                     *  Add filters
                     */


                    if (!startsWith.equals(""))
                        unscrambledWords = StringOpearations.startsWithFiltering(unscrambledWords, startsWith);

                    if (!endsWith.equals(""))
                        unscrambledWords = StringOpearations.endsWithFiltering(unscrambledWords, endsWith);

                    if (!contains.equals(""))
                        unscrambledWords = StringOpearations.containsFiltering(unscrambledWords, contains);

                    if (length > 1)
                        unscrambledWords = StringOpearations.lengthFiltering(unscrambledWords, length);


                    switch (sorting) {
                        case ResultSortingMethod.ASCENDING:
                            Collections.sort(unscrambledWords, new WordLengthComparatorAsc());
                            break;
                        case ResultSortingMethod.DESCENDING:
                            Collections.sort(unscrambledWords, new WordLengthComparatorDesc());
                            break;
                        case ResultSortingMethod.ALPHABETICAL:
                            Collections.sort(unscrambledWords, new WordAlphabetComparator());
                            break;
                        case ResultSortingMethod.REVERSE_ALPHABETICAL:
                            Collections.sort(unscrambledWords, new WordAlphabetReverseComparator());
                            break;
                    }

                    return unscrambledWords;
                } else
                    running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> unscrambledWords) {
            super.onPostExecute(unscrambledWords);
            running = false;

            progressDialog.hide();

            if (unscrambleWordsAsyncListener != null)
                unscrambleWordsAsyncListener.onUnscrambled(unscrambledWords);
        }

        public void registerResultListener(UnscrambleWordsAsyncListener unscrambleWordsAsyncListener) {
            this.unscrambleWordsAsyncListener = unscrambleWordsAsyncListener;
        }
    }

    /**
     * ASYNC TASKS END
     */

}
