package sociallockinvite.anything.com.sociallock.integrated;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import sociallockinvite.anything.com.sociallock.DAL.GroupDAL;
import sociallockinvite.anything.com.sociallock.DAL.UserDAL;
import sociallockinvite.anything.com.sociallock.R;

public class ok extends AppCompatActivity implements View.OnClickListener {

    private Button bStop;
    private UserDAL mUserDAL;
    private GroupDAL mGroupDAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);

        mUserDAL = new UserDAL();
        mGroupDAL = new GroupDAL();

        bStop = (Button) findViewById(R.id.bStop);
        bStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == bStop)
        {
//            stopService(new Intent(this, scan.class));

            Log.d(ok.class.getCanonicalName(), mUserDAL.amIHost(this) ? "true" : "false");

            if (mUserDAL.amIHost(this)) {
                mGroupDAL.hostToGroupLockBroadcast(false);
            } else {
                mUserDAL.memberToHostUnlockRequest(this);
            }

            finish();
        }
    }
}
