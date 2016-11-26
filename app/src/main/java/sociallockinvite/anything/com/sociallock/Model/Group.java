package sociallockinvite.anything.com.sociallock.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzyzy on 26/11/2016.
 */

public class Group {
    private String name;
    private String host;
    private Map<String, Boolean> members = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addMember(String memberName)
    {
        members.put(memberName, true);
    }

    public void removeMember(String memberName)
    {
        members.remove(memberName);
    }
}
