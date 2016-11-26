package sociallockinvite.anything.com.sociallock.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import sociallockinvite.anything.com.sociallock.DAL.GroupDAL;
import sociallockinvite.anything.com.sociallock.DAL.UserDAL;
import sociallockinvite.anything.com.sociallock.Interface.DashboardSwitcherListener;
import sociallockinvite.anything.com.sociallock.R;
import sociallockinvite.anything.com.sociallock.integrated.MyApplication;

public class GroupAddMember extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private UserDAL mUserDAL;
    private GroupDAL mGroupDAL;
    private Switch sFamily;

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

        sFamily = (Switch) view.findViewById(R.id.sFamily);
        sFamily.setOnCheckedChangeListener(this);

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(getActivity(), "Family Mode Started", Toast.LENGTH_SHORT).show();
            MyApplication.beaconConnect();
        }
        else
        {
            Toast.makeText(getActivity(), "Family Mode Stopped", Toast.LENGTH_SHORT).show();
            MyApplication.beaconStop();
        }
    }
}
