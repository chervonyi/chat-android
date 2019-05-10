package chr.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
        getBackground().mutate().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);

        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        requestFocus();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        // Show keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
