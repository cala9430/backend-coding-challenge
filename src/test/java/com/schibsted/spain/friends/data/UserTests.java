package com.schibsted.spain.friends.data;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class UserTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByUsername() {
        // given
        User registeredUser = new User();
        registeredUser.setUsername("test");
        registeredUser.setPassword("pass");
        entityManager.persist(registeredUser);
        entityManager.flush();
        userRepository.save(registeredUser);

        // when
        Optional<User> found = userRepository.findByUsername(registeredUser.getUsername());

        // then
        Assert.assertTrue(found.isPresent());
        User userFound = found.get();
        Assert.assertEquals(registeredUser.getUsername(), userFound.getUsername());
    }

    @Test
    public void existsUserByUsername(){
        // given
        User registeredUser = new User();
        registeredUser.setUsername("test");
        registeredUser.setPassword("pass");
        entityManager.persist(registeredUser);
        entityManager.flush();
        userRepository.save(registeredUser);

        boolean existsUser = userRepository.existsUserByUsername(registeredUser.getUsername());

        Assert.assertTrue(existsUser);

        boolean notExistsUser = userRepository.existsUserByUsername("notpresent");

        Assert.assertFalse(notExistsUser);

    }
}
