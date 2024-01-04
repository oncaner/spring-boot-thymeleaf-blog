package caner.blog.service;

import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.response.PostDTO;
import caner.blog.model.Post;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    List<PostDTO> getAllPostsByUserId(Long id);

    Post getPostById(Long id);

    Post createPost(CreatePostRequest createPostRequest);

    void deletePostById(Long id);
}
