package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.domain.exception.InvalidCredentialsException;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, DuplicatedUserException.class, InvalidCredentialsException.class, InvalidFriendshipStatus.class, UsernameNotFoundException.class})
    protected ResponseEntity handleIllegalArgumentException(Exception ex, WebRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}