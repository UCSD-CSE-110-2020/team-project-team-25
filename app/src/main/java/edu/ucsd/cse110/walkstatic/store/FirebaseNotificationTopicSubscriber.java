package edu.ucsd.cse110.walkstatic.store;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import edu.ucsd.cse110.walkstatic.MainActivity;

public class FirebaseNotificationTopicSubscriber implements NotificationTopicSubscriber {
    private static final String TAG = "FirebaseNotificationTopicSubscriber";
    @Override
    public void subscribeToNotificationTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                        }
                );
    }
}
