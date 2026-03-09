package com.poll.Poll_Spring_Boot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poll.Poll_Spring_Boot.dtos.VoteDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date postedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Options options;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Poll poll;

    public VoteDTO getVoteDTO() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(id);
        voteDTO.setPostedBy(user.getId());
        voteDTO.setOptionId(options.getId());
        voteDTO.setPollId(poll.getId());
        return voteDTO;
    }

}
