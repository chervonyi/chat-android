package chr.chat.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class AdultContentExclusions {

    private static final String KEY = "exclusions_Hag2F7q";

    /**
     * Save id of some chat as an exclusions of adult content checking
     * @param context context
     * @param chatID required id
     */
    public static void append(Context context, String chatID) {

        Set<String> exclusions = getExclusions(context);
        exclusions.add(chatID);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(KEY, exclusions)
                .apply();
    }


    /**
     * Returns list of exclusions IDs
     * @param context context
     * @return list of chat IDs
     */
    private static Set<String> getExclusions(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(KEY, new HashSet<String>());
    }

    /**
     * Check if given ID is contained in phone memory (as an exclusion)
     * @param context context
     * @param chatID interested chat id
     * @return true if it contains. False if it does not contain
     */
    public static boolean isContain(Context context, String chatID) {
        return getExclusions(context).contains(chatID);
    }
}
