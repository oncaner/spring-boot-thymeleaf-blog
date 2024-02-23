package caner.blog.service;

import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.request.UpdatePostRequest;
import caner.blog.dto.response.PageablePostDTO;
import caner.blog.dto.response.PostDTO;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    Page<Post> getAllPageablePosts(String page, int size);

    List<PostDTO> searchPostsByTitle(String title, String page);

    List<PostDTO> getAllPostsByUserId(Long id);

    List<PageablePostDTO> getAllPostsByUserIdPageable(Long id, String page, Principal principal);

    Post getPostById(Long id);

    Post createPost(CreatePostRequest createPostRequest);

    void deletePostById(Long id);

    Post updatePost(UpdatePostRequest updatePostRequest, HttpServletRequest request);
}
