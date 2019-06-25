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

    public List<String> listAcceptedFriendshipUsers(User user){
        List<String> result = new ArrayList<>();

        List<Friendship> friendships = this.friendshipRepository.findAllByUserAndStatus(user, ACCEPTED);

        if(!CollectionUtils.isEmpty(friendships)){
            friendships.sort((o1,o2)-> friendshipComparator.compare(o1,o2));
            friendships.forEach(friendship -> result.add(this.extractFriendUsername(user, friendship)));
        }

        return result;
    }

    public Friendship requestFriendship(User user, User friend){
        return this.performFriendshipAction(user, friend, REQUESTED);
    }

    public Friendship acceptFriendship(User user, User friend){
        return this.performFriendshipAction(user, friend, ACCEPTED);
    }

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

        Friendship friendshipRequest = null;
        switch (newStatus){
            case REQUESTED:
                if ((actualFriendship.isPresent() && !DECLINED.equals(actualFriendship.get().getStatus())) ||
                    (reverseFriendship.isPresent() && !DECLINED.equals(reverseFriendship.get().getStatus()))){
                    throw new InvalidFriendshipStatus("Cannot ask friendship. There is friendship request");
                }

                friendshipRequest = new Friendship();
                friendshipRequest.setUser(user);
                friendshipRequest.setFriend(friend);
                friendshipRequest.setStatus(newStatus);

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

        friendshipRequest.setLastModifiedDate(new Date());
        return this.friendshipRepository.save(friendshipRequest);
    }

    private String extractFriendUsername(User user, Friendship friendship) {
        if(friendship.getUser().equals(user)){
            return friendship.getFriend().getUsername();
        }else{
            return friendship.getUser().getUsername();
        }
    }
}
