package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerNewUser(String username, String password){
        Optional<User> user = this.findByUsername(username);
        if(user.isPresent()){
            throw new DuplicatedUserException(String.format("Username %s is already taken", username));
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        return this.userRepository.save(newUser);
    }

    public void authenticateUser(){
        throw new NotImplementedException();
    }

    public Optional<User> findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }
}