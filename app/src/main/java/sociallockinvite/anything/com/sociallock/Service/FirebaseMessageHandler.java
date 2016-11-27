package sociallockinvite.anything.com.sociallock.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import sociallockinvite.anything.com.sociallock.Common.Constants;
import sociallockinvite.anything.com.sociallock.View.LockRequest;
import sociallockinvite.anything.com.sociallock.View.MemberUnlockRequest;
import sociallockinvite.anything.com.sociallock.View.UnlockRequest;
import sociallockinvite.anything.com.sociallock.integrated.scan;

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

        final String[] message = remoteMessage.getNotification().getBody().split(",");
        final String groupId = remoteMessage.getFrom().substring(8);
        final String operation = message[0];
        final String email = message[1];
        final String hostId = message[2];

        Log.d(TAG, "Operation: \"" + operation + "\"");

        SharedPreferences sharedpreferences = getSharedPreferences("social", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("groupId", groupId);
        editor.putString("email", email);
        editor.putString("hostId", hostId);
        editor.apply();

        switch (operation) {
            case Constants.HOST_TO_GROUP_LOCK_BROADCAST:
                startLockRequestActivity();
                break;
            case Constants.HOST_TO_GROUP_UNLOCK_BROADCAST:
                stopService(new Intent(this, scan.class));
                break;
            case Constants.MEMBER_TO_HOST_UNLOCK_REQUEST:
                startMemberUnlockRequestActivity();
                break;
            case Constants.HOST_TO_MEMBER_UNLOCK_REPLY_YES:
                stopService(new Intent(this, scan.class));
                break;
        }
    }

    private void startLockRequestActivity() {
        Intent intent = new Intent(this, LockRequest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startUnlockRequestActivity() {
        Intent intent = new Intent(this, UnlockRequest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void startMemberUnlockRequestActivity() {
        Intent intent = new Intent(this, MemberUnlockRequest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
