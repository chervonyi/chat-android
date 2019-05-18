package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import chr.chat.R;
import chr.chat.activities.MainActivity;

@SuppressLint("AppCompatCustomView")
public class ChatBotBlockView extends TextView {

    private Context context;

    public ChatBotBlockView(Context context) {
        super(context);
        this.context = context;
        render();
    }

    public ChatBotBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        render();
    }

    public ChatBotBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        render();
    }

    @SuppressLint("ResourceType")
    private void render() {
        setPadding(20, 20, 20, 20);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        setMaxWidth(calculateMaxWidth());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        setTypeface(null, Typeface.ITALIC);

        setTextColor(getResources().getColor(R.color.light_gray));
    }


    private int calculateMaxWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return (int) (width * 0.6);
    }
}
