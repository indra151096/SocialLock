package sociallockinvite.anything.com.sociallock.DAL;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sociallockinvite.anything.com.sociallock.Common.Constants;
import sociallockinvite.anything.com.sociallock.Interface.SocialLockService;
import sociallockinvite.anything.com.sociallock.Model.User;

/**
 * Group service provider
 * <p>
 * Copyright (C) 2016 Zhen Zhi Lee
 * Written by Zhen Zhi Lee (leezhenzhi@gmail.com)
 * <p>
 * Handles all group CRUD using Firebase as the backend
 */

public class GroupDAL {
    private static final String TAG = GroupDAL.class.getCanonicalName();

    private DatabaseReference mUsers;
    private DatabaseReference mGroups;
    private DatabaseReference mUsersIndex;

    public GroupDAL() {
        mUsers = FirebaseDatabase.getInstance().getReference("users");
        mGroups = FirebaseDatabase.getInstance().getReference("groups");
        mUsersIndex = FirebaseDatabase.getInstance().getReference("usersIndex");
    }

    public void addMemberToGroup(final String memberEmail) {
        mUsersIndex.child(memberEmail.replace(".", "%20"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userId = dataSnapshot.getValue(String.class);
                        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

                        if (userAuth != null) {
                            mUsers.child(userAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    String groupId = user.getGroup();
                                    DatabaseReference groupRef = mGroups.child(groupId).child("members");
                                    DatabaseReference userRef = mUsers.child(userId).child("groupsIn");

                                    groupRef.child(userId).setValue(true);
                                    userRef.child(groupId).setValue(true);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void hostToGroupLockBroadcast(final boolean lock) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://indra151096.com/")
                .build();

        final SocialLockService service = retrofit.create(SocialLockService.class);

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            final String userId = userAuth.getUid();
            DatabaseReference userRef = mUsers.child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String groupId = dataSnapshot.child("group").getValue(String.class);
                    final String hostEmail = dataSnapshot.child("email").getValue(String.class);
                    final String message = (lock
                            ? Constants.HOST_TO_GROUP_LOCK_BROADCAST
                            : Constants.HOST_TO_GROUP_UNLOCK_BROADCAST) + "," + hostEmail + "," + userId;

                    Call<ResponseBody> result = service.sendLockRequest(groupId, message);

                    result.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Sent HTTP POST request --- " + groupId);
                            } else {
                                Log.w(TAG, "Sent HTTP POST request --- not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
