package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.User;
import chr.chat.fragments.ChatFragment;
import chr.chat.fragments.EmptyListFragment;
import chr.chat.R;
import chr.chat.fragments.HeaderChatListFragment;
import chr.chat.fragments.HeaderEmptyChatListFragment;
import chr.chat.views.ChatPopupAttachMenu;
import chr.chat.views.ChatPopupMenu;

public class MainActivity extends AppCompatActivity {


    // User data
    public static User currentUser;


    // Fragments:
    private HeaderChatListFragment headerChatListFragment = new HeaderChatListFragment();
    private HeaderEmptyChatListFragment headerEmptyChatListFragment = new HeaderEmptyChatListFragment();
    private EmptyListFragment emptyListFragment = new EmptyListFragment();
    private ChatFragment chatFragment = new ChatFragment();

    private FrameLayout headerLayout;

    private List<String> mChats = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUserDataFromPhoneMemory();

        Log.d("CHR_GAMES_TEST", "MainActivity: onCreate: user is: " + currentUser);

        headerLayout = findViewById(R.id.header);

        // Check if chat-list is empty or not
        if (mChats.size() == 0) {
            // Show header and body  fragments according to EMPTY chat-list
            changeFragment(R.id.container, emptyListFragment, "EmptyListFragment", false);
            setHeaderSize(R.dimen.header_size_small);
            changeFragment(R.id.header, new HeaderEmptyChatListFragment(), "HeaderEmptyChatListFragment",false);
        } else {
            // Show header and body fragments according to NOT EMPTY chat-list
            changeFragment(R.id.container, chatFragment, "ChatFragment", false);
            setHeaderSize(R.dimen.header_size);
            changeFragment(R.id.header, new HeaderChatListFragment(), "HeaderChatListFragment",false);
        }
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
                    .commit();
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
        fragmentTransaction.commit();
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
}
