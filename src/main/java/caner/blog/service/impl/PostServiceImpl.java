package caner.blog.service.impl;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.request.UpdatePostRequest;
import caner.blog.dto.response.PageablePostDTO;
import caner.blog.dto.response.PostDTO;
import caner.blog.model.Post;
import caner.blog.model.User;
import caner.blog.repository.PostRepository;
import caner.blog.service.PostService;
import caner.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

        return posts.stream()
                .map(post -> modelMapperService.forResponse()
                        .map(post, PostDTO.class)).toList();
    }

    @Override
    public Page<Post> getAllPageablePosts(String page, int size) {

        try {
            int pageNumber = Integer.parseInt(page);

            if (pageNumber < 1) {
                pageNumber = 1;
            }

            Pageable pageable = PageRequest.of(pageNumber - 1, size);

            return postRepository.findAll(pageable);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Sayfa numarası 0'dan büyük olmalı veya sayı olmalıdır.");
        }
    }

    @Override
    @Transactional // Large Object Hatası alınmaması için transactional kullanıldı.
    public List<PostDTO> searchPostsByTitle(String title, String page) {

        try {
            int pageNumber = Integer.parseInt(page);

            if (pageNumber < 1) {
                pageNumber = 1;
            }

            int size = 4;

            Pageable pageable = PageRequest.of(pageNumber - 1, size);
            Page<Post> pageablePosts = postRepository.findAllByTitleContainingIgnoreCase(title, pageable);

            return pageablePosts.getContent().stream()
                    .map(post -> modelMapperService.forResponse()
                            .map(post, PostDTO.class)).toList();

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Sayfa numarası 0'dan büyük olmalı veya sayı olmalıdır.");
        }
    }

    @Override
    @Transactional
    public List<PostDTO> getAllPostsByUserId(Long id) {
        List<Post> postList = postRepository.findAllByUserId(id);

        return postList.stream()
                .map(post -> modelMapperService.forResponse()
                        .map(post, PostDTO.class)).toList();
    }

    @Override
    @Transactional
    public List<PageablePostDTO> getAllPostsByUserIdPageable(Long id, String page, Principal principal) {

        try {
            int pageNumber = Integer.parseInt(page);

            if (pageNumber < 1) {
                pageNumber = 1;
            }

            int size = 4;

            String principalEmail = principal.getName();

            Pageable pageable = PageRequest.of(pageNumber - 1, size);
            Page<Post> pageablePosts = postRepository.findAllByUserId(id, pageable);

            List<PageablePostDTO> postDTOS = pageablePosts.getContent().stream()
                    .map(post -> modelMapperService.forResponse()
                            .map(post, PageablePostDTO.class)).toList();

            return postDTOS.stream().peek(post -> post.setPrincipalEmail(principalEmail)).toList();

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Sayfa numarası 0'dan büyük olmalı veya sayı olmalıdır.");
        }
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    @Override
    public Post createPost(CreatePostRequest createPostRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> user = userService.findByEmail(email);

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .shortInformation(createPostRequest.getShortInformation())
                .content(createPostRequest.getContent())
                .user(user.get())
                .build();

        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePostRequest updatePostRequest, HttpServletRequest request) {

        String postId = request.getParameter("postId");
        Long id = Long.parseLong(postId);

        Post post = getPostById(id);
        post.setTitle(updatePostRequest.getTitle());
        post.setShortInformation(updatePostRequest.getShortInformation());
        post.setContent(updatePostRequest.getContent());

        return postRepository.save(post);
    }
}
