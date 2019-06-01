package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import chr.chat.R;
import chr.chat.activities.MainActivity;

@SuppressLint("AppCompatCustomView")
public class ChatBlockView extends TextView {

    private boolean ownerIsUser = false;

    private Context context;

    public ChatBlockView(Context context) {
        super(context);
        this.context = context;
        render();
    }

    public ChatBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        render();
    }

    public ChatBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        render();
    }

    private void render() {
        setPadding(30, 20, 30, 20);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        setMaxWidth(calculateMaxWidth());
    }

    public void setOwner(boolean owner) {
        ownerIsUser = owner;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (ownerIsUser) {
            setStyle(R.drawable.chat_block_user, R.color.white);
            params.gravity = Gravity.RIGHT;
        } else {
            setStyle(R.drawable.chat_block_gray, R.color.chat_block_text_color_gray);
            params.gravity = Gravity.LEFT;
        }

        setLayoutParams(params);
    }

    private int calculateMaxWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return (int) (width * 0.8);
    }

    private void setStyle(int background, int color) {
        setBackground(getResources().getDrawable(background));
        setTextColor(getResources().getColor(color));
    }
}
