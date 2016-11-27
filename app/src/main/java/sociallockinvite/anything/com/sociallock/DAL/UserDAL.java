package sociallockinvite.anything.com.sociallock.DAL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sociallockinvite.anything.com.sociallock.Common.Constants;
import sociallockinvite.anything.com.sociallock.Interface.SocialLockService;
import sociallockinvite.anything.com.sociallock.Model.Group;
import sociallockinvite.anything.com.sociallock.Model.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * User service provider
 * <p>
 * Copyright (C) 2016 Zhen Zhi Lee
 * Written by Zhen Zhi Lee (leezhenzhi@gmail.com)
 * <p>
 * Handles all user CRUD using Firebase as the backend
 */

public class UserDAL {
    private static final String TAG = UserDAL.class.getCanonicalName();

    private FirebaseAuth mAuth;
    private DatabaseReference mUsers;
    private DatabaseReference mGroups;
    private DatabaseReference mUsersIndex;

    public UserDAL() {
        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference("users");
        mGroups = FirebaseDatabase.getInstance().getReference("groups");
        mUsersIndex = FirebaseDatabase.getInstance().getReference("usersIndex");
    }

    public void createNewUser(final String email, final String password, final Activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                            String fcmToken = FirebaseInstanceId.getInstance().getToken();

                            if (userAuth != null) {
                                DatabaseReference userRef = mUsers.child(userAuth.getUid());
                                DatabaseReference groupRef = mGroups.push();
                                Group group = new Group();
                                User user = new User();

                                group.setId(groupRef.getKey());
                                user.setId(userRef.getKey());

                                user.setEmail(email);
                                user.setGroup(group.getId());
                                user.addGroupsIn(group);

                                if (fcmToken != null) {
                                    user.setFcmToken(fcmToken);
                                }

                                group.setHost(user.getId());
                                group.addMember(user);

                                userRef.setValue(user);
                                groupRef.setValue(group);

                                mUsersIndex.child(user.getEmail()).setValue(user.getId());
                            }
                        } else {
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userSignIn(final String email, final String password, final Activity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String fcmToken = FirebaseInstanceId.getInstance().getToken();

                            if (fcmToken != null) {
                                updateFCMToken(fcmToken);
                            }
                        }
                    }
                });
    }

    public void userSignOut() {
        mAuth.signOut();
    }

    public void updateFCMToken(final String fcmToken) {
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            final String userId = userAuth.getUid();
            DatabaseReference userRef = mUsers.child(userId);

            userRef.child("fcmToken").setValue(fcmToken);
        }
    }

    public Boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void subscribeToTopic() {
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            final String userId = userAuth.getUid();
            DatabaseReference userRef = mUsers.child(userId);

            userRef.child("groupsIn").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, Boolean>> t = new GenericTypeIndicator<Map<String, Boolean>>() {
                    };
                    Map<String, Boolean> groups = dataSnapshot.getValue(t);

                    if (groups == null) return;

                    for (Map.Entry<String, Boolean> entry : groups.entrySet()) {
                        Log.d(TAG, "Subscribing to " + entry.getKey());
                        FirebaseMessaging.getInstance().subscribeToTopic(entry.getKey());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void memberToHostUnlockRequest(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://indra151096.com/")
                .build();

        final SocialLockService service = retrofit.create(SocialLockService.class);

        SharedPreferences social = context.getSharedPreferences("social", MODE_PRIVATE);
        final String groupId = social.getString("groupId", "");

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            final String userId = userAuth.getUid();

            mGroups.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String hostId = dataSnapshot.child("host").getValue(String.class);

                    mUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String myEmail = dataSnapshot.child("email").getValue(String.class);
                            final String message = Constants.MEMBER_TO_HOST_UNLOCK_REQUEST + "," + myEmail + "," + hostId;

                            mUsers.child(hostId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final String hostToken = dataSnapshot.child("fcmToken").getValue(String.class);

                                            Call<ResponseBody> result = service.replyLockRequest(hostToken, message);

                                            result.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Log.d(TAG, "Sent HTTP POST request to " + hostToken);
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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void hostToMemberUnlockReply(final boolean yes, Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://indra151096.com/")
                .build();

        final SocialLockService service = retrofit.create(SocialLockService.class);

        SharedPreferences social = context.getSharedPreferences("social", MODE_PRIVATE);
        final String recipientEmail = social.getString("email", "");

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            final String userId = userAuth.getUid();

            mUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String myEmail = dataSnapshot.child("email").getValue(String.class);
                    final String message = (yes
                            ? Constants.HOST_TO_MEMBER_UNLOCK_REPLY_YES
                            : Constants.HOST_TO_MEMBER_UNLOCK_REPLY_NO) + "," + myEmail + "," + userId;

                    mUsersIndex.child(recipientEmail.replace(".", "%20"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String recipientId = dataSnapshot.getValue(String.class);

                                    mUsers.child(recipientId)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final String recipientToken = dataSnapshot.child("fcmToken").getValue(String.class);

                                                    Call<ResponseBody> result = service.replyLockRequest(recipientToken, message);

                                                    result.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            if (response.isSuccessful()) {
                                                                Log.d(TAG, "Sent HTTP POST request to " + recipientToken);
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

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean amIHost(Context context) {
        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        if (userAuth != null) {
            SharedPreferences social = context.getSharedPreferences("social", MODE_PRIVATE);
            final String userId = userAuth.getUid();
            final String hostId = social.getString("hostId", "");
            return userId.equals(hostId);
        }

        return false;
    }
}
