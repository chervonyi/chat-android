package chr.chat.components;

import android.content.Context;
import android.preference.PreferenceManager;

import chr.chat.components.models.Setting;

public class GlobalSettings {

    private static final Setting[] SETTINGS = new Setting[] {
            new Setting("new_chat_26dh21", true),
            new Setting("blur_images_Hs25A", true),
            new Setting("check_adult_content_Ag2aGF", true),
            new Setting("dark_mode_6GaVgC5", false),
            new Setting("bot_messages_O2PaVc", true),
    };

    public static int NOTIFICATION_NEW_CHAT = 0;
    public static int BLUR_IMAGES = 1;
    public static int CHECK_ON_ADULT_CONTENT = 2;
    public static int DARK_MODE = 3;
    public static int BOT_MESSAGES = 4;

    public static boolean isChecked(Context context, int switcher) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SETTINGS[switcher].CODE, SETTINGS[switcher].DEFAULT_VALUE);
    }

    public static void setCheck(Context context, int switcher, boolean newValue) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(SETTINGS[switcher].CODE, newValue)
                .apply();
    }


}
