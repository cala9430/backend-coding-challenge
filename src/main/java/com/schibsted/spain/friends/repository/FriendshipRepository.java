package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findAllByUserUsername(String username);

    List<Friendship> findAllByUser(User user);
}
