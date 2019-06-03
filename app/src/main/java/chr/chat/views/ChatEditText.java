package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import chr.chat.R;

@SuppressLint("AppCompatCustomView")
public class ChatEditText extends EditText {

    private Context context;

    public ChatEditText(Context context) {
        super(context);
        this.context = context;
        render();
    }

    public ChatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        render();
    }

    public ChatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        render();
    }

    public void render() {
        // Color for underline line
        getBackground().mutate().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);

        // Auto-focus on current EditText
        requestFocus();

        // Show keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
