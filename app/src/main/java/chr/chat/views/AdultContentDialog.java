package chr.chat.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.Objects;

import chr.chat.R;
import chr.chat.activities.MainActivity;

public class AdultContentDialog extends Dialog {

    private MainActivity activity;
    private final Context context;

    // UI
    private Button buttonYes;
    private Button buttonNo;

    public AdultContentDialog(MainActivity activity, Context context) {
        super(context);

        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.adult_content_warning);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        buttonYes = findViewById(R.id.adult_content_warning_yes);
        buttonNo = findViewById(R.id.adult_content_warning_no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).adultContentDialogClick(true);
                dismiss();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).adultContentDialogClick(false);
                dismiss();
            }
        });
    }
}
