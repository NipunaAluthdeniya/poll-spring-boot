package com.poll.Poll_Spring_Boot.dtos;

import com.poll.Poll_Spring_Boot.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private UserRole userRole;

}
