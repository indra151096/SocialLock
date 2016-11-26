package sociallockinvite.anything.com.sociallock.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseTokenHandler extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseTokenHandler.class.getCanonicalName();

    public FirebaseTokenHandler() {
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    void sendRegistrationToServer(String token) {

    }
}
