package chr.chat.components.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Chat {

    private String ID;
    private String userID1;
    private String userID2;
    private String userName1;
    private String userName2;
    private boolean open;
    private boolean notified;

    public Chat() {}

    public Chat(String userID1, String userName1, String userID2, String userName2) {
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.userName1 = userName1;
        this.userName2 = userName2;
        this.open = true;
        this.notified = false;
    }

    public Chat(String userID1, String userName1, String userID2, String userName2, boolean open, boolean notified) {
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.userName1 = userName1;
        this.userName2 = userName2;
        this.open = open;
        this.notified = notified;
    }

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID1() {
        return userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public String getUserName1() {
        return userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isNotified() {
        return notified;
    }

    @Override
    public String toString() {
        return "ID: " + ID + "\n" +
                "userID1: " + userID1 + "\n" +
                "userID2: " + userID2 + "\n" +
                "userName1: " + userName1 + "\n" +
                "userName2: " + userName2 + "\n" +
                "isOpen: " + open + "\n" +
                "isNotified: " + notified + "\n";
    }
}
