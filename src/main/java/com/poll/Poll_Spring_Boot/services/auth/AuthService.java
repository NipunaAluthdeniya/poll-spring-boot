package com.poll.Poll_Spring_Boot.services.auth;

import com.poll.Poll_Spring_Boot.dtos.SignupRequest;
import com.poll.Poll_Spring_Boot.dtos.UserDTO;

public interface AuthService {

    UserDTO createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);

}
