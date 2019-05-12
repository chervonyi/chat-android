package chr.chat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import chr.chat.views.ChatButton;
import chr.chat.R;
import chr.chat.activities.SearchActivity;

public class GenderQuestionFragment extends Fragment implements View.OnClickListener {

    private List<ChatButton> answerButtons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question_gender, container, false);

        // Selection by default (Highlight appropriate button)
        ((ChatButton)view.findViewById(R.id.button_answer_anybody)).setHighlight(true);

        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_man));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_woman));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_anybody));

        // Connect listeners
        view.findViewById(R.id.button_answer_man).setOnClickListener(this);
        view.findViewById(R.id.button_answer_woman).setOnClickListener(this);
        view.findViewById(R.id.button_answer_anybody).setOnClickListener(this);

        view.findViewById(R.id.button_next_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String answer = "anybody";

                for (ChatButton button : answerButtons) {
                    if (button.isHighlighted()) {
                        answer = button.getText().toString();
                        break;
                    }
                }

                ((SearchActivity) Objects.requireNonNull(getActivity())).answeredOnGender(answer);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        // Highlight selected button and remove highlighting for others
        for (ChatButton button : answerButtons) {
            button.setHighlight(button.getId() == v.getId());
        }
    }
}
