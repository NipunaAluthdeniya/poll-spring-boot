package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

}
