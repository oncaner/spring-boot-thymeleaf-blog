package caner.blog.controller;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.request.CreateCommentRequest;
import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.request.UpdatePostRequest;
import caner.blog.dto.response.CommentDTO;
import caner.blog.dto.response.PostDTO;
import caner.blog.model.Post;
import caner.blog.service.CommentService;
import caner.blog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ModelMapperService modelMapperService;
    private final CommentService commentService;

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {

        Post post = postService.getPostById(id);
        PostDTO postDTO = modelMapperService.forResponse().map(post, PostDTO.class);

        List<CommentDTO> commentDTOS = commentService.getAllCommentsByPostId(id);

        model.addAttribute("post", postDTO);
        model.addAttribute("newComment", new CreateCommentRequest());
        model.addAttribute("comments", commentDTOS);

        return "post-detail";
    }

    @GetMapping("/create-post")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new CreatePostRequest());

        return "create_post-form";
    }

    @PostMapping("/create-post")
    public String createPost(@Valid @ModelAttribute("post") CreatePostRequest createPostRequest,
                             BindingResult result) {

        if (result.hasErrors()) {
            return "create_post-form";
        }

        Post post = postService.createPost(createPostRequest);

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/update-post/{id}")
    public String showUpdatePostForm(@PathVariable("id") Long id, Model model) {

        Post post = postService.getPostById(id);

        model.addAttribute("post", post);

        return "update_post-form";
    }

    @PostMapping("/update-post")
    public String updatePost(@Valid @ModelAttribute("post") UpdatePostRequest updatePostRequest,
                             BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "update_post-form";
        }

        String postId = request.getParameter("postId");
        long id = Long.parseLong(postId);

        postService.updatePost(updatePostRequest, request);

        return "redirect:/posts/" + id;
    }

    //Admin paneline eklenecek.

//    @GetMapping("/delete/{id}")
//    public String deletePostById(@PathVariable("id") Long id) {
//        postService.deletePostById(id);
//
//        return "redirect:/posts";
//    }

}
