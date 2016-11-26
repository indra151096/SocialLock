package sociallockinvite.anything.com.sociallock.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sociallockinvite.anything.com.sociallock.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button, button2, button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.hostLock);
        button2.setOnClickListener(this);

        button3 = (Button) findViewById(R.id.submitUniqueCode);
        button3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == button)
        {
            if (button.getText() == "LOGIN")
                button.setText("LOGOUT");

            else
                button.setText("LOGIN");
        }

        else if (v == button2)
        {
            Intent var = new Intent(this, hostLock.class);
            startActivity(var);
        }

        else if (v == button3)
        {
            Intent var = new Intent(this, active.class);
            startActivity(var);
            startService(var);
            finish();
        }
    }
}
