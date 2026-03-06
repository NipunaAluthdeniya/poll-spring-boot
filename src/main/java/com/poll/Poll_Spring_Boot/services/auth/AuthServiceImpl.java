package com.poll.Poll_Spring_Boot.services.auth;

import com.poll.Poll_Spring_Boot.dtos.SignupRequest;
import com.poll.Poll_Spring_Boot.dtos.UserDTO;
import com.poll.Poll_Spring_Boot.entities.User;
import com.poll.Poll_Spring_Boot.enums.UserRole;
import com.poll.Poll_Spring_Boot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(SignupRequest signupRequest) {

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setUserRole(UserRole.USER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        User createdUser = userRepository.save(user);
        return createdUser.getUserDTO();
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
