package sociallockinvite.anything.com.sociallock.integrated;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sociallockinvite.anything.com.sociallock.DAL.GroupDAL;
import sociallockinvite.anything.com.sociallock.R;
import sociallockinvite.anything.com.sociallock.View.MainActivity;

public class ok extends AppCompatActivity implements View.OnClickListener {

    private Button bStop;
    private GroupDAL mGroupDAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);

        mGroupDAL = new GroupDAL();

        bStop = (Button) findViewById(R.id.bStop);
        bStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == bStop)
        {
            stopService(new Intent(ok.this, scan.class));
            mGroupDAL.broadcastLockRequest(true);

            finish();
        }
    }
}
