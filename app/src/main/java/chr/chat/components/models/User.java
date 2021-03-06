package chr.chat.components.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {


    private String ID;
    private String name;
    private String sex;
    private boolean available;

    public User() {}

    public User(String ID, String name, String sex) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.available = true;
    }

    public User(String ID, String name, String sex, boolean available) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.available = available;
    }

    @Exclude
    public String getID() {
        return this.ID;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return this.ID + " " +
                this.name + " " +
                this.sex + " " +
                this.available;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
