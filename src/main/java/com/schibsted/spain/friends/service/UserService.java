package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.domain.exception.InvalidCredentialsException;
import com.schibsted.spain.friends.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ParamValidatorService validatorService;

    @Autowired
    private UserRepository userRepository;

    public User registerNewUser(String username, String password){
        this.validatorService.validateUsername(username);
        this.validatorService.validatePassword(password);

        Optional<User> user = this.findByUsername(username);
        if(user.isPresent()){
            throw new DuplicatedUserException(String.format("Username %s is already taken", username));
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        return this.userRepository.save(newUser);
    }

    public User authenticateUser(String username, String password){
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new InvalidCredentialsException();
    }

    public Optional<User> findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }

    public User getByUsername(String username){
        Optional<User> userOptional = this.userRepository.findByUsername(username);

        if(userOptional.isPresent()){
            return userOptional.get();
        }

        throw new UsernameNotFoundException(username);
    }
}
