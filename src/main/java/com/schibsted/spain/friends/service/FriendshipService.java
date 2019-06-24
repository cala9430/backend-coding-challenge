package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Friendship requestFriendship(User user, User friend){
        return this.saveFriendship(user, friend, FriendshipStatus.REQUESTED);
    }

    @Transactional
    public Friendship acceptFriendship(User user, User friend) throws Exception {
        Optional<Friendship> actualFriendship = this.friendshipRepository.findByUserUsernameAndFriendUsername(user.getUsername(), friend.getUsername());
        if(actualFriendship.isPresent()){
            Friendship friendship = actualFriendship.get();
            if(FriendshipStatus.REQUESTED.equals(friendship.getStatus())){
                friendship.setStatus(FriendshipStatus.ACCEPTED);
                return this.friendshipRepository.save(friendship);
            }

        }
        throw new Exception("No friendship requested to accept");
    }

    public boolean declineFriendship(User user, User friend){
        throw new NotImplementedException();
    }

    private Friendship saveFriendship(User user, User friend, FriendshipStatus status){
        Friendship friendshipRequest = new Friendship();
        friendshipRequest.setUser(user);
        friendshipRequest.setFriend(friend);
        friendshipRequest.setStatus(status);

        return this.friendshipRepository.save(friendshipRequest);
    }
}
