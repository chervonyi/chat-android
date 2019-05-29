package chr.chat.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class AdultContentExclusions {

    private static final String KEY = "exclusions_Hag2F7q";

    public static void append(Context context, String chatID) {

        Set<String> exclusions = getExclusions(context);
        exclusions.add(chatID);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(KEY, exclusions)
                .apply();
    }


    private static Set<String> getExclusions(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(KEY, new HashSet<String>());
    }

    public static boolean isContains(Context context, String chatID) {
        return getExclusions(context).contains(chatID);
    }
}
