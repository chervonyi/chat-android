package chr.chat.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.fragments.HeaderIntroductionFragment;
import chr.chat.views.ChatEditText;
import chr.chat.fragments.InputGenderFragment;
import chr.chat.fragments.InputNameFragment;
import chr.chat.R;

public class ChangeInfoActivity extends AppCompatActivity {

    // Fragments
    private InputNameFragment inputNameFragment = new InputNameFragment();
    private InputGenderFragment inputGenderFragment = new InputGenderFragment();
    private HeaderIntroductionFragment headerIntroductionFragment = new HeaderIntroductionFragment();

    // Constants
    public static final int INPUT_ALL_INFO_CODE = 50001;
    public static final int CHANGE_NAME_CODE = 50002;
    public static final String ENTER_CODE = "ENTER_CODE";

    // Vars
    private int currentEnterCode;
    private String enteredName;

    // Max length of input string (EditView)
    public static final int MAX_LENGTH = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        Intent intent = getIntent();

        // Read enter code
        currentEnterCode = intent.getIntExtra(ENTER_CODE, INPUT_ALL_INFO_CODE);

        // Go to Input Name fragment
        changeFragment(R.id.container, inputNameFragment, "InputNameFragment", false);
        changeFragment(R.id.header, headerIntroductionFragment, "HeaderIntroductionFragment", false);
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

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    /**
     * Hides the forced shown keyboard.
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * Listener on click "Done" button.
     * @param view "Done" button
     */
    public void onInputName(View view) {

        String input = ((ChatEditText)findViewById(R.id.editTextName)).getText().toString();

        hideKeyboard();

        if (currentEnterCode == INPUT_ALL_INFO_CODE) {
            // First run ever

            enteredName = input;

            // Go to input gender fragment
            changeFragment(R.id.container, inputGenderFragment, "InputGenderFragment", true);

            // Hide button "Done"
            view.setVisibility(View.INVISIBLE);
        } else if (currentEnterCode == CHANGE_NAME_CODE) {
            // Changing info

            // Update user name in Database
            Database.instance.changeUserName(UniqueIdentifier.identifier, input);

            goToMainActivity();
        }
    }

    /**
     * Listener on click "Save" button
     * @param gender selected gender
     */
    public void onInputGender(String gender) {
        // Register a new user into Database
        Database.instance.registerNewUser(UniqueIdentifier.identifier, enteredName, gender);

        goToMainActivity();
    }

    /**
     * Listener on click "Back" button
     * @param view "Back" button
     */
    public void onClickBack(View view) {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_left);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
