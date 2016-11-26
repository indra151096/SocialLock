package sociallockinvite.anything.com.sociallock.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import sociallockinvite.anything.com.sociallock.DAL.GroupDAL;
import sociallockinvite.anything.com.sociallock.DAL.UserDAL;
import sociallockinvite.anything.com.sociallock.Interface.DashboardSwitcherListener;
import sociallockinvite.anything.com.sociallock.R;

public class GroupAddMember extends Fragment {
    private UserDAL mUserDAL;
    private GroupDAL mGroupDAL;

    private EditText mTxtEmail;

    private DashboardSwitcherListener mCallback;

    public GroupAddMember() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserDAL = new UserDAL();
        mGroupDAL = new GroupDAL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_group_add_member, container, false);
        final View btnAddMember = view.findViewById(R.id.btnAddMember);
        final View btnSignOut = view.findViewById(R.id.btnSignOut);
        final View btnDashboard = view.findViewById(R.id.btnDashboard);

        mTxtEmail = ((EditText) view.findViewById(R.id.txtEmail));

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupDAL.addMemberToGroup(mTxtEmail.getText().toString());
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDAL.userSignOut();
            }
        });

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.OnBtnDashboardPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (DashboardSwitcherListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DashboardSwitcherListener");
        }
    }
}
