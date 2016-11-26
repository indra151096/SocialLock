package sociallockinvite.anything.com.sociallock.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sociallockinvite.anything.com.sociallock.DAL.UserDAL;
import sociallockinvite.anything.com.sociallock.Interface.DashboardSwitcherListener;
import sociallockinvite.anything.com.sociallock.R;

public class MainActivity extends AppCompatActivity implements DashboardSwitcherListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private UserDAL mUserDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frgMain, new UserSignUp()).commit();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frgMain, new GroupAddMember());
                    transaction.commit();
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frgMain, new UserSignUp());
                    transaction.commit();
                }
            }
        };

        mUserDAL = new UserDAL();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mUserDAL.subscribeToTopic();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void OnBtnDashboardPressed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frgMain, new GroupDashboard());
        transaction.commit();
    }

    @Override
    public void OnBtnAddMemberPressed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frgMain, new GroupAddMember());
        transaction.commit();
    }
}
