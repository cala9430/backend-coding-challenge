package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public List<User> listFriendship(User user){
        throw new NotImplementedException();
    }

    public boolean requestFriendship(User user, User friend){
        throw new NotImplementedException();
    }

    public boolean acceptFriendship(User user, User friend){
        throw new NotImplementedException();
    }

    public boolean declineFriendship(User user, User friend){
        throw new NotImplementedException();
    }
}
