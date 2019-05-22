package chr.chat.components.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Line {

    private int ID;
    private String userID;
    private String sex;
    private String language;
    private String userSex;
    private String userName;

    public Line() {}

    public Line(String userID, String userName, String userSex, String sex, String language) {
        this.userID = userID;
        this.userSex = userSex;
        this.sex = sex;
        this.language = language;
        this.userName = userName;
    }

    public Line(int ID, String userName, String userSex, String userID, String sex, String language) {
        this.ID = ID;
        this.userID = userID;
        this.userSex = userSex;
        this.sex = sex;
        this.language = language;
        this.userName = userName;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Exclude
    public int getID() {
        return ID;
    }

    public String getUserID() {
        return userID;
    }

    public String getSex() {
        return sex;
    }

    public String getLanguage() {
        return language;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return userID + "(" + userSex + "), looking for " + sex + " and " + language;
    }
}
