package sociallockinvite.anything.com.sociallock.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import sociallockinvite.anything.com.sociallock.R;

public class active extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs

        // toast
        Toast.makeText(this, "Uh oh! Press the unlock button, m8.", Toast.LENGTH_SHORT).show();
    }
}
