package com.poll.Poll_Spring_Boot.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OptionsDTO {

    private Long id;

    private String title;

    private Long pollId;

    private Integer voteCount;

    private boolean userVotedThisOption;

    private List<VoteDTO> voteDTOS;

}
