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

    public Line() {}

    public Line(String userID, String userSex, String sex, String language) {
        this.userID = userID;
        this.userSex = userSex;
        this.sex = sex;
        this.language = language;
    }

    public Line(int ID, String userSex, String userID, String sex, String language) {
        this.ID = ID;
        this.userID = userID;
        this.userSex = userSex;
        this.sex = sex;
        this.language = language;
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

    @Override
    public String toString() {
        return userID + "(" + userSex + "), looking for " + sex + " and " + language;
    }
}
