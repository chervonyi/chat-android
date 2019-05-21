package chr.chat.components.models;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Map;

public class Message {

    private String ID;
    private String chatID;
    private String message;
    private String owner;
    private Object datetime;

    public Message() {}


    public Message(String chatID, String message, String owner) {
        this.chatID = chatID;
        this.message = message;
        this.owner = owner;
        this.datetime = ServerValue.TIMESTAMP;
    }

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getMessage() {
        return message;
    }

    public String getOwner() {
        return owner;
    }

    public Object getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return "chatID: " + chatID + ", " +
                "message: " + message + ", " +
                "owner: " + owner + ", " +
                "timestamp: " + datetime.toString() + "\n\n";
    }

    @Exclude
    public void getDatetimeString() {
        Log.d("CHR_GAMES_TEST", "datetime: " + datetime.toString());
    }
}
