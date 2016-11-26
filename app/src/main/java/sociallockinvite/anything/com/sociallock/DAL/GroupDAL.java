package sociallockinvite.anything.com.sociallock.DAL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
}
