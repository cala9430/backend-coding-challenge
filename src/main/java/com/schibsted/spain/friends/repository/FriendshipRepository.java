package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.domain.Friendship;
import com.schibsted.spain.friends.domain.FriendshipStatus;
import com.schibsted.spain.friends.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT fs FROM Friendship fs WHERE (fs.user = :user OR fs.friend = :user) AND fs.status = :status")
    List<Friendship> findAllByUserAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);

    Optional<Friendship> findByUserAndFriend(User username, User friend);
}
