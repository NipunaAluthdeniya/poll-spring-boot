package com.poll.Poll_Spring_Boot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poll.Poll_Spring_Boot.dtos.CommentDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private String content;

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

    public CommentDTO getCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setContent(content);
        commentDTO.setCreatedAt(createdAt);
        commentDTO.setPollId(poll.getId());
        commentDTO.setUserId(user.getId());
        commentDTO.setUsername(user.getFirstName() + " " + user.getLastName());
        return commentDTO;
    }

}
