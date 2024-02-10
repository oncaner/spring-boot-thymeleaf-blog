package caner.blog.service;

import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.request.UpdatePostRequest;
import caner.blog.dto.response.PostDTO;
import caner.blog.model.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    Page<Post> getAllPageablePosts(String page, int size);

    List<PostDTO> getAllPostsByUserId(Long id);

    Post getPostById(Long id);

    Post createPost(CreatePostRequest createPostRequest);

    void deletePostById(Long id);

    Post updatePost(UpdatePostRequest updatePostRequest, HttpServletRequest request);
}
