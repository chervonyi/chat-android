package chr.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class ChangeInfoActivity extends AppCompatActivity {

    public static final int INPUT_NAME_FRAGMENT_ID = 0;
    public static final int INPUT_GENDER_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        // Add fragments according to appropriate IDs
        mFragments.add(new InputNameFragment());
        mFragments.add(new InputGenderFragment());

        changeFragment(INPUT_NAME_FRAGMENT_ID, false);
    }

    @SuppressLint("ResourceType")
    public void changeFragment(int position, boolean animation) {

        // Remove current fragment if it has been added before
        if (getSupportFragmentManager().findFragmentByTag(position + "") != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mFragments.get(position))
                    .commit();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (animation) {
            fragmentTransaction.setCustomAnimations(R.anim.enter, 0);
        }

        // Add current fragment
        fragmentTransaction.add(R.id.container, mFragments.get(position), position + "");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }
}
