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

public class LanguageQuestionFragment extends Fragment implements View.OnClickListener {

    private List<ChatButton> answerButtons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_question_language, container, false);

        // Selection by default
        // Highlight appropriate button
        ((ChatButton)view.findViewById(R.id.button_answer_english)).setHighlight(true);

        // Connect all buttons
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_english));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_spanish));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_german));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_french));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_russian));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_ukrainian));
        answerButtons.add((ChatButton)view.findViewById(R.id.button_answer_other));

        // Set clickListeners
        view.findViewById(R.id.button_answer_english).setOnClickListener(this);
        view.findViewById(R.id.button_answer_spanish).setOnClickListener(this);
        view.findViewById(R.id.button_answer_german).setOnClickListener(this);
        view.findViewById(R.id.button_answer_french).setOnClickListener(this);
        view.findViewById(R.id.button_answer_russian).setOnClickListener(this);
        view.findViewById(R.id.button_answer_ukrainian).setOnClickListener(this);
        view.findViewById(R.id.button_answer_other).setOnClickListener(this);

        view.findViewById(R.id.button_start_searching).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = "other";

                for (ChatButton button : answerButtons) {
                    if (button.isHighlighted()) {
                        answer = button.getText().toString();
                        break;
                    }
                }

                // Go to another fragment to start searching for a new chat
                ((SearchActivity) Objects.requireNonNull(getActivity())).startSearching(answer);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        // Set highlight for a selected button and remove it for others
        for (ChatButton button : answerButtons) {
            button.setHighlight(button.getId() == v.getId());
        }
    }
}
