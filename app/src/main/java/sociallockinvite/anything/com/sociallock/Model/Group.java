package sociallockinvite.anything.com.sociallock.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Group POJO class
 *
 * Copyright (C) 2016 Zhen Zhi Lee
 * Written by Zhen Zhi Lee (leezhenzhi@gmail.com)
 *
 * POJO class for Group
 */

public class Group {
    private String id;
    private String host;
    private Map<String, Boolean> members = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    public void addMember(User user) {
        if (user.getId().isEmpty()) {
            throw new RuntimeException("User ID is empty");
        }
        members.put(user.getId(), true);
    }

    public void removeMember(User user) {
        if (user.getId().isEmpty()) {
            throw new RuntimeException("User ID is empty");
        }
        members.remove(user.getId());
    }
}
