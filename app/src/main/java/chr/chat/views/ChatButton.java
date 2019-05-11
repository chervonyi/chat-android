package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import chr.chat.R;

@SuppressLint("AppCompatCustomView")
public class ChatButton extends Button {

    private Context context;

    private boolean isHighlighted = false;

    public ChatButton(Context context) {
        super(context);

        this.context = context;
        renderButton();
    }

    public ChatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        renderButton();
    }

    public ChatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        renderButton();
    }

    @SuppressLint("ResourceAsColor")
    public void renderButton() {
        setBackground(context.getResources().getDrawable(R.drawable.button));
        setPadding(0,0,0,1);
        setTextColor(context.getResources().getColor(R.color.gray));
        setStateListAnimator(null);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        setAllCaps(false);
    }

    public void setHighlight(boolean newValue) {
        isHighlighted = newValue;

        if (isHighlighted) {
            setHighlight(R.drawable.highlighted_button, R.color.white);
        } else {
            setHighlight(R.drawable.button, R.color.gray);
        }
    }

    public void setHighlight(int background, int color) {
        setBackground(context.getResources().getDrawable(background));
        setTextColor(context.getResources().getColor(color));
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }
}
