package chr.chat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import chr.chat.components.BotAttachments;
import chr.chat.R;
import chr.chat.activities.MainActivity;

public class ChatPopupAttachMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

    private Context context;

    public ChatPopupAttachMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
        setOnMenuItemClickListener(this);
        this.context = context;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attach_topic:
                ((MainActivity)context).attachBotMessage(BotAttachments.ATTACHMENTS_TOPIC);
                return true;

            case R.id.attach_question:
                ((MainActivity)context).attachBotMessage(BotAttachments.ATTACHMENTS_QUESTION);
                return true;

            case R.id.attach_question_for_couple:
                ((MainActivity)context).attachBotMessage(BotAttachments.ATTACHMENTS_QUESTION_COUPLE);
                return true;
        }
        return false;
    }
}
