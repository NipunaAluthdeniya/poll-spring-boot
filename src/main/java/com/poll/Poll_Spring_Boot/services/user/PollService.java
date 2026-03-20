package com.poll.Poll_Spring_Boot.services.user;

import com.poll.Poll_Spring_Boot.dtos.*;

import java.util.List;

public interface PollService {

    PollDTO postPoll(PollDTO pollDTO);

    void deletedPoll(Long id);

    List<PollDTO> getAllPolls();

    List<PollDTO> getMyPolls();

    LikesDTO giveLikeToPoll(Long id);

    CommentDTO postCommentOnPoll(CommentDTO commentDTO);

    VoteDTO postVoteOnPoll(VoteDTO voteDTO);

    PollDetailsDTO getPollById(Long pollId);

}
