package chr.chat.components;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import chr.chat.R;
import chr.chat.activities.MainActivity;

public class ChatNotificationManager {

    private final String NOTIFICATION_CHANEL_ID = "chr-chat";

    private final AtomicInteger uniqueID = new AtomicInteger(0);

    private NotificationCompat.Builder builder;

    // Prepare Notification in constructor
    public ChatNotificationManager(Context context) {
        createNotificationChannel(context);

        builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_chat_notification);
        builder.setColor(context.getResources().getColor(R.color.accent));
        builder.setContentTitle(context.getString(R.string.notification_title));
        builder.setContentText(context.getString(R.string.notification_body));
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
    }

    /**
     * Show notification which has been prepared before
     * @see ChatNotificationManager#ChatNotificationManager(Context)
     * @param newChatID ID of created chat which must be replaced in extra of new Intent
     *                  to open appropriate chat by click on notification
     */
    public void show(Context context, String newChatID) {

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(MainActivity.DESIRABLE_CHAT_ID, newChatID);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(getUniqueNotificationID(), builder.build());
    }

    private int getUniqueNotificationID() {
        return uniqueID.incrementAndGet();
    }

    /**
     * Required method for Android 5.0+
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, "R-Chat", importance);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
