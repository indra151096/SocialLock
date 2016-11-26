package sociallockinvite.anything.com.sociallock.Model;

/**
 * Created by zzyzy on 26/11/2016.
 */

public class User {
    private String email;
    private String password;

    public String GetEmail() {
        return email;
    }

    public void SetEmail(String email)
    {
        this.email = email;
    }

    public String GetPassword()
    {
        return password;
    }

    public void SetPassword(String password)
    {
        this.password = password;
    }
}
