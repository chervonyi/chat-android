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
import android.widget.LinearLayout;

import java.util.ArrayList;

import chr.chat.R;
import chr.chat.activities.MainActivity;
import chr.chat.components.UniqueIdentifier;
import chr.chat.components.models.Chat;
import chr.chat.components.models.User;
import chr.chat.views.ChatIconButton;

public class HeaderChatListFragment extends Fragment {

    private LinearLayout chatListContainer;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_header_chatlist, container, false);

        chatListContainer = view.findViewById(R.id.chat_list_container);


        ChatIconButton chatIconButton;

        for (Chat chat : ((MainActivity)getActivity()).chatList) {
            chatIconButton = new ChatIconButton(getContext());
            chatIconButton.setBackgroundColor(R.color.red);
            // TODO - set RANDOM background

            if (chat.getUserID1().equals(UniqueIdentifier.identifier)) {
                chatIconButton.setName(chat.getUserName2());
            } else {
                chatIconButton.setName(chat.getUserName1());
            }

            chatIconButton.setTag(chat.getID());

            chatListContainer.addView(chatIconButton);
        }

        return view;
    }
}
