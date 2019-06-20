package com.schibsted.spain.friends.unit.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    private final UserService userService;

    @MockBean
    private UserRepository userRepository;

    public UserServiceTests() {
        this.userService = new UserService(userRepository);
    }

    @Before
    public void setUp() {
        User test = new User();
        test.setUsername("test");
        test.setPassword("pass");

        Mockito.when(userRepository.findByUsername(test.getUsername()))
                .thenReturn(Optional.of(test));

        Mockito.when(userRepository.existsUserByUsername(test.getUsername()))
                .thenReturn(true);
    }

    @Test
    public void registerNewUser(){
        User newUser = new User();
        newUser.setUsername("test2");
        newUser.setPassword("pass2");

        boolean success = this.userService.registerNewUser(newUser.getUsername(), newUser.getPassword());

        Assert.assertTrue(success);
    }

    @Test(expected = DuplicatedUserException.class)
    public void registerAlreadyPresentUser(){
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword("pass2");

        this.userService.registerNewUser(newUser.getUsername(), newUser.getPassword());
    }
}
