package sociallockinvite.anything.com.sociallock.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * User POJO class
 *
 * Copyright (C) 2016 Zhen Zhi Lee
 * Written by Zhen Zhi Lee (leezhenzhi@gmail.com)
 *
 * POJO class for User
 */

public class User {
    private String id;
    private String email;
    private String group;
    private Map<String, Boolean> groupsIn = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.replace(".", "%20");
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Map<String, Boolean> getGroupsIn() {
        return groupsIn;
    }

    public void setGroupsIn(Map<String, Boolean> groupsIn) {
        this.groupsIn = groupsIn;
    }

    public void addGroupsIn(Group group) {
        if (group.getId().isEmpty()) {
            throw new RuntimeException("Group ID is empty");
        }
        groupsIn.put(group.getId(), true);
    }

    public void removeGroupsIn(Group group) {
        if (group.getId().isEmpty()) {
            throw new RuntimeException("Group ID is empty");
        }
        groupsIn.remove(group.getId());
    }
}
