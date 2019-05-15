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

import chr.chat.R;
import chr.chat.activities.MainActivity;
import chr.chat.views.ChatBlockView;
import chr.chat.views.ChatInputMessageView;

public class ChatFragment extends Fragment {

    private LinearLayout chatContainer;
    private ScrollView scrollView;
    private ChatInputMessageView inputView;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatContainer = view.findViewById(R.id.chat_container);
        scrollView = view.findViewById(R.id.scrollViewChat);
        inputView = view.findViewById(R.id.input_message_view);
        //inputView.requestFocus();

        ChatBlockView blockView = new ChatBlockView(getContext());
        blockView.setText("My custom text!");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My another user custom text! Long very long some random text. My another user custom text! Long very long some random text");
        blockView.setOwner(false);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("And little text");
        blockView.setOwner(false);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("Ok, sure.");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("What do you want?");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My another user custom text! Long very long some random text.");
        blockView.setOwner(false);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My another user custom text! Long very long some random text.");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("Yeah! Great!");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My custom text!");
        blockView.setOwner(true);
        chatContainer.addView(blockView);


        blockView = new ChatBlockView(getContext());
        blockView.setText("My another user custom text! Long very long some random text. My another user custom text! Long very long some random text");
        blockView.setOwner(false);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My custom text!");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

        blockView = new ChatBlockView(getContext());
        blockView.setText("My another user custom text! Long very long some random text. My another user custom text! Long very long some random text");
        blockView.setOwner(true);
        chatContainer.addView(blockView);

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
}
