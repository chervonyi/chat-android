package chr.chat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import chr.chat.views.ChatButton;
import chr.chat.R;
import chr.chat.activities.ChangeInfoActivity;

public class InputGenderFragment extends Fragment implements View.OnClickListener {

    private Button buttonSave;
    private ChatButton buttonMan;
    private ChatButton buttonWoman;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_input_gender, container, false);

        // Connect all views
        buttonMan = view.findViewById(R.id.button_man);
        buttonWoman = view.findViewById(R.id.button_woman);
        buttonSave = view.findViewById(R.id.button_save);

        // Set clickListeners
        view.findViewById(R.id.button_man).setOnClickListener(this);
        view.findViewById(R.id.button_woman).setOnClickListener(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String selectedGender = "man";

            if (buttonWoman.isHighlighted()) {
                selectedGender = "woman";
            }

            ((ChangeInfoActivity)getActivity()).onInputGender(selectedGender);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

        // Set highlight for selected button and remove highlighting for another one.
        buttonMan.setHighlight(buttonMan.getId() == v.getId());
        buttonWoman.setHighlight(buttonMan.getId() != v.getId());

        // Show "Save" button after the first selection
        if (buttonSave.getVisibility() != View.VISIBLE) {
            buttonSave.setVisibility(View.VISIBLE);
        }
    }
}
