package chr.chat.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

import chr.chat.R;
import chr.chat.activities.MainActivity;

@SuppressLint("AppCompatCustomView")
public class ChatIconButton extends Button implements View.OnClickListener {

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

        setTextColor(getResources().getColor(R.color.white));
        setStateListAnimator(null);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        setOnClickListener(this);


        Button button = new Button(context);
        button.setText("MY BUTTONs");

//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
////                PopupMenu popup = new PopupMenu(wrapper, view, Gravity.END);
//
//                ChatPopupAttachMenu popupMenu = new ChatPopupAttachMenu(context, v, Gravity.END);
//                MenuInflater inflater = popupMenu.getMenuInflater();
//                inflater.inflate(R.menu.remove_chat_menu, popupMenu.getMenu());
//                popupMenu.show();
//
//                return false;
//            }
//        });
    }

    public void setName(String name) {

        String shortForm = "";

        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                shortForm += c;
            }
        }

        if (shortForm.length() == 0) {
            shortForm = String.valueOf(new Random().nextInt(98) + 1);
        } else if (shortForm.length() > 2) {
            shortForm = shortForm.substring(0, 2);
        }

        setText(shortForm);
    }

    public void setBackgroundColor(int color) {
        switch (color) {
            case R.color.blue:      setBackgroundResource(R.drawable.chat_icon_blue);   break;
            case R.color.yellow:    setBackgroundResource(R.drawable.chat_icon_yellow); break;
            case R.color.red:       setBackgroundResource(R.drawable.chat_icon_red);    break;
            case R.color.violet:    setBackgroundResource(R.drawable.chat_icon_violet); break;
            case R.color.green:     setBackgroundResource(R.drawable.chat_icon_green);  break;
            case R.color.orange:    setBackgroundResource(R.drawable.chat_icon_orange); break;
            default:                setBackgroundResource(R.drawable.chat_icon_red);    break;
        }
    }

    @Override
    public void onClick(View v) {
        ((MainActivity)context).onSelectChat(v);
    }
}
