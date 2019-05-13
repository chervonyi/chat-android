package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;

import chr.chat.R;

@SuppressLint("AppCompatCustomView")
public class ChatIconButton extends Button {

    private Context context;

    public ChatIconButton(Context context) {
        super(context);
        this.context = context;
        render();
    }

    public ChatIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        render();
    }

    public ChatIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        render();
    }

    private void render() {

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                getResources().getDisplayMetrics());
        int divider = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(0, 0, divider, 0);
        setLayoutParams(params);


        setBackground(getResources().getDrawable(R.drawable.chat_icon_red));
        setTextColor(getResources().getColor(R.color.white));
        setStateListAnimator(null);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        setText("YC");

        // TODO change text
    }
}
