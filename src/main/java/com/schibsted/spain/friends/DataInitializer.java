package com.schibsted.spain.friends;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import com.schibsted.spain.friends.repository.FriendshipRepository;
import com.schibsted.spain.friends.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository users;

    @Autowired
    FriendshipRepository friendships;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setUsername("test");
        user1.setPassword(this.passwordEncoder.encode("pass"));
        this.users.save(user1);

        User user2 = new User();
        user2.setUsername("test2");
        user2.setPassword(this.passwordEncoder.encode("pass"));
        this.users.save(user2);

        Friendship friendship = new Friendship();
        friendship.setUser(user1);
        friendship.setFriend(user2);
        friendship.setStatus(FriendshipStatus.REQUESTED);
        this.friendships.save(friendship);

    }
}
