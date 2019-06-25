package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.service.FriendshipService;
import com.schibsted.spain.friends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friendship")
public class FriendshipLegacyController {

  private final FriendshipService friendshipService;

  private final UserService userService;

  @Autowired
  public FriendshipLegacyController(FriendshipService friendshipService, UserService userService) {
    this.friendshipService = friendshipService;
    this.userService = userService;
  }

  @PostMapping("/request")
  void requestFriendship(
      @RequestParam("usernameFrom") String usernameFrom,
      @RequestParam("usernameTo") String usernameTo,
      @RequestHeader("X-Password") String password
  ) {
    this.friendshipService.requestFriendship(
            this.userService.authenticateUser(usernameFrom, password),
            this.userService.getByUsername(usernameTo));

  }

  @PostMapping("/accept")
  void acceptFriendship(
      @RequestParam("usernameFrom") String usernameFrom,
      @RequestParam("usernameTo") String usernameTo,
      @RequestHeader("X-Password") String password
  ) {
    this.friendshipService.acceptFriendship(
            this.userService.authenticateUser(usernameFrom, password),
            this.userService.getByUsername(usernameTo));
  }

  @PostMapping("/decline")
  void declineFriendship(
      @RequestParam("usernameFrom") String usernameFrom,
      @RequestParam("usernameTo") String usernameTo,
      @RequestHeader("X-Password") String password
  ) {
    this.friendshipService.declineFriendship(
            this.userService.authenticateUser(usernameFrom, password),
            this.userService.getByUsername(usernameTo));
  }

  @GetMapping("/list")
  Object listFriends(
      @RequestParam("username") String username,
      @RequestHeader("X-Password") String password
  ) {
    return this.friendshipService.listAcceptedFriendshipUsers(this.userService.authenticateUser(username, password));
  }
}
