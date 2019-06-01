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


    // Fragments
    private GenderQuestionFragment genderQuestionFragment = new GenderQuestionFragment();
    private LanguageQuestionFragment languageQuestionFragment = new LanguageQuestionFragment();
    private SearchingFragment searchingFragment = new SearchingFragment();

    // Headers
    private HeaderSearchingFragment headerSearchingFragment = new HeaderSearchingFragment();

    private String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        // Search for appropriate chat in database
        Database.instance.searchSomebodyFor(this, MainActivity.getUser(), selectedGender, language);

        // Change appropriate fragment
        changeFragment(R.id.container, searchingFragment, "SearchingFragment", true);
    }

    /**
     * Listener for "Back" button to move to previous activity (MainActivity)
     * @param view "Back" button
     */
    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }

    public void goToMainActivity(String desirableChatID) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.DESIRABLE_CHAT_ID, desirableChatID);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
