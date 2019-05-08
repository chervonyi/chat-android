package chr.chat;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final int CHAT_FRAGMENT_ID = 0;
    public static final int EMPTY_LIST_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mChats = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add in order according to appropriate ID
        mFragments.add(new ChatFragment());
        mFragments.add(new EmptyListFragment());

        if (mChats.size() == 0) {
            changeFragment(EMPTY_LIST_FRAGMENT_ID, false);
            Log.d("CHR_GAMES_TEST", "EMPTY LIST FRAGMENT");
        } else {
            changeFragment(CHAT_FRAGMENT_ID, false);
            Log.d("CHR_GAMES_TEST", "CHAT FRAGMENT");
        }
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
}
