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

import chr.chat.fragments.ChatFragment;
import chr.chat.fragments.EmptyListFragment;
import chr.chat.R;
import chr.chat.fragments.HeaderChatListFragment;
import chr.chat.fragments.HeaderEmptyChatListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CHAT_FRAGMENT_ID = 0;
    private static final int EMPTY_LIST_FRAGMENT_ID = 1;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mChats = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add in order according to ID numbers
        mFragments.add(new ChatFragment());
        mFragments.add(new EmptyListFragment());

        if (mChats.size() == 0) {
            changeFragment(R.id.container, mFragments.get(EMPTY_LIST_FRAGMENT_ID), "EmptyListFragment", false);
            findViewById(R.id.header).getLayoutParams().height = (int) getResources().getDimension(R.dimen.header_size_small);
            changeFragment(R.id.header, new HeaderEmptyChatListFragment(), "HeaderEmptyChatListFragment",false);
        } else {
            changeFragment(R.id.container, mFragments.get(CHAT_FRAGMENT_ID), "ChatFragment", false);
            findViewById(R.id.header).getLayoutParams().height = (int) getResources().getDimension(R.dimen.header_size);
            changeFragment(R.id.header, new HeaderChatListFragment(), "HeaderChatListFragment",false);
        }
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // TODO - Remove then
    public void onClickInput(View view) {
        Intent intent = new Intent(this, ChangeInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
            fragmentTransaction.setCustomAnimations(R.anim.enter, 0);
        } else {
            fragmentTransaction.setCustomAnimations(0, 0);
        }

        // Add current fragment
        fragmentTransaction.add(destination, newFragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
