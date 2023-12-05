package caner.blog.service;

import caner.blog.dto.request.CreateCommentRequest;
import caner.blog.dto.response.CommentDTO;
import caner.blog.model.Comment;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getAllCommentsByPostId(Long id);

    Comment createComment(CreateCommentRequest createCommentRequest, Long id, String username);
}
