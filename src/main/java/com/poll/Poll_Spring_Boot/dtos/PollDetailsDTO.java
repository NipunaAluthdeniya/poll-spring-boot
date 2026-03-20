package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PollDetailsDTO {

    private PollDTO pollDTO;

    private List<CommentDTO> commentDTOS;

    private Long likesCount;

    private Long commentsCount;

}
