package com.schibsted.spain.friends.service.comparator;

import com.schibsted.spain.friends.domain.Friendship;

import java.util.Comparator;

/**
 * Used for sorting friendships order by last modification
 */
public class FriendshipComparator implements Comparator<Friendship> {
    @Override
    public int compare(Friendship o1, Friendship o2) {
        if(o1 != null && o2 != null){
            return o1.getLastModifiedDate().compareTo(o2.getLastModifiedDate());
        }

        return 0;
    }
}
