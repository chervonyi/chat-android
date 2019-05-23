package chr.chat.components.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Report {

    private String suspect;
    private String chatID;

    public Report(String suspect, String chatID) {
        this.suspect = suspect;
        this.chatID = chatID;
    }

    public String getSuspect() {
        return suspect;
    }

    public String getChatID() {
        return chatID;
    }
}
