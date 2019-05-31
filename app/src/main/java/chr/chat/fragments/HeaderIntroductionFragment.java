package chr.chat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import chr.chat.R;
import chr.chat.views.ChatEditText;

public class HeaderIntroductionFragment extends Fragment {

    ImageButton buttonSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_introduction, container, false);

        buttonSubmit = view.findViewById(R.id.buttonSubmitEnterName);
        return view;
    }

    public void setVisibilitySubmit(boolean status) {
        buttonSubmit.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
    }


}
