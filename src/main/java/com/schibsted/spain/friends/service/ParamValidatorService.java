package com.schibsted.spain.friends.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ParamValidatorService {

    public void validateUsername(String username){
        if(StringUtils.isEmpty(username) || username.length() < 5 || username.length() > 10 || !username.matches("[a-zA-Z0-9]+")){
            throw new IllegalArgumentException("Invalid username.");
        }
    }

    public void validatePassword(String password){
        if(StringUtils.isEmpty(password) || password.length() < 8 || password.length() > 12 || !password.matches("[a-zA-Z0-9]+")){
            throw new IllegalArgumentException("Invalid username.");
        }
    }

}
