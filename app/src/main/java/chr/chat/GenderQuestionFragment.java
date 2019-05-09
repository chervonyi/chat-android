package chr.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenderQuestionFragment extends Fragment implements View.OnClickListener {

    private int selection;

    private List<Button> answerButtons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question_gender, container, false);

        // Selection by default
        selection = R.id.button_answer_anybody;

        // Highlight appropriate button
        setHighlight(view.findViewById(selection), R.drawable.highlighted_button, R.color.white);

        answerButtons.add((Button)view.findViewById(R.id.button_answer_man));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_woman));
        answerButtons.add((Button)view.findViewById(R.id.button_answer_anybody));

        // Connect listeners
        view.findViewById(R.id.button_answer_man).setOnClickListener(this);
        view.findViewById(R.id.button_answer_woman).setOnClickListener(this);
        view.findViewById(R.id.button_answer_anybody).setOnClickListener(this);

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
