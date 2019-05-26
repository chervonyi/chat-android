package chr.chat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import chr.chat.activities.MainActivity;
import chr.chat.components.BotAttachments;
import chr.chat.R;
import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.Chat;
import chr.chat.components.models.Line;
import chr.chat.components.models.Message;
import chr.chat.components.models.User;
import chr.chat.views.ChatBlockView;
import chr.chat.views.ChatBotBlockView;
import chr.chat.views.ChatInputMessageView;

public class ChatFragment extends Fragment {

    private final String END_PHRASE = "END_PHRASE_CODE";

    public static final String BOT_MESSAGES_CODE = "BOT_MESSAGES_ID_1984";


    private BotAttachments botAttachments = new BotAttachments();

    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private ChatInputMessageView inputView;

    // List of forbidden words that may contain adult content, offend somebody etc.
    private final ArrayList<String> forbiddenWords = getForbiddenWords();

    // List of phrases
    private HashMap<String, String> phrases_EN = getPhrases();


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatContainer = view.findViewById(R.id.chat_container);
        scrollView = view.findViewById(R.id.scrollViewChat);
        inputView = view.findViewById(R.id.input_message_view);

        setMessages(((MainActivity)getActivity()).currentMessages);

        // On click 'SEND'
        final ImageButton buttonSend = view.findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMessage = inputView.getText().toString();

                if (inputMessage.length() > 0) {
                    inputView.setText("");

                    Message message = new Message(MainActivity.currentChatID, inputMessage, UniqueIdentifier.identifier);
                    Database.instance.appendNewMessage(message);
                }
            }
        });

        // Set listener to do a full scroll to down every time on changes
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.setSmoothScrollingEnabled(false);
                        scrollView.fullScroll(View.FOCUS_DOWN);

                        if (scrollView.getVisibility() == View.INVISIBLE) {
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        hideScrollView();
        return view;
    }


    public void hideScrollView() {
        if (scrollView != null) {
            scrollView.setVisibility(View.INVISIBLE);
        }
    }

    public void setMessages(ArrayList<Message> messages) {

        if (chatContainer == null) { return; }

        chatContainer.removeAllViews();

        for (Message message : messages) {
            if (message.getOwner().equals(BOT_MESSAGES_CODE)) {
                printBotMessage(message.getMessage());
            } else if (message.getOwner().equals(UniqueIdentifier.identifier)) {
                appendMessage(message.getMessage(), true);
            } else {
                appendMessage(message.getMessage(), false);
            }
        }

    }

    private void printBotMessage(String message) {
        ChatBotBlockView botBlockView = new ChatBotBlockView(getContext());
        botBlockView.setText(message);
        chatContainer.addView(botBlockView);
    }

    /**
     * Append given message to chat
     * @param message necessary message
     * @param isUserMessage
     * <b>true</b> - it is user's message (Right)
     * <b>false</b> - it is companion's message (Left)
     */
    private void appendMessage(String message, boolean isUserMessage) {

        // TODO - add check for 'isUserMessage' == false
        if (isContainedAdultContent(message)) {
            //Toast.makeText(getContext(), "ADULT CONTENT", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).showAdultContentDialog();
        }

        ChatBlockView blockView = new ChatBlockView(getContext());
        blockView.setText(message);
        blockView.setOwner(isUserMessage);
        chatContainer.addView(blockView);
    }


    /**
     * Check if given text contains at least one of forbidden words. <br>
     * {@link #forbiddenWords} <br>
     * {@link #getForbiddenWords()}
     * @param text interested text
     * @return <b>true</b> if text contains one of forbidden words.
     * <b>false</b> if it does not contain any forbidden words.
     */
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

    public void attachBotMessage(int type) {
        String botMessage = botAttachments.getRandom(type);
        Message message = new Message(MainActivity.currentChatID, botMessage, BOT_MESSAGES_CODE);
        Database.instance.appendNewMessage(message);
    }


    // TODO - Fill the list up when the app will be connected to the DB.
    private HashMap<String, String> getPhrases() {
        return new HashMap<String, String>() {{
            put("Hey, what's up?", "Fine. Have you seen Avengers: EndGame?");
            put("Fine. Have you seen Avengers: EndGame?", END_PHRASE);

            put("Hi! How are you doing?", "Well. What about you?");
            put("Well. What about you?", END_PHRASE);
            // ....
        }};
    }

    // TODO - Fill the list up when the app will be connected to the DB.
    private ArrayList<String> getForbiddenWords() {
        return new ArrayList<>(Arrays.asList(
                // English
                "fuck", "cum", "dick", "cunt", "boner", "tits", "nipple", //...

                // Russian
                "ебать", "трахать", "сосать", "член", //...

                // Ukrainian
                "їбати", "трахати", "сосати" //...
        ));
    }
}
