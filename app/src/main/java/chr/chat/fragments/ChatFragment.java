package chr.chat.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import chr.chat.activities.MainActivity;
import chr.chat.components.BotAttachments;
import chr.chat.R;
import chr.chat.components.Database;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.Chat;
import chr.chat.components.models.Message;
import chr.chat.views.ChatBlockView;
import chr.chat.views.ChatBotBlockView;
import chr.chat.views.ChatIconButton;
import chr.chat.views.ChatInputMessageView;

public class ChatFragment extends Fragment {

    private final String END_PHRASE = "END_PHRASE_CODE";
    public static final String BOT_MESSAGES_CODE = "BOT_MESSAGES_ID_1984";
    private BotAttachments botAttachments = new BotAttachments();
    private boolean foundAdultContent = false;

    // UI
    private LinearLayout chatContainer;
    private LinearLayout chatListContainer;
    private HorizontalScrollView chatListBlock;
    private ScrollView scrollView;
    private ChatInputMessageView inputView;
    private TextView labelEmptyChat;

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
        chatListBlock = view.findViewById(R.id.chatlist_block);
        labelEmptyChat = view.findViewById(R.id.textview_empty_chat);

        //setMessages(((MainActivity)getActivity()).currentMessages);

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
                            Animation aniFade = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);
                            scrollView.setVisibility(View.VISIBLE);
                            scrollView.startAnimation(aniFade);
                        }
                    }
                });
            }
        });


        chatListContainer = view.findViewById(R.id.chat_list_container);
        setChatList(((MainActivity) Objects.requireNonNull(getActivity())).chatList);

        hideScrollView();

        return view;
    }


    // --------------------------------
    //   Main methods
    // --------------------------------

    public void setChatList(List<Chat> chatlist) {
        ChatIconButton chatIconButton;

        if (chatListContainer == null || getContext() == null) { return; }

        chatListContainer.removeAllViews();
        for (Chat chat : chatlist) {
            chatIconButton = new ChatIconButton(getContext());

            if (chat.getUserID1().equals(UniqueIdentifier.identifier)) {
                chatIconButton.setName(chat.getUserName2());
            } else {
                chatIconButton.setName(chat.getUserName1());
            }

            chatIconButton.setTag(chat.getID());

            chatListContainer.addView(chatIconButton);
            registerForContextMenu(chatIconButton);
        }

    }

    public void setMessages(ArrayList<Message> messages, boolean checkOnAdultContent, boolean withAnim) {

        if (getContext() == null) { return; }

        foundAdultContent = false;
        chatContainer.removeAllViews();

        if (messages.size() == 0) {
            labelEmptyChat.setVisibility(View.VISIBLE);
            return;
        }

        labelEmptyChat.setVisibility(View.INVISIBLE);


        for (Message message : messages) {
            if (message.getOwner().equals(BOT_MESSAGES_CODE)) {
                printBotMessage(message.getMessage());
            } else if (message.getOwner().equals(UniqueIdentifier.identifier)) {
                appendMessage(message.getMessage(), true, checkOnAdultContent);
            } else {
                appendMessage(message.getMessage(), false, checkOnAdultContent);
            }
        }

        if (withAnim) {
            Animation aniFade = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.fade_in);
            scrollView.startAnimation(aniFade);
        }

        if (foundAdultContent) {
            ((MainActivity)getActivity()).showAdultContentDialog();
        }
    }


    // --------------------------------
    //   Supporting methods
    // --------------------------------

    public void hideChatListBlock() {
        if (chatListBlock == null) { return; }

        if (chatListBlock.getVisibility() == View.VISIBLE) {

            chatListBlock.animate()
                    .translationY(-10)
                    .alpha(0.0f)
                    .setDuration(150)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            chatListBlock.setVisibility(View.INVISIBLE);
                        }
                    });

        } else {
            chatListBlock.animate()
                    .translationY(10)
                    .alpha(1.0f)
                    .setDuration(150)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            chatListBlock.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    public void hideScrollView() {
        if (scrollView != null) {
            scrollView.setVisibility(View.INVISIBLE);
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
    private void appendMessage(String message, boolean isUserMessage, boolean checkOnAdultContent) {

        // Check on adult content only if it's companion's message
        // and it's allowed to check contains of messages (checkOnAdultContent)

        if (!isUserMessage && checkOnAdultContent) {
            if (isContainedAdultContent(message)) {
                foundAdultContent = true;
            }
        }

        ChatBlockView blockView = new ChatBlockView(getContext());
        blockView.setText(message);
        blockView.setOwner(isUserMessage);
        chatContainer.addView(blockView);
    }


    /**
     * <b>Useless checking but I think it will prevent most of the attempts to use adult content</b><br>
     * Check if given text contains at least one of forbidden words. <br>
     * {@link #getForbiddenWords()}
     * @param text interested text
     * @return <b>true</b> if text contains one of forbidden words.
     * <b>false</b> if it does not contain any forbidden words.
     */
    private boolean isContainedAdultContent(String text) {
        text = text.toLowerCase();

        // Check for contains of forbidden words
        for (String word : getForbiddenWords()) {
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


    private HashMap<String, String> getPhrases() {
        return new HashMap<String, String>() {{
            put("Hey, what's up?", "Fine. Have you seen Avengers: EndGame?");
            put("Fine. Have you seen Avengers: EndGame?", END_PHRASE);

            put("Hi! How are you doing?", "Well. What about you?");
            put("Well. What about you?", END_PHRASE);
            // ....
        }};
    }

    private ArrayList<String> getForbiddenWords() {
        return new ArrayList<>(Arrays.asList(
                // English:
                "fuck", "cum", "dick", "cunt", "boner", "tits", "nipple", "ass", "asshole",
                "bitch", "slut", "whore", "sex", "butt", "vagina", "anus", "anus", "cock",
                "semen", "fucking", "fucks", "gay", "penis", "pussy", "wank",

                // Russian:
                "ебать", "кончать", "член", "пизда", "стояк", "сиськи", "сосок", "задница", "сука",
                "шлюха", "секс", "влагалище", "анус", "клитор", "жопа", "трахать",
                "сперма", "трахаться", "гей", "пенис", "киска", "дрочить", "сосать",

                // Ukrainian:
                "їбати", "кінчати", "член", "пізда", "стояк", "груди", "сосок", "дупа", "сука",
                "повія", "секс", "піхва", "анус", "клітор", "трахати",
                "сперма", "трахатись", "гей", "пеніс", "дрочити", "член", "сосати"
        ));
    }
}
