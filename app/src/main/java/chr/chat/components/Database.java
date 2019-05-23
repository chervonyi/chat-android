package chr.chat.components;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import chr.chat.activities.MainActivity;
import chr.chat.activities.SearchActivity;
import chr.chat.components.models.Chat;
import chr.chat.components.models.Line;
import chr.chat.components.models.Message;
import chr.chat.components.models.Report;
import chr.chat.components.models.User;

public class Database {

    public static final Database instance = new Database();

    private DatabaseReference mDatabase;

    // Table names
    private final String USERS = "users";
    private final String LINE = "line";
    private final String CHATS = "chats";
    private final String MESSAGES = "messages";
    private final String REPORT = "report";

    private Database() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    // -------------------------
    // ------  MESSAGES  -------
    // -------------------------

    public void appendNewMessage(Message messageNode) {
        DatabaseReference lineRef = mDatabase.child(MESSAGES);
        lineRef.push().setValue(messageNode);
    }

    public void assignListenerOnMessagesForChat(final String chatID) {
        mDatabase.child(MESSAGES).orderByChild("chatID").equalTo(chatID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                MainActivity.setMessages(chatID, messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    // -------------------------
    // ------    CHATS   -------
    // -------------------------

    public void createChat(Chat chatNode) {
        DatabaseReference lineRef = mDatabase.child(CHATS).push();

        String pushedUniqueID = lineRef.getKey();

        lineRef.setValue(chatNode);

        assignListenerOnMessagesForChat(pushedUniqueID);
    }

    public void loadAllChats(final String userID) {
        mDatabase.child(CHATS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Chat> chats = new ArrayList<>();
                ArrayList<Chat> requiredChatList = new ArrayList<>();

                Chat chat;
                for (DataSnapshot chatNode: dataSnapshot.getChildren()) {
                    chat = chatNode.getValue(Chat.class);
                    chat.setID(chatNode.getKey());

                    chats.add(chat);
                }

                for (Chat foundChat: chats) {
                    if (foundChat.getUserID1().equals(userID) ||
                        foundChat.getUserID2().equals(userID)) {
                        requiredChatList.add(foundChat);
                        assignListenerOnMessagesForChat(foundChat.getID());
                        Log.d("CHR_GAMES_TEST", "On app start - Assign listeners for all chats: " + foundChat.getID());
                    }
                }

                MainActivity.setChatList(requiredChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });
    }


    // -------------------------
    // ------    LINE    -------
    // -------------------------

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

    private void lookForMatches(User userWhichSearching, ArrayList<Line> lineNodes, String sex, String language) {

        for (Line line : lineNodes) {
            if (language.equals(line.getLanguage())) {

                if (line.getSex().equals(userWhichSearching.getSex()) || line.getSex().equals("any")) {
                    if (sex.equals(line.getUserSex()) || sex.equals("any")) {

                        // TODO - Continue from here - fix problem: why it crashes and why it does not remove line instance calling 'removeUserFromLine(line.getUserID());'
                        // Remove current user from line
                        removeUserFromLine(line.getUserID());

                        Chat currentChat = new Chat(userWhichSearching.getID(), userWhichSearching.getName(), line.getUserID(), line.getUserName());

                        // Create chat in database
                        Database.instance.createChat(currentChat);

                        SearchActivity.goToChat();

                        return;
                    }
                }
            }
        }

        // Add userWhichSearching to Line
        Line line = new Line(userWhichSearching.getID(), userWhichSearching.getName(), userWhichSearching.getSex(), sex, language);
        putUserInLine(line);
        // TODO - notify user (Change fragment about putting himself on the line)
    }


    // -------------------------
    // ------    USERS   -------
    // -------------------------

    public void changeUserName(String ID, String name) {
        mDatabase.child(USERS).child(ID).child("name").setValue(name);
    }

    public void loadUserData(String ID) {

        ValueEventListener loadUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    // or use UniqueIdentifier
                    user.setID(dataSnapshot.getKey());
                }

                MainActivity.currentUser = user;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        mDatabase.child("users").child(ID).addValueEventListener(loadUserListener);
    }

    public void updateName(String ID, String newName) {
        mDatabase.child(USERS).child(ID).child("name").setValue(newName);
    }

    public void registerNewUser(User user) {
        mDatabase.child(USERS).child(user.getID()).setValue(user);
    }

    public void registerNewUser(String ID, String name, String sex) {
        User user = new User(ID, name, sex);
        registerNewUser(user);
    }

    // -------------------------
    // ------    REPORT   ------
    // -------------------------

    public void reportUser(Report report) {
        DatabaseReference lineRef = mDatabase.child(REPORT);
        lineRef.push().setValue(report);
    }

}
