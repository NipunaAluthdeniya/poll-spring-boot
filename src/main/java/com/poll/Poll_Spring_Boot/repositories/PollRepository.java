package com.poll.Poll_Spring_Boot.repositories;

import com.poll.Poll_Spring_Boot.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByUserId(Long id);
}
