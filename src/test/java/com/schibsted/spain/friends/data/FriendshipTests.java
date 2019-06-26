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
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass");

        Friendship registeredFriendship = new Friendship(user1, user2, FriendshipStatus.ACCEPTED);
        entityManager.persist(registeredFriendship);
        entityManager.flush();

        List<Friendship> friendshipList = this.friendshipRepository.findAllByUserAndStatus(user1, FriendshipStatus.ACCEPTED);

        Assert.assertEquals(1, friendshipList.size());
    }

    @Test
    public void findFriendshipByFriend() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass");

        Friendship registeredFriendship = new Friendship(user1, user2, FriendshipStatus.ACCEPTED);
        entityManager.persist(registeredFriendship);
        entityManager.flush();

        List<Friendship> friendshipList = this.friendshipRepository.findAllByUserAndStatus(user2, FriendshipStatus.ACCEPTED);

        Assert.assertEquals(1, friendshipList.size());
    }
}
