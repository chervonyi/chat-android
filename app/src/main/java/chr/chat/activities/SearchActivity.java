package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import chr.chat.fragments.GenderQuestionFragment;
import chr.chat.fragments.HeaderSearchingFragment;
import chr.chat.fragments.LanguageQuestionFragment;
import chr.chat.R;

public class SearchActivity extends AppCompatActivity {

    public static final int QUESTION_GENDER_FRAGMENT_ID = 0;
    public static final int QUESTION_LANGUAGE_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    private String selectedGender;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Add in order according to ID numbers
        mFragments.add(new GenderQuestionFragment());
        mFragments.add(new LanguageQuestionFragment());

        changeFragment(R.id.container, mFragments.get(QUESTION_GENDER_FRAGMENT_ID), "GenderQuestionFragment", false);
        changeFragment(R.id.header, new HeaderSearchingFragment(), "HeaderSearchingFragment", false);
    }

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

    public void answeredOnGender(String gender) {
        selectedGender = gender;
        Log.d("CHR_GAMES_TEST", "selected gender is: " + gender);
        changeFragment(R.id.container, mFragments.get(QUESTION_LANGUAGE_FRAGMENT_ID), "LanguageQuestionFragment", true);
    }

    public void startSearching(String language) {
        selectedLanguage = language;
        Log.d("CHR_GAMES_TEST", "selected language is: " + language);
        // TODO - change fragment and start searching for a chat

        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }

}
