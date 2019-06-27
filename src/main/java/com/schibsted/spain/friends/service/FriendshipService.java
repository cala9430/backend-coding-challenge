package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.schibsted.spain.friends.domain.FriendshipStatus.*;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    /**
     * Lists Users username with friendship accepted for the user requested
     * @param user  User to be evaluated
     * @return      List of usernames
     */
    public List<String> listAcceptedFriendshipUsers(User user){
        return this.friendshipRepository.findAllFriendsByUserAndStatus(user, ACCEPTED);
    }

    /**
     * Request friendship
     * @param user      User requester
     * @param friend    User requested
     * @return          Resultant friendship
     */
    public Friendship requestFriendship(User user, User friend){
        return this.performFriendshipAction(user, friend, REQUESTED);
    }

    /**
     * Accept friendship
     * @param user      User answering
     * @param friend    User requester
     * @return          Updated friendship
     */
    public Friendship acceptFriendship(User user, User friend){
        return this.performFriendshipAction(user, friend, ACCEPTED);
    }

    /**
     * Accept friendship
     * @param user      User answering
     * @param friend    User requester
     * @return          Updated friendship
     */
    public Friendship declineFriendship(User user, User friend){
        return this.performFriendshipAction(user, friend, DECLINED);
    }


    private Friendship performFriendshipAction(User user, User friend, FriendshipStatus newStatus){
        if(user.equals(friend)){
            throw new IllegalArgumentException("Cannot perform a friendship action with yourself");
        }

        return this.saveFriendshipStatus(user, friend, newStatus);
    }

    @Transactional
    protected Friendship saveFriendshipStatus(User user, User friend, FriendshipStatus newStatus){
        Optional<Friendship> actualFriendship = this.friendshipRepository.findByUserAndFriend(user, friend);
        Optional<Friendship> reverseFriendship = this.friendshipRepository.findByUserAndFriend(friend, user);

        this.validateNewStatus(actualFriendship.orElse(null), reverseFriendship.orElse(null), newStatus );

        Friendship friendshipRequest;

        if(REQUESTED.equals(newStatus)){
            friendshipRequest = new Friendship(user, friend, newStatus);
        }else{
            friendshipRequest = reverseFriendship.get();
            friendshipRequest.setStatus(newStatus);
        }

        return this.friendshipRepository.save(friendshipRequest);
    }

    /**
     * State machine for Friendship status. Creates a friendship Request or updates the existing one when Declining or Accepting
     * @param actualFriendship  Friendship from User to Friend
     * @param reverseFriendship Friendship from Friend to User
     * @param newStatus         new desired Status
     */
    private void validateNewStatus(Friendship actualFriendship, Friendship reverseFriendship, FriendshipStatus newStatus){

        boolean alreadyFriends = actualFriendship != null && !DECLINED.equals(actualFriendship.getStatus());

        switch (newStatus){
            case REQUESTED:
                boolean alreadyReverseFriends = reverseFriendship != null && !DECLINED.equals(reverseFriendship.getStatus());

                if ( alreadyFriends || alreadyReverseFriends ){
                    throw new InvalidFriendshipStatus("Cannot ask friendship. There is friendship request");
                }

                break;
            case ACCEPTED:
            case DECLINED:
                boolean friendshipNotRequested = reverseFriendship == null || !REQUESTED.equals(reverseFriendship.getStatus());

                if ( alreadyFriends|| friendshipNotRequested){
                    throw new InvalidFriendshipStatus("Cannot accept nor decline friendship. There is no friendship request");
                }

                break;
            default:
                throw new IllegalArgumentException(String.format("Unmapped friendship status: %s", String.valueOf(newStatus)));
        }

    }
}
