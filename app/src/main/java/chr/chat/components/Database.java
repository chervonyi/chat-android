package chr.chat.components;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import chr.chat.components.models.Chat;
import chr.chat.components.models.Line;
import chr.chat.components.models.Message;
import chr.chat.components.models.User;

public class Database {

    private DatabaseReference mDatabase;

    // Tables
    private final String USERS = "users";
    private final String LINE = "line";
    private final String CHATS = "chats";
    private final String MESSAGES = "messages";
    private final String REPORT = "report";

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

        for (Line line : lineNodes) {
            if (language.equals(line.getLanguage())) {

                if (line.getSex().equals(userWhichSearching.getSex()) || line.getSex().equals("any")) {
                    if (sex.equals(line.getUserSex()) || sex.equals("any")) {

                        // Remove current user from line
                        removeUserFromLine(line.getUserID());

                        // TODO - call some (static) method to start chat with:
                        //       userWhichSearching.getID() and
                        //       line.getUserID()

                        // Create chat with userWhichSearching and currentUser('line' instance)

                        return;
                    }
                }
            }
        }

        // Add userWhichSearching to Line
        Line line = new Line(userWhichSearching.getID(), userWhichSearching.getSex(), sex, language);
        putUserInLine(line);

    }

    public void appendNewMessage(Message messageNode) {
        DatabaseReference lineRef = mDatabase.child(MESSAGES);
        lineRef.push().setValue(messageNode);
    }

    public void createChat(Chat chatNode) {
        DatabaseReference lineRef = mDatabase.child(CHATS);
        lineRef.push().setValue(chatNode);
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

    public void loadListOfMessages(String chatID) {
        mDatabase.child(MESSAGES).orderByChild("chatID").equalTo(chatID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                // TODO - Call some (static) method to set this list to update view of chat-view (MainActivity)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void loadAllChatsFor(final String userID) {
        mDatabase.child(CHATS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Chat> chats = new ArrayList<>();
                ArrayList<String> requiredChatList = new ArrayList<>();

                Chat chat;
                for (DataSnapshot chatNode: dataSnapshot.getChildren()) {
                    chat = chatNode.getValue(Chat.class);
                    chat.setID(chatNode.getKey());

                    chats.add(chat);
                }

                for (Chat foundChat: chats) {
                    if (foundChat.getUserID1().equals(userID) ||
                        foundChat.getUserID2().equals(userID)) {
                        requiredChatList.add(foundChat.getID());
                    }
                }

                // TODO - assign chat-list ('requiredChatList') with current user ('userID')
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });
    }

    public void reportMessage(String messageID) {
        DatabaseReference lineRef = mDatabase.child(REPORT);
        lineRef.push().setValue(messageID);
        //mDatabase.child("report").setValue(messageID);
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
