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
import android.widget.TextView;

import chr.chat.R;
import chr.chat.activities.ChangeInfoActivity;
import chr.chat.views.ChatEditText;

public class InputNameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_input_name, container, false);

        final ChatEditText editText = view.findViewById(R.id.editTextName);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int leftCharacters = ChangeInfoActivity.MAX_LENGTH - editText.getText().length();

                // Update textView with amount of available characters
                ((TextView)view.findViewById(R.id.textViewLeftCharacters)).setText(String.valueOf(leftCharacters));
            }
        });

        return view;
    }
}
