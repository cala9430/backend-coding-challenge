package com.schibsted.spain.friends.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

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

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(friend, that.friend) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend, status);
    }
}
