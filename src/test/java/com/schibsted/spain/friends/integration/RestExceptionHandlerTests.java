package com.schibsted.spain.friends.integration;

import com.schibsted.spain.friends.domain.exception.ApiException;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.domain.exception.InvalidCredentialsException;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import com.schibsted.spain.friends.legacy.RestExceptionHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RestExceptionHandlerTests {

    private RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void testIllegalArgumentHandling() {
        ResponseEntity response = handler.handleException(new IllegalArgumentException("message"), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertEquals("message", exception.getMessage());
    }

    @Test
    public void testDuplicatedUserExceptionHandling() {
        ResponseEntity response = handler.handleException(new DuplicatedUserException("message"), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertEquals("message", exception.getMessage());
    }

    @Test
    public void testInvalidFriendshipStatusHandling() {
        ResponseEntity response = handler.handleException(new InvalidFriendshipStatus("message"), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertEquals("message", exception.getMessage());
    }

    @Test
    public void testUsernameNotFoundExceptionHandling() {
        ResponseEntity response = handler.handleException(new UsernameNotFoundException("message"), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertEquals("message", exception.getMessage());
    }

    @Test
    public void testInvalidCredentialsExceptionHandling() {
        ResponseEntity response = handler.handleException(new InvalidCredentialsException(), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.FORBIDDEN;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertNull(exception.getMessage());
    }

    @Test
    public void testExceptionHandling() {
        ResponseEntity response = handler.handleException(new Exception("message"), null);
        ApiException exception = (ApiException)response.getBody();

        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Assert.assertEquals(expectedStatus, response.getStatusCode());
        Assert.assertEquals(expectedStatus, exception.getStatus());
        Assert.assertEquals("message", exception.getMessage());
    }

}
