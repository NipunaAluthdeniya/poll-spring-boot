package com.poll.Poll_Spring_Boot.controllers.user;

import com.poll.Poll_Spring_Boot.dtos.PollDTO;
import com.poll.Poll_Spring_Boot.services.user.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin("*")
public class PollController {

    private final PollService pollService;

    @PostMapping("/poll")
    public ResponseEntity<?> postPoll(@RequestBody PollDTO pollDTO) {
        PollDTO createdPollDTO = pollService.postPoll(pollDTO);
        if (createdPollDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPollDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/poll/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        pollService.deletedPoll(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/polls")
    public ResponseEntity<?> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @GetMapping("/my-polls")
    public ResponseEntity<?> getMyPolls() {
        return ResponseEntity.ok(pollService.getMyPolls());
    }

}
