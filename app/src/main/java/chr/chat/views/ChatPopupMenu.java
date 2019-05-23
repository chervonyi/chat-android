package chr.chat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import chr.chat.R;
import chr.chat.activities.MainActivity;
import chr.chat.activities.SearchActivity;

public class ChatPopupMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

    private Context context;

    public ChatPopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
        setOnMenuItemClickListener(this);
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_report_user:
                ((MainActivity)context).reportUser();
                return true;

            case R.id.menu_settings:
                if (context instanceof MainActivity) {
                    ((MainActivity)context).goToSettings();
                } else if (context instanceof SearchActivity) {
                    ((SearchActivity)context).goToSettings();
                }
                return true;

            case R.id.menu_change_name:

                if (context instanceof MainActivity) {
                    ((MainActivity)context).goToPersonalInfo();
                } else if (context instanceof SearchActivity) {
                    ((SearchActivity)context).goToPersonalInfo();
                }

                return true;
            default:
                return false;
        }
    }
}
