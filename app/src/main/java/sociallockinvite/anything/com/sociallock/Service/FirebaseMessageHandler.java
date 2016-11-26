package sociallockinvite.anything.com.sociallock.Service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import sociallockinvite.anything.com.sociallock.View.LockRequest;

public class FirebaseMessageHandler extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessageHandler.class.getCanonicalName();

    public FirebaseMessageHandler() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        startLockRequestActivity();
    }

    private void startLockRequestActivity() {
        Intent intent = new Intent(this, LockRequest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        EventParcelable eventParcelable = new EventParcelable(event);
//        intent.putExtra(Constants.EVENT_OBJECT, eventParcelable);
//        intent.putExtra(Constants.EVENT_KEY, key);
        startActivity(intent);
    }
}
