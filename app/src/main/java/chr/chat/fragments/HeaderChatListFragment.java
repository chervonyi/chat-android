package chr.chat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import chr.chat.R;
import chr.chat.views.ChatIconButton;

public class HeaderChatListFragment extends Fragment {

    private LinearLayout chatListContainer;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_header_chatlist, container, false);

        chatListContainer = view.findViewById(R.id.chat_list_container);

        ChatIconButton chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.red);
        chatIconButton.setName("Yuri Chervonyi");
        chatListContainer.addView(chatIconButton);


        chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.blue);
        chatIconButton.setName("Jon Snow");
        chatListContainer.addView(chatIconButton);

        chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.orange);
        chatIconButton.setName("Dak Noise");
        chatListContainer.addView(chatIconButton);

        chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.green);
        chatIconButton.setName("Tark Morbund");
        chatListContainer.addView(chatIconButton);

        chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.violet);
        chatIconButton.setName("Sansa Snow");
        chatListContainer.addView(chatIconButton);

        chatIconButton = new ChatIconButton(getContext());
        chatIconButton.setBackgroundColor(R.color.yellow);
        chatIconButton.setName("Khal Drogo");
        chatListContainer.addView(chatIconButton);

        return view;
    }
}
