package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.domain.exception.InvalidCredentialsException;
import com.schibsted.spain.friends.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ParamValidatorService validatorService;

    @Before
    public void setUp() {
        User test = new User("test", this.passwordEncoder.encode("pass"));

        Mockito.when(userRepository.findByUsername(test.getUsername()))
                .thenReturn(Optional.of(test));

        Mockito.when(userRepository.existsUserByUsername(test.getUsername()))
                .thenReturn(true);

        Mockito.doNothing().when(validatorService).validateUsername(Mockito.anyString());
        Mockito.doNothing().when(validatorService).validatePassword(Mockito.anyString());
    }

    @Test
    public void registerNewUser(){
        String password = "pass2";
        User newUser = new User("test2", this.passwordEncoder.encode(password));

        Mockito.when(userRepository.save(newUser))
                .thenReturn(newUser);

        User registeredNewUser = this.userService.registerNewUser(newUser.getUsername(), password);

        Assert.assertNotNull(registeredNewUser);
        Assert.assertEquals(newUser.getUsername(), registeredNewUser.getUsername());
        Assert.assertTrue(this.passwordEncoder.matches(password, registeredNewUser.getPassword()));
    }

    @Test(expected = DuplicatedUserException.class)
    public void registerAlreadyPresentUser(){
        User newUser = new User("test", "pass2");
        this.userService.registerNewUser(newUser.getUsername(), newUser.getPassword());
    }

    @Test
    public void findUserByUsernamePresent(){
        Optional<User> optionalUser = this.userService.findByUsername("test");

        Assert.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        Assert.assertEquals("test", user.getUsername());
    }

    @Test
    public void findUserByUsernameNotPresent(){
        Optional<User> optionalUser = this.userService.findByUsername("usernotfound");
        Assert.assertFalse(optionalUser.isPresent());
    }

    @Test
    public void validCredentialsTest() {
        String username = "test";
        String password = "pass";
        User user = this.userService.authenticateUser(username, password);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
        Assert.assertTrue( this.passwordEncoder.matches("pass", user.getPassword()));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void invalidCredentialsTest() {
        this.userService.authenticateUser("test", "wrongPassword");
    }

    @Test
    public void getByUsernameTest() {
        String username = "test";
        User user = this.userService.getByUsername(username);
        Assert.assertNotNull(user);
        Assert.assertEquals(username, user.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getByUsernameNotFoundTest() {
        this.userService.getByUsername("userNotFound");
    }
}
