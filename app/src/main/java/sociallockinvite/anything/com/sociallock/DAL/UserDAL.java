package sociallockinvite.anything.com.sociallock.DAL;

import android.app.Activity;
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

import sociallockinvite.anything.com.sociallock.Model.Group;
import sociallockinvite.anything.com.sociallock.Model.User;

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
}
