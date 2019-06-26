package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.service.comparator.FriendshipComparator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FriendshipComparatorTests {
    private FriendshipComparator comparator = new FriendshipComparator();

    @Test
    public void firstBeforeTest() throws InterruptedException {
        Friendship first = new Friendship(new User(), new User(), FriendshipStatus.ACCEPTED);
        Thread.sleep(5);
        Friendship second = new Friendship(new User(), new User(), FriendshipStatus.ACCEPTED);
        Assert.assertEquals(-1, this.comparator.compare(first, second));
    }

    @Test
    public void secondBeforeTest() throws InterruptedException {
        Friendship first = new Friendship(new User(), new User(), FriendshipStatus.ACCEPTED);
        Thread.sleep(5);
        Friendship second = new Friendship(new User(), new User(), FriendshipStatus.ACCEPTED);
        Assert.assertEquals(1, this.comparator.compare(second, first));
    }

    @Test
    public void areEqualTests() {
        Friendship friendship = new Friendship(new User(), new User(), FriendshipStatus.ACCEPTED);
        Assert.assertEquals(0, this.comparator.compare(friendship, friendship));
    }

    @Test
    public void checkNullModifiedDateChecker() {
        Friendship friendship = new Friendship();
        Assert.assertEquals(0, this.comparator.compare(friendship, friendship));
    }
}
