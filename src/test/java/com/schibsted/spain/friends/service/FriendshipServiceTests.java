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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        User test = new User();
        test.setUsername("test1");
        test.setPassword("pass");

        Mockito.when(friendshipRepository.findAllByUserUsername(test.getUsername())).thenReturn(new ArrayList<>());

        User test2 = new User();
        test2.setUsername("test2");
        test2.setPassword("pass");

        Friendship friendship = new Friendship();
        friendship.setUser(test2);
        friendship.setFriend(test);
        friendship.setStatus(FriendshipStatus.REQUESTED);

        Mockito.when(friendshipRepository.findAllByUserUsername(test2.getUsername())).thenReturn(Arrays.asList(friendship));
    }

    @Test
    public void listEmptyFriendshipForUser() {
        List<Friendship> friendshipList = this.friendshipService.listFriendship("test1");

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(0, friendshipList.size());
    }

    @Test
    public void listNotEmptyFriendshipForUser() {
        List<Friendship> friendshipList = this.friendshipService.listFriendship("test2");

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(1, friendshipList.size());
        Assert.assertEquals("test1", friendshipList.get(0).getFriend().getUsername());
        Assert.assertEquals(FriendshipStatus.REQUESTED, friendshipList.get(0).getStatus());
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
        friendship.setUser(user);
        friendship.setFriend(friend);
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
