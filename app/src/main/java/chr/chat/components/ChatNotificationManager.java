package chr.chat.components;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    
    public ChatNotificationManager(Context context) {
        createNotificationChannel(context);

        builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANEL_ID);
        builder.setSmallIcon(R.drawable.ic_chat_notification);
        builder.setColor(context.getResources().getColor(R.color.accent));
        builder.setContentTitle(context.getString(R.string.notification_title));
        builder.setContentText(context.getString(R.string.notification_body));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
    }

    public void show(Context context, String newChatID) {

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.NEW_CHAT_FROM_NOTIFICATION, newChatID);

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

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, "R-Chat", importance);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
