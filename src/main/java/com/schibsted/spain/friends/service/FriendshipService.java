package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.schibsted.spain.friends.domain.FriendshipStatus.*;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;


    public List<Friendship> listFriendship(String username){
        return this.friendshipRepository.findAllByUserUsername(username);
    }

    public Friendship requestFriendship(User user, User friend){
        return this.saveFriendshipStatus(user, friend, REQUESTED);
    }


    public Friendship acceptFriendship(User user, User friend){
        return this.saveFriendshipStatus(user, friend, ACCEPTED);
    }

    public Friendship declineFriendship(User user, User friend){
        return this.saveFriendshipStatus(user, friend, ACCEPTED);
    }

    @Transactional
    protected Friendship saveFriendshipStatus(User user, User friend, FriendshipStatus newStatus){
        Optional<Friendship> actualFriendship = this.friendshipRepository.findByUserAndFriend(user, friend);
        Optional<Friendship> reverseFriendship = this.friendshipRepository.findByUserAndFriend(friend, user);

        switch (newStatus){
            case REQUESTED:
                if (actualFriendship.isPresent() && !DECLINED.equals(actualFriendship.get().getStatus())||
                    reverseFriendship.isPresent() && !DECLINED.equals(reverseFriendship.get().getStatus())){
                    throw new InvalidFriendshipStatus("Cannot ask friendship. There is friendship request");
                }
                break;
            case ACCEPTED:
            case DECLINED:
                if (!reverseFriendship.isPresent() || !REQUESTED.equals(reverseFriendship.get().getStatus())){
                    throw new InvalidFriendshipStatus("Cannot accept nor decline friendship. There is no friendship request");
                }
                break;
        }

        Friendship friendshipRequest = new Friendship();
        friendshipRequest.setUser(user);
        friendshipRequest.setFriend(friend);
        friendshipRequest.setStatus(newStatus);

        return this.friendshipRepository.save(friendshipRequest);
    }
}
