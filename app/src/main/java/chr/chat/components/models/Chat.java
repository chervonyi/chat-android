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

    public Chat() {}

    public Chat(String ID, String userID1, String userName1, String userID2, String userName2) {
        this.ID = ID;
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.userName1 = userName1;
        this.userName2 = userName2;
    }

    public Chat(String userID1, String userName1, String userID2, String userName2) {
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.userName1 = userName1;
        this.userName2 = userName2;
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

    @Override
    public String toString() {
        return ID + " - " + userID1 + " - " + userID2;
    }
}
