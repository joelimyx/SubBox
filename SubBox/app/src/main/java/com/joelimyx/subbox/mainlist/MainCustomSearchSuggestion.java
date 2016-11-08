package com.joelimyx.subbox.mainlist;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Joe on 11/8/16.
 */

public class MainCustomSearchSuggestion extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = MainCustomSearchSuggestion.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public MainCustomSearchSuggestion() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
