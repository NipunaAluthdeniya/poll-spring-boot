package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwtToken;

    private String name;

}
