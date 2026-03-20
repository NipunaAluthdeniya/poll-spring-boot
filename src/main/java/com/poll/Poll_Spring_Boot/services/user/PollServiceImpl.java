package com.poll.Poll_Spring_Boot.services.user;

import com.poll.Poll_Spring_Boot.dtos.*;
import com.poll.Poll_Spring_Boot.entities.*;
import com.poll.Poll_Spring_Boot.repositories.*;
import com.poll.Poll_Spring_Boot.utils.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService{

    private final JWTUtil jwtUtil;

    private final PollRepository pollRepository;

    private final OptionsRepository optionsRepository;

    private final VoteRepository voteRepository;

    private final JavaMailSender javaMailSender;

    private final LikesRepository likesRepository;

    private final CommentRepository commentRepository;

    public PollDTO getPollDTOInService(Poll poll) {
        User loggedInUser = jwtUtil.getLoggedInUser(); // Currently logged-in user
        PollDTO pollDTO = new PollDTO();
        pollDTO.setId(poll.getId());
        pollDTO.setQuestion(poll.getQuestion());
        pollDTO.setExpiredAt(poll.getExpiredAt());
        pollDTO.setExpired(poll.getExpiredAt() != null && poll.getExpiredAt().before(new Date()));
        pollDTO.setPostedDate(poll.getPostedDate());
        pollDTO.setOptionsDTOS(poll.getOptions().stream().map(
                options -> this.getOptionDTO(options, loggedInUser.getId(), poll.getId())).collect(Collectors.toList()));
        pollDTO.setTotalVoteCount(poll.getTotalVoteCount());

        User pollOwner = poll.getUser(); // User who posted the poll

        // Check if logged-in user is the poll owner
        if (loggedInUser != null && pollOwner.getId().equals(loggedInUser.getId())) {
            pollDTO.setUsername("You");
        } else {
            pollDTO.setUsername(pollOwner.getFirstName() + " " + pollOwner.getLastName());
        }

        pollDTO.setUserId(pollOwner.getId());

        // Check if logged-in user has voted
        if (loggedInUser != null) {
            pollDTO.setVoted(voteRepository.existsByPollIdAndUserId(poll.getId(), loggedInUser.getId()));
        }

        return pollDTO;

    }

    public OptionsDTO getOptionDTO(Options options, Long userId, Long pollId) {
        OptionsDTO optionsDTO = new OptionsDTO();
        optionsDTO.setId(options.getId());
        optionsDTO.setTitle(options.getTitle());
        optionsDTO.setPollId(options.getPoll().getId());
        optionsDTO.setVoteCount(options.getVoteCount());
        optionsDTO.setUserVotedThisOption(voteRepository.existsByPollIdAndUserIdAndOptionsId(pollId, userId, options.getId()));
        return optionsDTO;
    }

    @Override
    public PollDTO postPoll(PollDTO pollDTO) {
        User user = jwtUtil.getLoggedInUser();
        if (user != null) {
            Poll poll = new Poll();
            poll.setQuestion(pollDTO.getQuestion());
            poll.setPostedDate(new Date());
            poll.setExpiredAt(pollDTO.getExpiredAt());
            poll.setUser(user);
            poll.setTotalVoteCount(0);
            Poll createdPoll = pollRepository.save(poll);

            List<Options> options = new ArrayList<>();
            for (String optionTitle : pollDTO.getOptions()) {
                Options option = new Options();
                option.setTitle(optionTitle);
                option.setPoll(createdPoll);
                option.setVoteCount(0);
                options.add(option);
            }

            List<Options> savedOptions = optionsRepository.saveAll(options);
            poll.setOptions(savedOptions);
            pollRepository.save(poll);

            if (createdPoll.getId() != null) {
                try {
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                    mimeMessageHelper.setFrom("nipunaaluthdeniya@gmail.com");
                    mimeMessageHelper.setTo(user.getEmail());
                    mimeMessageHelper.setSubject("New Poll Posted");
                    mimeMessageHelper.setText("Dear " + user.getFirstName() + "! I trust this message finds you in good spirits. I wanted to inform you that a new poll has been successfully posted. The question you submitted is as follows: '" + createdPoll.getQuestion() + "'. This poll was posted on " + createdPoll.getPostedDate() + ", and it is scheduled to expire on " + createdPoll.getExpiredAt() + ". Thank you for your engagement and contribution to our platform. POLL APP");
                    javaMailSender.send(mimeMessage);
                    System.out.println("Mail sent to " + user.getEmail());
                } catch (MessagingException e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                }
            }
            return getPollDTOInService(createdPoll);
        }
        return null;
    }

    @Override
    public void deletedPoll(Long id) {
        pollRepository.deleteById(id);
    }

    @Override
    public List<PollDTO> getAllPolls() {
        return pollRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Poll::getPostedDate).reversed())
                .map(this::getPollDTOInService)
                .collect(Collectors.toList());
    }

    @Override
    public List<PollDTO> getMyPolls() {
        User user = jwtUtil.getLoggedInUser();
        if (user != null) {
            return pollRepository.findAllByUserId(user.getId())
                    .stream()
                    .sorted(Comparator.comparing(Poll::getPostedDate).reversed())
                    .map(this::getPollDTOInService)
                    .collect(Collectors.toList());
        }
        throw new EntityNotFoundException("User not found");
    }

    @Override
    public LikesDTO giveLikeToPoll(Long id) {
        Optional<Poll> optionalPoll = pollRepository.findById(id);
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent()){
            Likes like = new Likes();
            like.setUser(user);
            like.setPoll(optionalPoll.get());
            return likesRepository.save(like).getLikesDTO();
        }
        return null;
    }

    @Override
    public CommentDTO postCommentOnPoll(CommentDTO commentDTO) {
        Optional<Poll> optionalPoll = pollRepository.findById(commentDTO.getPollId());
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent()){
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPoll(optionalPoll.get());
            comment.setContent(comment.getContent());
            comment.setCreatedAt(new Date());
            return commentRepository.save(comment).getCommentDTO();
        }
        return null;
    }

    @Override
    public VoteDTO postVoteOnPoll(VoteDTO voteDTO) {
        Optional<Poll> optionalPoll = pollRepository.findById(voteDTO.getPollId());
        Optional<Options> optionalOption = optionsRepository.findById(voteDTO.getOptionId());
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent() && optionalOption.isPresent()){
            Vote vote = new Vote();
            // Check if the poll has expired
            if (optionalPoll.get().getExpiredAt().before(new Date())) {
                throw new EntityNotFoundException("Poll has expired and cannot be voted on.");
            }
            vote.setUser(user);
            vote.setPoll(optionalPoll.get());
            vote.setOptions(optionalOption.get());
            vote.setPostedDate(new Date());
            optionalPoll.get().setTotalVoteCount(optionalPoll.get().getTotalVoteCount() + 1);
            optionalOption.get().setVoteCount(optionalOption.get().getVoteCount() + 1);
            optionsRepository.save(optionalOption.get());
            Vote voted = voteRepository.save(vote);
            pollRepository.save(optionalPoll.get());
            return voted.getVoteDTO();
        }
        return null;
    }

    @Override
    public PollDetailsDTO getPollById(Long pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        User user = jwtUtil.getLoggedInUser();
        if(user != null && optionalPoll.isPresent()){
            List<Likes> likesList = likesRepository.findAllByPollId(optionalPoll.get().getId());
            List<Comment> commentList = commentRepository.findAllByPollId(optionalPoll.get().getId());
            PollDetailsDTO pollDetailsDTO = new PollDetailsDTO();
            pollDetailsDTO.setPollDTO(getPollDTOInService(optionalPoll.get()));
            pollDetailsDTO.getPollDTO().setLiked(likesRepository.findByPollIdAndUserId(pollId, user.getId()).isPresent());
            // Map comments and set "You" if the comment was posted by the logged-in user
            List<CommentDTO> commentDTOList = commentList.stream().map( comment -> {
                CommentDTO commentDTO = comment.getCommentDTO();
                if (comment.getUser().getId().equals(user.getId())) {
                    commentDTO.setUsername("You"); // Set "You" if the user posted the comment
                }
                return commentDTO;
            }).toList();
            pollDetailsDTO.setCommentDTOS(commentDTOList);
            pollDetailsDTO.setLikesCount((long) likesList.size());
            pollDetailsDTO.setCommentsCount((long) commentList.size());
            return pollDetailsDTO;
        }
        return null;
    }

}
