package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.DuplicatedUserException;
import com.schibsted.spain.friends.domain.exception.InvalidCredentialsException;
import com.schibsted.spain.friends.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ParamValidatorService validatorService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Registers a new User
     * @param username  User username
     * @param password  User password
     * @return          Resultant User entity
     */
    public User registerNewUser(String username, String password){
        this.validatorService.validateUsername(username);
        this.validatorService.validatePassword(password);

        Optional<User> user = this.findByUsername(username);
        if(user.isPresent()){
            throw new DuplicatedUserException(String.format("Username %s is already taken", username));
        }

        User newUser = new User(username, this.encoder.encode(password));

        return this.userRepository.save(newUser);
    }

    /**
     * Authenticates User by username and password
     * @param username  User username
     * @param password  User password
     * @return          User if credentials were Ok
     */
    public User authenticateUser(String username, String password){
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if(optionalUser.isPresent() && this.encoder.matches(password, optionalUser.get().getPassword())){
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
