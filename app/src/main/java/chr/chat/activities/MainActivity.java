package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.Chat;
import chr.chat.components.models.Message;
import chr.chat.components.models.Report;
import chr.chat.components.models.User;
import chr.chat.fragments.ChatFragment;
import chr.chat.fragments.EmptyListFragment;
import chr.chat.R;
import chr.chat.fragments.HeaderChatListFragment;
import chr.chat.fragments.HeaderEmptyChatListFragment;
import chr.chat.views.ChatPopupAttachMenu;
import chr.chat.views.ChatPopupMenu;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    // User data
    public static User currentUser;

    public ArrayList<Message> currentMessages = new ArrayList<>();

    public static String currentChatID;


    // Fragments:
    private HeaderChatListFragment headerChatListFragment = new HeaderChatListFragment();
    private HeaderEmptyChatListFragment headerEmptyChatListFragment = new HeaderEmptyChatListFragment();
    private EmptyListFragment emptyListFragment = new EmptyListFragment();
    private ChatFragment chatFragment = new ChatFragment();

    private FrameLayout headerLayout;

    public List<Chat> chatList = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        loadUserDataFromPhoneMemory();

        headerLayout = findViewById(R.id.header);

        Database.instance.loadAllChats(UniqueIdentifier.identifier);
    }




    private void loadUserDataFromPhoneMemory() {

        if (!UniqueIdentifier.isCreatedBefore(this)) {
            // The first start ever
            // Go to INPUT_ALL_INFO to get required data to register a new user
            Intent intent = new Intent(this, ChangeInfoActivity.class);
            intent.putExtra(ChangeInfoActivity.ENTER_CODE, ChangeInfoActivity.INPUT_ALL_INFO_CODE);
            startActivity(intent);
            finish();
        }

        Database.instance.loadUserData(UniqueIdentifier.identifier);
    }

    private void setHeaderSize(int height) {
        headerLayout.getLayoutParams().height = (int) getResources().getDimension(height);
    }

    /**
     * Redirection to SearchActivity
     */
    public void onClickSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    /**
     * Redirection to ChangeInfoActivity
     */
    public void goToPersonalInfo() {
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    /**
     * Method for change any fragment on current activity
     * @param destination place of fragment
     * @param newFragment instance of necessary fragment
     * @param tag tag of string type
     * @param animation boolean value to use animation or not
     */
    @SuppressLint("ResourceType")
    public void changeFragment(int destination, Fragment newFragment, String tag, boolean animation) {

        // Remove current fragment if it has been added before
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(newFragment)
                    //.commit();
                    .commitAllowingStateLoss();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (animation) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, 0);
        } else {
            fragmentTransaction.setCustomAnimations(0, 0);
        }

        // Add current fragment
        fragmentTransaction.add(destination, newFragment, tag);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Listener on Menu button
     * @param view "Menu" button
     */
    public void onClickMenu(View view) {
        // Show menu
        ChatPopupMenu popupMenu = new ChatPopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
        popupMenu.show();
    }


    public void updateActivityView(ArrayList<Chat> list) {
        chatList = list;

        // Check if chat-list is empty or not
        if (chatList.size() == 0) {
            // Show header and body  fragments according to EMPTY chat-list
            changeFragment(R.id.container, emptyListFragment, "EmptyListFragment", false);
            setHeaderSize(R.dimen.header_size_small);
            changeFragment(R.id.header, headerEmptyChatListFragment, "HeaderEmptyChatListFragment",false);
        } else {
            // Show header and body fragments according to NOT EMPTY chat-list
            if (currentChatID == null) {
                currentChatID = chatList.get(0).getID();
            }
            changeFragment(R.id.container, chatFragment, "ChatFragment", false);
            setHeaderSize(R.dimen.header_size);
            changeFragment(R.id.header, headerChatListFragment, "HeaderChatListFragment",false);
        }
    }

    public void onSelectChat(View view) {
        //...
        // currentChatID = ...
    }

    public static void setChatList(ArrayList<Chat> chatList) {
        ((MainActivity)context).updateActivityView(chatList);
    }

    public static void setMessages(String chatID, ArrayList<Message> messages) {
        // Update messages only for current chat
        if (currentChatID != null && currentChatID.equals(chatID)) {
            ((MainActivity)context).currentMessages = messages;
            ((MainActivity)context).changeFragment(R.id.container, ((MainActivity)context).chatFragment, "ChatFragment", false);
        }
    }

    public void onClickAttach(View view) {
        ChatPopupAttachMenu popupMenu = new ChatPopupAttachMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.attach_menu, popupMenu.getMenu());

        popupMenu.show();
    }

    public void attachBotMessage(int attachmentType) {
        chatFragment.attachBotMessage(attachmentType);
    }

    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }


    public void reportUser() {

        Chat chat = getChatByID(currentChatID);

        if (chat != null) {
            String suspectID = chat.getUserID1();

            if (chat.getUserID1().equals(UniqueIdentifier.identifier)) {
                suspectID = chat.getUserID2();
            }

            Report report = new Report(suspectID, currentChatID);

            Database.instance.reportUser(report);
        }
    }

    public Chat getChatByID(String requiredID) {
        for (Chat chat : chatList) {
            if (chat.getID().equals(requiredID)) {
                return chat;
            }
        }
        return null;
    }
}
