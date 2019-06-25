package com.schibsted.spain.friends.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ParamValidatorServiceTests {

    @TestConfiguration
    static class ParamValidatorServiceImplTestContextConfiguration {

        @Bean
        public ParamValidatorService paramValidatorService() {
            return new ParamValidatorService();
        }
    }

    @Autowired
    private ParamValidatorService paramValidatorService;

    @Test(expected = IllegalArgumentException.class)
    public void usernameNull() {
        this.paramValidatorService.validateUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void usernameTooShort() {
        this.paramValidatorService.validateUsername("user");
    }

    @Test(expected = IllegalArgumentException.class)
    public void usernameTooLong() {
        this.paramValidatorService.validateUsername("toolongusername");
    }

    @Test(expected = IllegalArgumentException.class)
    public void usernameInvalidCharacters() {
        this.paramValidatorService.validateUsername("john_doe");
    }

    @Test
    public void usernameValidLengthAndCharacters() {
        this.paramValidatorService.validateUsername("johndoe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordNull() {
        this.paramValidatorService.validatePassword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordTooShort() {
        this.paramValidatorService.validatePassword("passwor");
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordTooLong() {
        this.paramValidatorService.validatePassword("toolongpassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordInvalidCharacters() {
        this.paramValidatorService.validatePassword("pass/word");
    }

    @Test
    public void passwordValid() {
        this.paramValidatorService.validatePassword("password");
    }
}
