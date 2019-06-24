package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendshipServiceTests {

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
        user.setUsername("test3");

        User friend = new User();
        friend.setUsername("test1");

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
    public void requestAccept() throws Exception {
        User user = new User();
        user.setUsername("test3");

        User friend = new User();
        friend.setUsername("test1");

        Friendship friendshipRequested = new Friendship();
        friendshipRequested.setUser(user);
        friendshipRequested.setFriend(friend);
        friendshipRequested.setStatus(FriendshipStatus.REQUESTED);
        Mockito.when(this.friendshipRepository.findByUserUsernameAndFriendUsername(user.getUsername(), friend.getUsername()))
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

}
