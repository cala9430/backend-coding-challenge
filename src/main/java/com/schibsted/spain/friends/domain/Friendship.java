package com.schibsted.spain.friends.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name="friendship")
@IdClass(FriendshipId.class)
public class Friendship implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(referencedColumnName = "username")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(referencedColumnName = "username")
    private User friend;

    @NotEmpty
    private String status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
