package com.schibsted.spain.friends.domain;

import java.io.Serializable;

public class FriendshipId implements Serializable {

    private String user;

    private String friend;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
