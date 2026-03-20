package com.poll.Poll_Spring_Boot.repositories;

import com.poll.Poll_Spring_Boot.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPollId(Long id);

}
