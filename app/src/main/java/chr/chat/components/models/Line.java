package chr.chat.components.models;

import com.google.firebase.database.Exclude;

public class Line {

    private int ID;
    public String userID;
    public String sex;
    public String language;

    public Line() {}

    public Line(String userID, String sex, String language) {
        this.userID = userID;
        this.sex = sex;
        this.language = language;
    }

    public Line(int ID, String userID, String sex, String language) {
        this.ID = ID;
        this.userID = userID;
        this.sex = sex;
        this.language = language;
    }

    @Exclude
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return userID + " looking for " + sex + " and " + language;
    }
}
