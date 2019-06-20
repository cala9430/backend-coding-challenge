package com.schibsted.spain.friends.data;

import com.schibsted.spain.friends.domain.Friendship;
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
    public void findFriendshipByUsername() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass");

        Friendship registeredFriendship = new Friendship();
        registeredFriendship.setUser(user1);
        registeredFriendship.setFriend(user2);
        registeredFriendship.setStatus("REQUESTED");
        entityManager.persist(registeredFriendship);
        entityManager.flush();

        List<Friendship> friendshipList = this.friendshipRepository.findAllByUserUsername(user1.getUsername());

        Assert.assertEquals(1, friendshipList.size());




    }
}
