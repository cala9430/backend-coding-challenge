package com.schibsted.spain.friends.data;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class FriendshipTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Test
    public void findFriendshipByUser() {
        User user1 = new User("user1", "pass");
        User user2 = new User("user2", "pass");

        Friendship registeredFriendship = new Friendship(user1, user2, FriendshipStatus.ACCEPTED);
        entityManager.persist(registeredFriendship);
        entityManager.flush();

        List<String> friendshipList = this.friendshipRepository.findAllFriendsByUserAndStatus(user1, FriendshipStatus.ACCEPTED);

        Assert.assertEquals(1, friendshipList.size());
        Assert.assertEquals(user2.getUsername(), friendshipList.get(0));
    }

    @Test
    public void findFriendshipByFriend() {
        User user1 = new User("user1", "pass");
        User user2 = new User("user2", "pass");

        Friendship registeredFriendship = new Friendship(user1, user2, FriendshipStatus.ACCEPTED);
        entityManager.persist(registeredFriendship);
        entityManager.flush();

        List<String> friendshipList = this.friendshipRepository.findAllFriendsByUserAndStatus(user2, FriendshipStatus.ACCEPTED);

        Assert.assertNotNull(friendshipList);
        Assert.assertEquals(1, friendshipList.size());
        Assert.assertEquals(user1.getUsername(), friendshipList.get(0));
    }

    @Test
    public void findFriendshipEmpty() {
        User user1 = new User("user1", "pass");

        List<String> friendshipList = this.friendshipRepository.findAllFriendsByUserAndStatus(user1, FriendshipStatus.ACCEPTED);

        Assert.assertEquals(0, friendshipList.size());
    }
}
