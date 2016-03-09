package com.eowise.recyclerview.stickyheaders.samples.data;

import java.io.Serializable;

/**
 * Created by sanjay on 12/10/2015.
 */
public class MessageToFriendsData implements Serializable

{
    private String uname;
    private String profilepic;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String userid;

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


}
