package sociallockinvite.anything.com.sociallock.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sociallockinvite.anything.com.sociallock.Interface.DashboardSwitcherListener;
import sociallockinvite.anything.com.sociallock.R;

public class GroupDashboard extends Fragment {
    private DashboardSwitcherListener mCallback;

    public GroupDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_group_dashboard, container, false);
        final View btnLock = view.findViewById(R.id.btnLock);
        final View btnUnlock = view.findViewById(R.id.btnUnlock);
        final View btnAddMember = view.findViewById(R.id.btnAddMember);

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.OnBtnAddMemberPressed();
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
