package com.poll.Poll_Spring_Boot.repositories;

import com.poll.Poll_Spring_Boot.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByPollIdAndUserId(Long pollId, Long userId);

    boolean existsByPollIdAndUserIdAndOptionsId(Long pollId, Long userId, Long optionId);

}
