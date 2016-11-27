package sociallockinvite.anything.com.sociallock.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import sociallockinvite.anything.com.sociallock.DAL.UserDAL;

public class FirebaseTokenHandler extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseTokenHandler.class.getCanonicalName();

    private UserDAL mUserDAL;

    public FirebaseTokenHandler() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mUserDAL = new UserDAL();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        if (mUserDAL.isSignedIn()) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        }
    }

    void sendRegistrationToServer(String token) {
        mUserDAL.updateFCMToken(token);
    }
}
