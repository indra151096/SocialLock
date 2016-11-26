package sociallockinvite.anything.com.sociallock.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import sociallockinvite.anything.com.sociallock.R;
import sociallockinvite.anything.com.sociallock.Service.GroupService;
import sociallockinvite.anything.com.sociallock.Service.UserService;

public class GroupAddMember extends Fragment {
    private UserService mUserService;
    private GroupService mGroupService;

    private EditText mTxtEmail;

    public GroupAddMember() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserService = new UserService();
        mGroupService = new GroupService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_group_add_member, container, false);
        final View btnAddMember = view.findViewById(R.id.btnAddMember);
        final View btnSignOut = view.findViewById(R.id.btnSignOut);

        mTxtEmail = ((EditText) view.findViewById(R.id.txtEmail));

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupService.addMemberToGroup(mTxtEmail.getText().toString());
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserService.userSignOut();
            }
        });

        return view;
    }
}
