package sociallockinvite.anything.com.sociallock.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import sociallockinvite.anything.com.sociallock.R;
import java.security.SecureRandom;
import java.math.BigInteger;

public class hostLock extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_lock);

        txt = (TextView) findViewById(R.id.uniqueCode);
        txt.setText(nextSessionId());

    }

        private SecureRandom random = new SecureRandom();

        public String nextSessionId() {
            return new BigInteger(32, random).toString(16);
        }

}
