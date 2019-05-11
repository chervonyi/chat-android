package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import chr.chat.fragments.HeaderIntroductionFragment;
import chr.chat.views.ChatEditText;
import chr.chat.fragments.InputGenderFragment;
import chr.chat.fragments.InputNameFragment;
import chr.chat.R;

public class ChangeInfoActivity extends AppCompatActivity {

    public static final int INPUT_NAME_FRAGMENT_ID = 0;
    public static final int INPUT_GENDER_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    public static final int MAX_LENGTH = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        // Add fragments according to appropriate IDs
        mFragments.add(new InputNameFragment());
        mFragments.add(new InputGenderFragment());

        changeFragment(R.id.container, mFragments.get(INPUT_NAME_FRAGMENT_ID), "InputNameFragment", false);
        changeFragment(R.id.header, new HeaderIntroductionFragment(), "HeaderIntroductionFragment", false);
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

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void goToInputGender(View view) {
        changeFragment(R.id.container, mFragments.get(INPUT_GENDER_FRAGMENT_ID), "InputGenderFragment", true);
        String input = ((ChatEditText)findViewById(R.id.editTextName)).getText().toString();
        Log.d("CHR_GAMES_TEST", "Input: " + input);
        hideKeyboard();

        // Hide button "Done"
        view.setVisibility(View.INVISIBLE);
    }

    public void finishInput(String gender) {
        Log.d("CHR_GAMES_TEST", "Gender: " + gender);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_right);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }
}
