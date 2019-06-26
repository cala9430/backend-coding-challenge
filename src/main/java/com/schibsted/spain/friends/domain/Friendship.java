package com.schibsted.spain.friends.domain;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="friendship")
@IdClass(FriendshipId.class)
public class Friendship implements Serializable {

    public Friendship() {
        // For hibernate
    }

    public Friendship(@NotNull User user, @NotNull User friend, @NotNull FriendshipStatus status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.lastModifiedDate = new Date();
    }

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

    @LastModifiedDate
    private Date lastModifiedDate;

    public User getUser() {
        return user;
    }

    public User getFriend() {
        return friend;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
        this.lastModifiedDate = new Date();
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
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
