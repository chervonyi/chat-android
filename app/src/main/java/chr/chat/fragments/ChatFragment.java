package chr.chat.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import chr.chat.R;
import chr.chat.activities.MainActivity;
import chr.chat.views.ChatBlockView;
import chr.chat.views.ChatInputMessageView;

public class ChatFragment extends Fragment {

    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private ChatInputMessageView inputView;

    // List of forbidden words that may contain adult content, offend somebody etc.
    // TODO - Fill the list up when the app will be connected to the DB.
    private ArrayList<String> forbiddenWords = new ArrayList<>(Arrays.asList(
            // English
            "fuck", "cum", "dick", "cunt", "boner", "tits", "nipple", //...

            // Russian
            "ебать", "трахать", "сосать", "член", //...

            // Ukrainian
            "їбати", "трахати", "сосати" //...
    ));


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatContainer = view.findViewById(R.id.chat_container);
        scrollView = view.findViewById(R.id.scrollViewChat);
        inputView = view.findViewById(R.id.input_message_view);

        appendMessage("Anyway, I keep picturing all these little kids playing some game in this big field of rye and all.", false);
        appendMessage("Thousands of little kids, and nobody's around, nobody big, I mean, except me. ", false);
        appendMessage("What I have to do, I have to catch everybody if they start to go over the cliff", false);
        appendMessage("I know it's crazy, but that's the only thing I'd really like to be.", false);
        appendMessage("I know it's crazy.", false);
        appendMessage("Daddy's going to kill you", true);

        scrollDown();

        // On click 'SEND'
        final ImageButton buttonSend = view.findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMessage = inputView.getText().toString();

                if (inputMessage.length() > 0) {
                    inputView.setText("");

                    appendMessage(inputMessage, true);
                }

                scrollDown();
            }
        });

        return view;
    }


    private void appendMessage(String message, boolean isUserMessage) {

        // TODO - add check for 'isUserMessage' == false

        if (isContainedAdultContent(message)) {
            Toast.makeText(getContext(), "ADULT CONTENT", Toast.LENGTH_SHORT).show();
            // TODO - move to the special MessageDialog
        }

        ChatBlockView blockView = new ChatBlockView(getContext());
        blockView.setText(message);
        blockView.setOwner(isUserMessage);
        chatContainer.addView(blockView);
    }


    private void scrollDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.setSmoothScrollingEnabled(false);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    private boolean isContainedAdultContent(String text) {

        text = text.toLowerCase();

        // Check for contains of forbidden words
        for (String word : forbiddenWords) {
            if (text.contains(word)) {
                return true;
            }
        }

        return false;
    }
}
