package com.poll.Poll_Spring_Boot.repositories;

import com.poll.Poll_Spring_Boot.entities.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsRepository extends JpaRepository<Options,Long> {
}
