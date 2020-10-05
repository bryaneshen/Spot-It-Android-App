package ca.cmpt276.finddamatch.photogalleryactivity;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * QueryPreferences is the persistence engine for PhotoGallery. Stores and sets user's most recent
 * query
 */
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

}