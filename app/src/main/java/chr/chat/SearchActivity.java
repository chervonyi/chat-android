package chr.chat;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final int QUESTION_GENDER_FRAGMENT_ID = 0;
    private static final int QUESTION_LANGUAGE_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Add in order according to ID numbers
        mFragments.add(new GenderQuestionFragment());
        mFragments.add(new LanguageQuestionFragment());

        changeFragment(QUESTION_GENDER_FRAGMENT_ID, false);
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
        } else {
            fragmentTransaction.setCustomAnimations(0, 0);
        }

        // Add current fragment
        fragmentTransaction.add(R.id.container, mFragments.get(position), position + "");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
