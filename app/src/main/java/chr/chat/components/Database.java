package chr.chat.components;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import chr.chat.components.models.Line;
import chr.chat.components.models.User;

public class Database {

    private DatabaseReference mDatabase;

    // Tables
    private final String USERS = "users";
    private final String LINE = "line";
    private final String CHATS = "chats";
    private final String MESSAGES = "messages";

    public Database() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void registerNewUser(User user) {
        mDatabase.child("users").child(user.getID()).setValue(user);
    }

    public void registerNewUser(String ID, String name, String sex) {
        User user = new User(ID, name, sex);
        registerNewUser(user);
    }

    public void updateName(String ID, String newName) {
        mDatabase.child(USERS).child(ID).child("name").setValue(newName);
    }

    private void lookForMatches(User userWhichSearching, ArrayList<Line> lineNodes, String sex, String language) {


        boolean foundSmd = false;

        for (Line line : lineNodes) {
            if (language.equals(line.getLanguage())) {

                if (line.getSex().equals(userWhichSearching.getSex()) || line.getSex().equals("any")) {
                    if (sex.equals(line.getUserSex()) || sex.equals("any")) {
                        foundSmd = true;
                        /// TODO - remove user 'line.userID' from line using:
                        //        removeUserFromLine(line.userID);
                        // TODO - call some (static) method to create chat with current user id and line.userID'
                    }
                }
            }
        }

        if (!foundSmd) {
            // TODO - Add 'userWhichSearching' with 'sex' and 'language' to Line table
        }
    }

    public void searchSomebodyFor(final User user, final String sex, final String language) {

        mDatabase.child(LINE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Line> arrayList = new ArrayList<>();

                for (DataSnapshot lineNode : dataSnapshot.getChildren()) {
                    arrayList.add(lineNode.getValue(Line.class));
                }

                lookForMatches(user, arrayList, sex, language);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void removeUserFromLine(String ID) {
        mDatabase.child(LINE).orderByChild("userID").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot foundUserRef : dataSnapshot.getChildren()) {
                    // Remove line node where userID equals to given 'ID'
                    foundUserRef.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void putUserInLine(Line lineNode) {
        DatabaseReference lineRef = mDatabase.child(LINE);
        lineRef.push().setValue(lineNode);
    }

    public void loadUserData(String ID) {

        final String currentID = ID;

        ValueEventListener loadUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                // TODO - check on 'null'
                // TODO - call some static variable and assign 'user'
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        mDatabase.child("users").child(ID).addValueEventListener(loadUserListener);
    }


}
