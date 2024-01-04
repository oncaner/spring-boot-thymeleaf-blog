package caner.blog.service.impl;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.request.CreateCommentRequest;
import caner.blog.dto.response.CommentDTO;
import caner.blog.model.Comment;
import caner.blog.model.Post;
import caner.blog.model.User;
import caner.blog.repository.CommentRepository;
import caner.blog.service.CommentService;
import caner.blog.service.PostService;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapperService modelMapperService;
    private final PostService postService;
    private final UserService userService;

    @Override
    public List<CommentDTO> getAllCommentsByPostId(Long id) {
        List<Comment> comments = commentRepository.findAllByPostId(id);
        return comments.stream()
                .map(comment -> modelMapperService.forResponse()
                        .map(comment, CommentDTO.class)).toList();
    }

    @Override
    public Comment createComment(CreateCommentRequest request, Long id, String userEmail) {

        Post post = postService.getPostById(id);
        Optional<User> user = userService.findByEmail(userEmail);

        Comment comment = Comment.builder()
                .comment(request.getComment())
                .post(post)
                .user(user.get())
                .build();

        return commentRepository.save(comment);
    }
}
