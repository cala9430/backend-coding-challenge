package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;


    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;

    }

    @Transactional
    public List<Friendship> listFriendship(String username){
        return this.friendshipRepository.findAllByUserUsername(username);
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
