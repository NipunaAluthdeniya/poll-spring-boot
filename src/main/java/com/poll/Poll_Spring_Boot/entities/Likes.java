package com.poll.Poll_Spring_Boot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poll.Poll_Spring_Boot.dtos.LikesDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Poll poll;

    public LikesDTO getLikesDTO() {
        LikesDTO likesDTO = new LikesDTO();
        likesDTO.setId(id);
        likesDTO.setPollId(poll.getId());
        likesDTO.setUserId(user.getId());
        likesDTO.setUsername(user.getFirstName() + " " + user.getLastName());
        return likesDTO;
    }

}
