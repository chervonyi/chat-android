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

import java.util.ArrayList;
import java.util.List;

import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.Chat;
import chr.chat.fragments.GenderQuestionFragment;
import chr.chat.fragments.HeaderSearchingFragment;
import chr.chat.fragments.LanguageQuestionFragment;
import chr.chat.R;
import chr.chat.fragments.SearchingFragment;
import chr.chat.views.ChatPopupMenu;

public class SearchActivity extends AppCompatActivity {


    private static Context context;

    // Fragments
    private GenderQuestionFragment genderQuestionFragment = new GenderQuestionFragment();
    private LanguageQuestionFragment languageQuestionFragment = new LanguageQuestionFragment();
    private SearchingFragment searchingFragment = new SearchingFragment();

    // Headers
    private HeaderSearchingFragment headerSearchingFragment = new HeaderSearchingFragment();

    private String selectedGender;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        changeFragment(R.id.container, genderQuestionFragment, "GenderQuestionFragment", false);
        changeFragment(R.id.header,headerSearchingFragment, "HeaderSearchingFragment", false);
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
     * Listener for "NEXT" button to change fragment with another question
     * @param gender selected gender
     */
    public void onInputGender(String gender) {
        selectedGender = gender;

        changeFragment(R.id.container, languageQuestionFragment, "LanguageQuestionFragment", true);
    }

    /**
     * Listener for "GO" button to move into another fragment and start searching of a new chat.
     * @param language selected language
     */
    public void startSearching(String language) {
        selectedLanguage = language;

        // Start searching
        Database.instance.searchSomebodyFor(MainActivity.currentUser, selectedGender, selectedLanguage);

        changeFragment(R.id.container, searchingFragment, "SearchingFragment", true);
    }

    /**
     * Listener for "Back" button to move to previous activity (MainActivity)
     * @param view "Back" button
     */
    public void onClickBack(View view) {
        goToMainActivity();
    }

    public static void goToChat() {
        ((SearchActivity)context).goToMainActivity();
    }

    /**
     * Listener for "Menu" button (...) to show PopupMenu
     * @param view "Menu" button
     */
    public void onClickMenu(View view) {
        // Show menu
        ChatPopupMenu popupMenu = new ChatPopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.settings_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }

    /**
     * Redirection to ChangeInfoActivity
     */
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
}
