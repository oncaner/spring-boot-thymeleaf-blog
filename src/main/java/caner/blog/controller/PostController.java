package caner.blog.controller;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.request.CreateCommentRequest;
import caner.blog.dto.request.CreatePostRequest;
import caner.blog.dto.request.UpdatePostRequest;
import caner.blog.dto.response.CommentDTO;
import caner.blog.dto.response.PostDTO;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.Post;
import caner.blog.model.User;
import caner.blog.service.CommentService;
import caner.blog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getAllPosts(@RequestParam(value = "page", required = false,
            defaultValue = "1") String page, Model model, RedirectAttributes redirectAttributes) {

        int size = 4;

        try {
            Page<Post> pageableUsers = postService.getAllPageablePosts(page, size);

            int pageNumber = Integer.parseInt(page);

            if(pageNumber < 1){
                pageNumber = 1;
            }

            int totalPages = pageableUsers.getTotalPages();

            if(pageNumber > totalPages){
                redirectAttributes.addFlashAttribute("pageNumberException", "Sayfa numarası çok fazla!");
                return "redirect:/posts";
            }

            List<PostDTO> postList = pageableUsers.getContent().stream()
                    .map(post -> modelMapperService.forResponse().map(post, PostDTO.class)).toList();

            model.addAttribute("posts", postList);
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", pageableUsers.getTotalElements());

            return "posts";

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("numberFormatException", e.getMessage());
            return "redirect:/posts";
        }
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
    public String showUpdatePostForm(@PathVariable("id") Long id, Model model, Principal principal) {

        String principalEmail = principal.getName();
        Post post = postService.getPostById(id);

        if (!post.getUser().getEmail().equals(principalEmail)) {
            return "redirect:/posts";
        }

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
