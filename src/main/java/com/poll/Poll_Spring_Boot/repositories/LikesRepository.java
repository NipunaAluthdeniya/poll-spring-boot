package com.poll.Poll_Spring_Boot.repositories;

import com.poll.Poll_Spring_Boot.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findAllByPollId(Long id);

    Optional<Likes> findByPollIdAndUserId(Long pollId, Long id);

}
