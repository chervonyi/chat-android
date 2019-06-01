package chr.chat.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    // Listeners:
    private Map<String, ValueEventListener> mapListenersForMessages = new HashMap<>();
    private ValueEventListener chatsEventListener;
    private ValueEventListener loadUserEventListener;


    // -------------------------
    // ------  MESSAGES  -------
    // -------------------------

    public void appendNewMessage(Message messageNode) {
        DatabaseReference lineRef = mDatabase.child(MESSAGES);
        lineRef.push().setValue(messageNode);
    }

    public void assignListenerOnMessagesForChat(final Context context, final String chatID) {

        if (mapListenersForMessages.containsKey(chatID)) { return; }

        ValueEventListener messagesEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                // Sort messages by date and time manually
                // because Firebase does not support order by 2+ keys
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return (int)((long)o1.getDatetime() - (long)o2.getDatetime());
                    }
                });

                ((MainActivity)context).setMessages(chatID, messages, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        mapListenersForMessages.put(chatID, messagesEventListener);


        mDatabase.child(MESSAGES).orderByChild("chatID").equalTo(chatID).addValueEventListener(messagesEventListener);
    }

    private void removeMapListeners() {

        if (mapListenersForMessages == null || mapListenersForMessages.size() == 0) { return; }

        for (Map.Entry<String, ValueEventListener> entry : mapListenersForMessages.entrySet()) {
            if (entry.getValue() != null) {
                mDatabase.child(MESSAGES).orderByChild("chatID")
                        .equalTo(entry.getKey())
                        .removeEventListener(entry.getValue());
            }

        }

        mapListenersForMessages.clear();
    }

    public void removeAllListener() {
        removeMapListeners();
        removeChatsEventListener();
        removeLoadUserEventListener();
    }


    // On select another chat
    public void getMessagesForNewChat(final Context context, final String chatID, final boolean useAnimation) {

        mDatabase.child(MESSAGES).orderByChild("chatID").equalTo(chatID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> messages = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                // Sort messages by date and time manually
                // because Firebase does not support order by 2+ keys
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return (int)((long)o1.getDatetime() - (long)o2.getDatetime());
                    }
                });

                if (context instanceof MainActivity) {
                    ((MainActivity)context).setMessages(chatID, messages, useAnimation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    // -------------------------
    // ------    CHATS   -------
    // -------------------------

    public void closeChat(String chatID) {
        mDatabase.child(CHATS).child(chatID).child("open").setValue(false);
    }

    public String createChat(Chat chatNode) {
        DatabaseReference lineRef = mDatabase.child(CHATS).push();
        String pushedUniqueID = lineRef.getKey();
        lineRef.setValue(chatNode);
        return pushedUniqueID;
    }

    public void loadAllChats(final Context context, final String userID) {

        chatsEventListener = new ValueEventListener() {
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

                removeMapListeners();

                for (Chat foundChat: chats) {
                    if (foundChat.getUserID1().equals(userID) ||
                        foundChat.getUserID2().equals(userID)) {

                        if (foundChat.isOpen()) {

                            // Append found chat to chat-list of currentUser
                            requiredChatList.add(foundChat);

                            // Assign listener to load all messages for current chat
                            assignListenerOnMessagesForChat(context, foundChat.getID());
                        }
                    }
                }

                if (context instanceof MainActivity) {
                    ((MainActivity)context).updateActivityView(requiredChatList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        };

        mDatabase.child(CHATS).addValueEventListener(chatsEventListener);
    }

    private void removeChatsEventListener() {
        if (chatsEventListener != null) {
            mDatabase.child(CHATS).removeEventListener(chatsEventListener);
        }
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

    public void searchSomebodyFor(final Context context, final User user, final String sex, final String language) {

        mDatabase.child(LINE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Line> arrayList = new ArrayList<>();

                for (DataSnapshot lineNode : dataSnapshot.getChildren()) {
                    arrayList.add(lineNode.getValue(Line.class));
                }

                lookForMatches(context, user, arrayList, sex, language);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void lookForMatches(final Context context,User userWhichSearching, ArrayList<Line> lineNodes, String sex, String language) {

        for (Line line : lineNodes) {

            if (language.equals(line.getLanguage()) && !line.getUserID().equals(userWhichSearching.getID())) {

                if (line.getSex().equals(userWhichSearching.getSex()) || line.getSex().equals("any")) {
                    if (sex.equals(line.getUserSex()) || sex.equals("any")) {

                        // Remove current user from line
                        removeUserFromLine(line.getUserID());

                        // Important: userWhichSearching must be added into database in chat instance as a user #1
                        // Note: Chat instance contains pair of users: user #1 and user #2
                        Chat currentChat = new Chat(userWhichSearching.getID(), userWhichSearching.getName(), line.getUserID(), line.getUserName());

                        // Create chat in database
                        String createdChatID = createChat(currentChat);

                        if (context instanceof SearchActivity) {
                            ((SearchActivity)context).goToMainActivity(createdChatID);
                        }

                        return;
                    }
                }
            }
        }

        // Add userWhichSearching to Line
        Line line = new Line(userWhichSearching.getID(), userWhichSearching.getName(), userWhichSearching.getSex(), sex, language);
        putUserInLine(line);
    }


    // -------------------------
    // ------    USERS   -------
    // -------------------------

    public void changeUserName(String ID, String name) {
        mDatabase.child(USERS).child(ID).child("name").setValue(name);
    }

    public void loadUserData(final Context context, String ID) {

        loadUserEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    // or use UniqueIdentifier
                    user.setID(dataSnapshot.getKey());
                }

//                MainActivity.currentUser = user;
                ((MainActivity)context).setUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        mDatabase.child(USERS).child(ID).addValueEventListener(loadUserEventListener);
    }

    private void removeLoadUserEventListener() {
        if (loadUserEventListener != null) {
            mDatabase.child(USERS).child(UniqueIdentifier.identifier)
                    .removeEventListener(loadUserEventListener);
        }
    }

    public void registerNewUser(String ID, String name, String sex) {
        User user = new User(ID, name, sex);
        mDatabase.child(USERS).child(user.getID()).setValue(user);
    }

    // -------------------------
    // ------    REPORT   ------
    // -------------------------

    public void reportUser(Report report) {
        DatabaseReference lineRef = mDatabase.child(REPORT);
        lineRef.push().setValue(report);
    }

}
