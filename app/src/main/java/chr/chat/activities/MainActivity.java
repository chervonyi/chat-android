package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import chr.chat.components.AdultContentExclusions;
import chr.chat.components.Database;
import chr.chat.components.FirebaseBackgroundService;
import chr.chat.components.GlobalSettings;
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
import chr.chat.fragments.HeaderPreloadFragment;
import chr.chat.views.AdultContentDialog;
import chr.chat.views.ChatPopupAttachMenu;
import chr.chat.views.ChatPopupMenu;

public class MainActivity extends AppCompatActivity {

    // User data
    public static User currentUser;

    // Current data
    public ArrayList<Message> currentMessages = new ArrayList<>();
    public static String currentChatID;
    public List<Chat> chatList = new ArrayList<>();

    // Vars
    private int lastNumberOfChats = 0;
    private String contextMenuForChatID;

    // Constants
    public final static String DESIRABLE_CHAT_ID = "NEW_CHAT_CODE";

    // Fragments:
    private HeaderChatListFragment headerChatListFragment = new HeaderChatListFragment();
    private HeaderEmptyChatListFragment headerEmptyChatListFragment = new HeaderEmptyChatListFragment();
    private EmptyListFragment emptyListFragment = new EmptyListFragment();
    public ChatFragment chatFragment = new ChatFragment();
    private HeaderPreloadFragment headerPreloadFragment = new HeaderPreloadFragment();

    // UI
    private FrameLayout headerLayout;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String desirableChatID = intent.getStringExtra(DESIRABLE_CHAT_ID);

        if (desirableChatID != null) {
            currentChatID = desirableChatID;
        }

        startService(new Intent(this, FirebaseBackgroundService.class));

        loadUserDataFromPhoneMemory();

        headerLayout = findViewById(R.id.header);
        preloadHeader(lastNumberOfChats);
        setHeaderSize(R.dimen.header_size);

        Database.instance.loadAllChats(this, UniqueIdentifier.identifier);
    }


    private void preloadHeader(int numberOfChats) {
//        if (numberOfChats != 0) {
//            setHeaderSize(R.dimen.header_size);
//        } else {
//            setHeaderSize(R.dimen.header_size_small);
//        }
        changeFragment(R.id.header, headerPreloadFragment, "HeaderPreloadFragment",false);
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

    @Override
    protected void onDestroy() {
        Database.instance.removeAllListener();
        super.onDestroy();
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
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setHeaderSize(int height) {
        headerLayout.getLayoutParams().height = (int) getResources().getDimension(height);
    }


    public void updateActivityView(ArrayList<Chat> list) {
        chatList = list;


        // Check if chat-list is empty or not
        if (chatList.size() == 0) {
            // Show header and body  fragments according to EMPTY chat-list
            changeFragment(R.id.container, emptyListFragment, "EmptyListFragment", false);
//            setHeaderSize(R.dimen.header_size_small);
            changeFragment(R.id.header, headerEmptyChatListFragment, "HeaderEmptyChatListFragment",false);
        } else {
            // Show header and body fragments according to NOT EMPTY chat-list
            if (currentChatID == null || !isContains(currentChatID)) {
                currentChatID = chatList.get(0).getID();
            }
            changeFragment(R.id.container, chatFragment, "ChatFragment", false);
//            setHeaderSize(R.dimen.header_size);
            changeFragment(R.id.header, headerChatListFragment, "HeaderChatListFragment",false);
        }
    }

    public void onSelectChat(View view) {

        String selectedChatID = view.getTag().toString();

        if (!selectedChatID.equals(currentChatID)) {
            currentChatID = selectedChatID;

            // Get messages for selected chat
            Database.instance.getMessagesForNewChat(this, currentChatID);
        }
    }

    public void hideScrollView() {
        chatFragment.hideScrollView();
    }


    public void setMessages(String chatID, ArrayList<Message> messages, boolean withAnim) {

        // Update messages only for current chat
        if (currentChatID != null && currentChatID.equals(chatID)) {
            currentMessages = messages;

            // Update name of the companion
            headerChatListFragment.setCompanionName(getChatByID(currentChatID));

            boolean switcherOnCheck = GlobalSettings.isChecked(this, GlobalSettings.CHECK_ON_ADULT_CONTENT);

            boolean isExclusion = AdultContentExclusions.isContains(this, currentChatID);

            chatFragment.setMessages(messages, (switcherOnCheck && !isExclusion), withAnim);
        }
    }


    public void onClickAttach(View view) {
        ChatPopupAttachMenu popupMenu = new ChatPopupAttachMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.attach_menu, popupMenu.getMenu());

        popupMenu.show();
    }

    public void showAdultContentDialog() {
        AdultContentDialog dialog = new AdultContentDialog(MainActivity.this, this);
        dialog.show();
    }

    public void adultContentDialogClick(boolean confirmed) {

        if (confirmed) {
            // Clicked 'Yes'
            AdultContentExclusions.append(this, currentChatID);
        } else {
            // Clicked 'No'

            // Remove chat
            Database.instance.closeChat(currentChatID);

            currentChatID = null;
        }
    }

    public void hideChatlistBlock() {
        chatFragment.hideChatListBlock();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remove_chat_menu, menu);
        contextMenuForChatID = v.getTag().toString();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_remove_chat:
                Database.instance.closeChat(contextMenuForChatID);

                // If you are removing the chat which is open right now
                if (contextMenuForChatID.equals(currentChatID)) {

                    // Assign null so it will set the the first chat ID from the chat-list
                    // in the future (when DB updates chat-list)
                    currentChatID = null;
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void attachBotMessage(int attachmentType) {
        chatFragment.attachBotMessage(attachmentType);
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

    private boolean isContains(String chatID) {
        return getChatByID(chatID) != null;
    }

    /**
     * Redirection to SearchActivity
     */
    public void onClickSearch(View view) {
        lastNumberOfChats = chatList.size();
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void goToChangePersonalName() {
        lastNumberOfChats = chatList.size();
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        intent.putExtra(ChangeInfoActivity.ENTER_CODE, ChangeInfoActivity.CHANGE_NAME_CODE);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void goToSettings() {
        lastNumberOfChats = chatList.size();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    /**
     * Listener on Menu button
     * @param view "Menu" button
     */
    public void onClickMenu(View view) {
        // Show menu
        ChatPopupMenu popupMenu = new ChatPopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();

        if (chatList != null && chatList.size() > 0) {
            inflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
        } else {
            inflater.inflate(R.menu.settings_menu_short, popupMenu.getMenu());
        }

        popupMenu.show();
    }
}
