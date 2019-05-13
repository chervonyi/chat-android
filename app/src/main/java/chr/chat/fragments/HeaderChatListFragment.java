package chr.chat.fragments;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_header_chatlist, container, false);

        chatListContainer = view.findViewById(R.id.chat_list_container);

        ChatIconButton chatIconButton = new ChatIconButton(getContext());
        chatListContainer.addView(chatIconButton);

        chatIconButton = new ChatIconButton(getContext());
        chatListContainer.addView(chatIconButton);


        return view;
    }
}
