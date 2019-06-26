package com.schibsted.spain.friends.service;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.domain.exception.InvalidFriendshipStatus;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import com.schibsted.spain.friends.service.comparator.FriendshipComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

import static com.schibsted.spain.friends.domain.FriendshipStatus.*;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    private FriendshipComparator friendshipComparator = new FriendshipComparator();

    /**
     * Lists Users username with friendship accepted for the user requested
     * @param user  User to be evaluated
     * @return      List of usernames
     */
    public List<String> listAcceptedFriendshipUsers(User user){
        List<String> result = new ArrayList<>();

        List<Friendship> friendships = this.friendshipRepository.findAllByUserAndStatus(user, ACCEPTED);

        if(!CollectionUtils.isEmpty(friendships)){
            friendships.sort((o1,o2)-> friendshipComparator.compare(o1,o2));
            friendships.forEach(friendship -> result.add(this.extractFriendUsername(user, friendship)));
        }

        return result;
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

    /**
     * State machine for Friendship status. Creates a friendship Request or updates the existing one when Declining or Accepting
     * @param user      User that performs the action
     * @param friend    User target of the action
     * @param newStatus New Status for the friendship
     * @return          Resultant friendship entity
     */
    @Transactional
    protected Friendship saveFriendshipStatus(User user, User friend, FriendshipStatus newStatus){
        Optional<Friendship> actualFriendship = this.friendshipRepository.findByUserAndFriend(user, friend);
        Optional<Friendship> reverseFriendship = this.friendshipRepository.findByUserAndFriend(friend, user);

        Friendship friendshipRequest;
        switch (newStatus){
            case REQUESTED:
                if ((actualFriendship.isPresent() && !DECLINED.equals(actualFriendship.get().getStatus())) ||
                    (reverseFriendship.isPresent() && !DECLINED.equals(reverseFriendship.get().getStatus()))){
                    throw new InvalidFriendshipStatus("Cannot ask friendship. There is friendship request");
                }

                friendshipRequest = new Friendship(user, friend, newStatus);

                break;
            case ACCEPTED:
            case DECLINED:
                if ((actualFriendship.isPresent() && !DECLINED.equals(actualFriendship.get().getStatus())) || !reverseFriendship.isPresent() || !REQUESTED.equals(reverseFriendship.get().getStatus())){
                    throw new InvalidFriendshipStatus("Cannot accept nor decline friendship. There is no friendship request");
                }

                friendshipRequest = reverseFriendship.get();
                friendshipRequest.setStatus(newStatus);

                break;
            default:
                throw new IllegalArgumentException(String.format("Unmapped friendship status: %s", String.valueOf(newStatus)));
        }

        return this.friendshipRepository.save(friendshipRequest);
    }

    /**
     * Extracts the right username from friendship for user
     * @param user          User requesting
     * @param friendship    Friendship to be processed
     * @return              Username of friend
     */
    private String extractFriendUsername(User user, Friendship friendship) {
        if(friendship.getUser().equals(user)){
            return friendship.getFriend().getUsername();
        }else{
            return friendship.getUser().getUsername();
        }
    }
}
