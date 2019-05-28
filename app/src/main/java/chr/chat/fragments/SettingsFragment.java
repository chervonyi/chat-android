package chr.chat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import chr.chat.R;
import chr.chat.activities.ChangeInfoActivity;
import chr.chat.components.GlobalSettings;
import chr.chat.views.ChatEditText;

public class SettingsFragment extends Fragment implements  CompoundButton.OnCheckedChangeListener {

    Switch switchNotificationNewChat;
    Switch switchBlurImages;
    Switch switchAdultContent;
    Switch switchDarkMode;
    Switch switchBotMessages;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        context = getContext();

        switchNotificationNewChat = view.findViewById(R.id.switch_notification);
        switchBlurImages = view.findViewById(R.id.switch_blur);
        switchAdultContent = view.findViewById(R.id.switch_adult_content);
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        switchBotMessages = view.findViewById(R.id.switch_bot_message);

        // Update check status from SharedPreferences
        switchNotificationNewChat.setChecked(GlobalSettings.isChecked(getContext(), GlobalSettings.NOTIFICATION_NEW_CHAT));
        switchBlurImages.setChecked(GlobalSettings.isChecked(getContext(), GlobalSettings.BLUR_IMAGES));
        switchAdultContent.setChecked(GlobalSettings.isChecked(getContext(), GlobalSettings.CHECK_ON_ADULT_CONTENT));
        switchDarkMode.setChecked(GlobalSettings.isChecked(getContext(), GlobalSettings.DARK_MODE));
        switchBotMessages.setChecked(GlobalSettings.isChecked(getContext(), GlobalSettings.BOT_MESSAGES));

        // Set listeners on change
        switchNotificationNewChat.setOnCheckedChangeListener(this);
        switchBlurImages.setOnCheckedChangeListener(this);
        switchAdultContent.setOnCheckedChangeListener(this);
        switchDarkMode.setOnCheckedChangeListener(this);
        switchBotMessages.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isMatch(switchNotificationNewChat, buttonView)) {
            GlobalSettings.setCheck(context, GlobalSettings.NOTIFICATION_NEW_CHAT, isChecked);
        } else if (isMatch(switchBlurImages, buttonView)) {
            GlobalSettings.setCheck(context, GlobalSettings.BLUR_IMAGES, isChecked);
        } else if (isMatch(switchAdultContent, buttonView)) {
            GlobalSettings.setCheck(context, GlobalSettings.CHECK_ON_ADULT_CONTENT, isChecked);
        } else if (isMatch(switchDarkMode, buttonView)) {
            GlobalSettings.setCheck(context, GlobalSettings.DARK_MODE, isChecked);
        } else if (isMatch(switchBotMessages, buttonView)) {
            GlobalSettings.setCheck(context, GlobalSettings.BOT_MESSAGES, isChecked);
        }
    }

    private boolean isMatch(Switch switcher, CompoundButton buttonView) {
        return switcher != null && switcher.getId() == buttonView.getId();
    }

}
