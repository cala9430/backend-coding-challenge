package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
public class FriendshipServiceTests {

    @TestConfiguration
    static class FriendshipServiceImplTestContextConfiguration {

        @Bean
        public FriendshipService friendshipService() {
            return new FriendshipService();
        }
    }


    @Autowired
    private FriendshipService friendshipService;

    @MockBean
    private FriendshipRepository friendshipRepository;

    @Before
    public void setUp() throws Exception {
        User user = new User();
        user.setUsername("test1");
        user.setPassword("pass");

        Mockito.when(friendshipRepository.findAllByUserAndStatus(user, FriendshipStatus.ACCEPTED)).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setUsername("test2");
        user2.setPassword("pass");

        Friendship friendship = new Friendship();
        friendship.setUser(user2);
        friendship.setFriend(user);
        friendship.setLastModifiedDate(new Date());
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        User user3 = new User();
        user3.setUsername("test3");
        user3.setPassword("pass");

        Friendship friendship2 = new Friendship();
        friendship2.setUser(user3);
        friendship2.setFriend(user);
        friendship2.setLastModifiedDate(new Date());
        friendship2.setStatus(FriendshipStatus.ACCEPTED);

        Mockito.when(friendshipRepository.findAllByUserAndStatus(user2, FriendshipStatus.ACCEPTED)).thenReturn(Arrays.asList(friendship));
        Mockito.when(friendshipRepository.findAllByUserAndStatus(user,FriendshipStatus.ACCEPTED)).thenReturn(Arrays.asList(friendship, friendship2));
    }

    @Test
    public void listEmptyFriendshipForUser() {
        User user = new User();
        user.setUsername("noFriendsUser");
        List<String> friendshipList = this.friendshipService.listAcceptedFriendshipUsers(user);

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(0, friendshipList.size());
    }

    @Test
    public void listNotEmptyFriendshipForUser() {
        User user = new User();
        user.setUsername("test2");
        List<String> friendshipList = this.friendshipService.listAcceptedFriendshipUsers(user);

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(1, friendshipList.size());
        Assert.assertTrue( friendshipList.contains("test1"));
    }

    @Test
    public void listFriendshipOrderTest() {
        User user = new User();
        user.setUsername("test1");
        List<String> friendshipList = this.friendshipService.listAcceptedFriendshipUsers(user);

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(2, friendshipList.size());
        Assert.assertEquals( "test2", friendshipList.get(0));
        Assert.assertEquals( "test3", friendshipList.get(1));

    }

    @Test
    public void requestFriendship(){
        User user = new User();
        user.setUsername("requester");

        User friend = new User();
        friend.setUsername("requested");

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.REQUESTED);

        Mockito.when(this.friendshipRepository.save(friendship)).thenReturn(friendship);

        Friendship savedFriendship = this.friendshipService.requestFriendship(user, friend);

        Assert.assertNotNull(savedFriendship);
        Assert.assertEquals(friendship, savedFriendship);
    }

    @Test
    public void requestAccept(){
        User user = new User();
        user.setUsername("requested");

        User friend = new User();
        friend.setUsername("requester");

        Friendship friendshipRequested = new Friendship();
        friendshipRequested.setUser(friend);
        friendshipRequested.setFriend(user);
        friendshipRequested.setStatus(FriendshipStatus.REQUESTED);
        Mockito.when(this.friendshipRepository.findByUserAndFriend(friend, user))
                .thenReturn(Optional.of(friendshipRequested));

        Friendship friendship = new Friendship();
        friendship.setUser(friend);
        friendship.setFriend(user);
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        Mockito.when(this.friendshipRepository.save(friendship))
                .thenReturn(friendship);

        Friendship savedFriendship = this.friendshipService.acceptFriendship(user, friend);

        Assert.assertNotNull(savedFriendship);
        Assert.assertEquals(friendship, savedFriendship);
    }

    @Test(expected = InvalidFriendshipStatus.class)
    public void requestAcceptNotRequested(){
        User user = new User();
        user.setUsername("requested");

        User friend = new User();
        friend.setUsername("requester");

        this.friendshipService.acceptFriendship(user, friend);
    }

    @Test(expected = InvalidFriendshipStatus.class)
    public void requestAcceptAlreadyFriends(){
        User user = new User();
        user.setUsername("requested");

        User friend = new User();
        friend.setUsername("requester");

        Friendship friendshipRequested = new Friendship();
        friendshipRequested.setUser(friend);
        friendshipRequested.setFriend(user);
        friendshipRequested.setStatus(FriendshipStatus.ACCEPTED);
        Mockito.when(this.friendshipRepository.findByUserAndFriend(friend, user))
                .thenReturn(Optional.of(friendshipRequested));

        this.friendshipService.acceptFriendship(user, friend);
    }

    @Test(expected = InvalidFriendshipStatus.class)
    public void requestAcceptAlreadyReverseFriends(){
        User user = new User();
        user.setUsername("requested");

        User friend = new User();
        friend.setUsername("requester");

        Friendship friendshipRequested = new Friendship();
        friendshipRequested.setUser(user);
        friendshipRequested.setFriend(friend);
        friendshipRequested.setStatus(FriendshipStatus.ACCEPTED);
        Mockito.when(this.friendshipRepository.findByUserAndFriend(user, friend))
                .thenReturn(Optional.of(friendshipRequested));

        this.friendshipService.acceptFriendship(user, friend);
    }

    @Test(expected = InvalidFriendshipStatus.class)
    public void requestDeclineNotRequested(){
        User user = new User();
        user.setUsername("requested");

        User friend = new User();
        friend.setUsername("requester");

        this.friendshipService.declineFriendship(user, friend);
    }



}
