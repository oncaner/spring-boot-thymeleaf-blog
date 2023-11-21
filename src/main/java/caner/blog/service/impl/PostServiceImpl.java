package caner.blog.service.impl;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.response.PostDTO;
import caner.blog.model.Post;
import caner.blog.model.User;
import caner.blog.repository.PostRepository;
import caner.blog.service.PostService;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapperService modelMapperService;
    private final UserService userService;

    @Override
    public List<PostDTO> getAllPosts() {

        List<Post> posts = postRepository.findAll();

        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapperService.forResponse()
                        .map(post, PostDTO.class)).toList();

        return postDTOS;
    }

    @Override
    public Post createPost(CreatePostRequest createPostRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> user = userService.findByEmail(email);

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .createdDate(LocalDateTime.now())
                .user(user.get())
                .build();

        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}
