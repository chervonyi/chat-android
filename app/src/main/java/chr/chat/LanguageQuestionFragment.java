package chr.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LanguageQuestionFragment extends Fragment implements View.OnClickListener {

    private List<Button> answerButtons = new ArrayList<>();

    private int selection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_question_language, container, false);

        // Selection by default
        selection = R.id.button_answer_english;

        // Highlight appropriate button
        setHighlight(view.findViewById(R.id.button_answer_english), R.drawable.highlighted_button, R.color.white);

        answerButtons.add((Button)view.findViewById(R.id.button_answer_english));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_spanish));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_german));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_french));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_russian));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_ukrainian));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_other));

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
                String answer = ((Button)view.findViewById(selection)).getText().toString();
                ((SearchActivity) Objects.requireNonNull(getActivity())).startSearching(answer);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        for (Button button : answerButtons) {
            if (button.getId() == v.getId()) {
                setHighlight(button, R.drawable.highlighted_button, R.color.white);
            } else {
                setHighlight(button, R.drawable.button, R.color.gray);
            }
        }

        selection = v.getId();
    }

    private void setHighlight(View view, int background, int fontColor) {
        view.setBackground(getResources().getDrawable(background));
        ((Button)view).setTextColor(getResources().getColor(fontColor));
    }
}
