package chr.chat.components;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.UUID;

public class UniqueIdentifier {

    private static final String IDENTIFIER_KEY = "idf_key_2019";
    private static final String NOT_FOUND_VALUE = "idf_not_found_key_2019";

    public static String identifier;

    /**
     * Only one public method in this class.
     * Uses to check if unique identifier was created and saved before.
     * @param context context to use SharedPreferences
     * @return <br>
     * If <b>true</b> then go on and program can easy use static variable.<br>
     * if <b>false</b> then program should register a new user
     * in database using the static variable.
     */
    public static boolean isCreatedBefore(Context context) {

        String readIdf = readIdentifier(context);

        if (readIdf.equals(NOT_FOUND_VALUE)) {

            // Generate a new identifier
            String generatedIdentifier = getRandomIdentifier();

            // Assign to the static variable
            identifier = generatedIdentifier;

            // Save it to the SharedPreferences
            saveIdentifier(context, generatedIdentifier);

            return false;
        }

        // Assign to the static variable
        identifier = readIdf;
        return true;
    }

    /**
     * Generate a random unique identifier using UUID.
     * @return string of identifier
     */
    private static String getRandomIdentifier() {
        return UUID.randomUUID().toString();
    }

    /**
     * Read saved unique identifier from phone memory
     * @param context context to use SharedPreferences
     * @return string of identifier
     */
    public static String readIdentifier(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(IDENTIFIER_KEY, NOT_FOUND_VALUE);
    }

    /**
     * Save created identifier into phone memory.
     * @param context context to use SharedPreferences
     * @param identifier string of unique identifier
     */
    private static void saveIdentifier(Context context, String identifier) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(IDENTIFIER_KEY, identifier)
                .apply();
    }
}
