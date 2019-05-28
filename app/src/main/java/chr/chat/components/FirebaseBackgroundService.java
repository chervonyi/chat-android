package chr.chat.components;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import chr.chat.components.models.Chat;

public class FirebaseBackgroundService extends Service {

    private ChatNotificationManager notificationManager;
    private Context context;
    private DatabaseReference mDatabase;
    private String userID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        userID = UniqueIdentifier.readIdentifier(context);

        boolean isNotificationAllowed = GlobalSettings.isChecked(context,
                GlobalSettings.NOTIFICATION_NEW_CHAT);

        if (!isNotificationAllowed) {
            return START_STICKY;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        notificationManager = new ChatNotificationManager(context);

        mDatabase.child("chats").orderByChild("userID2").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Chat chat;

                for (DataSnapshot chatNode: dataSnapshot.getChildren()) {

                    chat = chatNode.getValue(Chat.class);
                    String chatID = chatNode.getKey();

                    if (chat != null && chatID != null) {
                        if (!chat.isNotified()) {

                            // Assign 'true' for current chatID for 'notified' state
                            mDatabase.child("chats").child(chatID).child("notified").setValue(true);

                            // Do notification
                            notificationManager.show(context, chatID);

                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
