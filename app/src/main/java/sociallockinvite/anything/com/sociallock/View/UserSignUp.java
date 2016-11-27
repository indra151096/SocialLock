package sociallockinvite.anything.com.sociallock.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import sociallockinvite.anything.com.sociallock.DAL.UserDAL;
import sociallockinvite.anything.com.sociallock.R;

public class UserSignUp extends Fragment {
    private UserDAL mUserDAL;

    private EditText mTxtEmail;
    private EditText mTxtPassword;

    public UserSignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserDAL = new UserDAL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_sign_up, container, false);
        final View btnSignUp = view.findViewById(R.id.btnSignUp);
        final View btnSignIn = view.findViewById(R.id.btnSignIn);

        mTxtEmail = ((EditText) view.findViewById(R.id.txtEmail));
        mTxtPassword = ((EditText) view.findViewById(R.id.txtPassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDAL.createNewUser(mTxtEmail.getText().toString(),
                        mTxtPassword.getText().toString(), getActivity());
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDAL.userSignIn(mTxtEmail.getText().toString(),
                        mTxtPassword.getText().toString(), getActivity());
            }
        });

        return view;
    }
}
