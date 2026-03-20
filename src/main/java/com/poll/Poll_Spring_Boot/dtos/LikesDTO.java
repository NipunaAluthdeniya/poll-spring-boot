package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

@Data
public class LikesDTO {

    private Long id;

    private Long pollId;

    private Long userId;

    private String username;

}
