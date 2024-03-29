package com.schibsted.spain.friends.legacy;

import com.schibsted.spain.friends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupLegacyController {

  private final UserService userService;

  @Autowired
  public SignupLegacyController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  void signUp(
      @RequestParam("username") String username,
      @RequestHeader("X-Password") String password
  ) {
    this.userService.registerNewUser(username, password);
  }

}
