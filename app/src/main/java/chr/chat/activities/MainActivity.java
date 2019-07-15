package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private static User currentUser;

    // Current data
    public ArrayList<Message> currentMessages = new ArrayList<>();
    public static String currentChatID;
    public List<Chat> chatList = new ArrayList<>();

    // Vars
    private String contextMenuForChatID;
    private static boolean listenersRemovedBefore = false;
    private SharedPreferences preferences;

    // Constants
    public final static String DESIRABLE_CHAT_ID = "NEW_CHAT_CODE";
    public final static String HINT_KEY = "TOAST_CHATLIST_BOOL";

    // Fragments:
    private HeaderChatListFragment headerChatListFragment = new HeaderChatListFragment();
    private HeaderEmptyChatListFragment headerEmptyChatListFragment = new HeaderEmptyChatListFragment();
    private EmptyListFragment emptyListFragment = new EmptyListFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private HeaderPreloadFragment headerPreloadFragment = new HeaderPreloadFragment();


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadUserDataFromPhoneMemory();

        Intent intent = getIntent();
        String desirableChatID = intent.getStringExtra(DESIRABLE_CHAT_ID);

        if (desirableChatID != null) {
            Database.instance.removeAllListener();
            listenersRemovedBefore = true;

            currentChatID = desirableChatID;
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preloadHeader();

        startService(new Intent(this, FirebaseBackgroundService.class));
        Database.instance.loadAllChats(this, UniqueIdentifier.identifier);
    }


    // --------------------------------
    //    Main Methods
    // --------------------------------

    private void loadUserDataFromPhoneMemory() {

        if (!UniqueIdentifier.isCreatedBefore(this)) {
            // The first start ever
            // Go to INPUT_ALL_INFO to get required data to register a new user
            Intent intent = new Intent(this, ChangeInfoActivity.class);
            intent.putExtra(ChangeInfoActivity.ENTER_CODE, ChangeInfoActivity.INPUT_ALL_INFO_CODE);
            startActivity(intent);
            finish();
        } else {
            Database.instance.loadUserData(this, UniqueIdentifier.identifier);
        }
    }

    public void updateActivityView(ArrayList<Chat> list) {

        List<Chat> previousChatList = chatList;

        chatList = list;

        if (chatList.size() == 0) {
            // Now: empty
            changeFragment(R.id.container, emptyListFragment, "EmptyListFragment", false);
            changeFragment(R.id.header, headerEmptyChatListFragment, "HeaderEmptyChatListFragment",false);
        } else if (previousChatList.size() == 0) {
            // Now: not empty. Before: empty.
            if (currentChatID == null || !isContains(currentChatID)) {
                currentChatID = chatList.get(0).getID();
            }
            changeFragment(R.id.container, chatFragment, "ChatFragment", false);
            changeFragment(R.id.header, headerChatListFragment, "HeaderChatListFragment",false);

            showHintToast();
        } else {
            // Now: not empty. Before: not empty.
            // Update chat-list content (Messages will be updated later via Firebase listener)
            chatFragment.setChatList(chatList);
        }
    }

    public void onSelectChat(View view) {

        String selectedChatID = view.getTag().toString();

        if (!selectedChatID.equals(currentChatID)) {
            currentChatID = selectedChatID;

            // Get messages for selected chat
            Database.instance.getMessagesForNewChat(this, currentChatID, true);
        }
    }

    public void setMessages(String chatID, ArrayList<Message> messages, boolean useAnimation) {

        // Update messages only for current chat
        if (currentChatID != null && currentChatID.equals(chatID)) {
            currentMessages = messages;

            // Update name of the companion
            headerChatListFragment.setCompanionName(getChatByID(currentChatID));

            boolean switcherOnCheck = GlobalSettings.isChecked(this, GlobalSettings.CHECK_ON_ADULT_CONTENT);
            boolean isExclusion = AdultContentExclusions.isContain(this, currentChatID);

            chatFragment.setMessages(messages, (switcherOnCheck && !isExclusion), useAnimation);
        }
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
            Toast.makeText(this, "Reported", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Remove chat from chat-list
        if (item.getItemId() == R.id.option_remove_chat) {

            boolean useAnimation = false;
            if (contextMenuForChatID.equals(currentChatID)) {
                useAnimation = true;
            }

            if (chatList.size() > 1) {
                for (Chat chat: chatList) {
                    if (chat.getID().equals(contextMenuForChatID)) {
                        chatList.remove(chat);
                        break;
                    }
                }

                currentChatID = chatList.get(0).getID();

                // Load messages for a new chat
                Database.instance.getMessagesForNewChat(this, currentChatID, useAnimation);
            }

            Database.instance.closeChat(contextMenuForChatID);

            return true;
        }
        return super.onContextItemSelected(item);
    }


    public void closeCurrentChat() {
        String chatToDelete = currentChatID;

        if (chatList.size() > 1) {
            for (Chat chat: chatList) {
                if (chat.getID().equals(currentChatID)) {
                    chatList.remove(chat);
                    break;
                }
            }

            // Chat id that will be shown after removing
            currentChatID = chatList.get(0).getID();

            // Load messages for this chat
            Database.instance.getMessagesForNewChat(this, currentChatID, true);
        }

        Database.instance.closeChat(chatToDelete);
    }


    // --------------------------------
    // Adult Content Dialog methods
    // --------------------------------

    public void showAdultContentDialog() {
        AdultContentDialog dialog = new AdultContentDialog(MainActivity.this, this);
        dialog.show();
    }

    /**
     * Listener for Adult Content Dialog
     * @param confirmed <b>true</b> - pressed YES. <b>false</b> - pressed NO.
     */
    public void adultContentDialogClick(boolean confirmed) {

        if (confirmed) {
            // Clicked 'Yes'
            AdultContentExclusions.append(this, currentChatID);
        } else {
            // Clicked 'No'

            // Remove chat
            Database.instance.closeChat(currentChatID);

            if (chatList.size() > 1) {
                for (Chat chat: chatList) {
                    if (chat.getID().equals(currentChatID)) {
                        chatList.remove(chat);
                        break;
                    }
                }

                currentChatID = chatList.get(0).getID();

                // Load messages for a new chat
                Database.instance.getMessagesForNewChat(this, currentChatID, true);
            } else {
                currentChatID = null;
            }
        }
    }

    // ----------------------------
    //   Redirection methods
    // ----------------------------
    public void onClickSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void goToChangePersonalName() {
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        intent.putExtra(ChangeInfoActivity.ENTER_CODE, ChangeInfoActivity.CHANGE_NAME_CODE);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    // --------------------------------
    //   Listeners
    // --------------------------------

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

    @Override
    public void onBackPressed() {
        // Put app on the background
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        // Remove all listeners
        if (!listenersRemovedBefore) {
            Database.instance.removeAllListener();
        } else {
            listenersRemovedBefore = false;
        }
        super.onDestroy();
    }

    // ----------------------------
    //      Supporting methods
    // ----------------------------

    private void preloadHeader() {
        changeFragment(R.id.header, headerPreloadFragment, "HeaderPreloadFragment",false);
    }

    private boolean isContains(String chatID) {
        return getChatByID(chatID) != null;
    }

    public static User getUser() {
        return currentUser;
    }

    public void attachBotMessage(int attachmentType) {
        chatFragment.attachBotMessage(attachmentType);
    }

    public Chat getChatByID(String requiredID) {
        for (Chat chat : chatList) {
            if (chat.getID().equals(requiredID)) {
                return chat;
            }
        }
        return null;
    }

    /**
     * Shows toast with hint to user until it is called {@link #hideChatlistBlock()}
     */
    public void showHintToast() {
        if (!preferences.getBoolean(HINT_KEY, false)) {

            final Context context = this;
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, R.string.hint_toast_hide_chatlist_box, Toast.LENGTH_LONG);

                    // Set text alignment to center
                    LinearLayout layout = (LinearLayout) toast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }

                    toast.show();
                }
            }, 2000);
        }
    }

    public void hideChatlistBlock() {

        if (!preferences.getBoolean(HINT_KEY, false)) {
            preferences.edit().putBoolean(HINT_KEY, true).apply();
        }

        chatFragment.hideChatListBlock();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Show remove chat menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remove_chat_menu, menu);
        contextMenuForChatID = v.getTag().toString();
    }

    public void onClickAttach(View view) {
        ChatPopupAttachMenu popupMenu = new ChatPopupAttachMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.attach_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    /**
     * Update instance of User class which keeps current user's data
     * @param user a new instance of User class
     */
    public void setUser(User user) {

        if (user != null && user.isAvailable()) {
            currentUser = user;
        } else {
            // Force close app because the fact that user has been banned before.
            finishAndRemoveTask();
        }
    }
}
