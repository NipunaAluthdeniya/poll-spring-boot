package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private Long id;

    private String content;

    private Date createdAt;

    private String username;

    private Long userId;

    private Long pollId;

}
