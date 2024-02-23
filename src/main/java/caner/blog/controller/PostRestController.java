package caner.blog.controller;

import caner.blog.dto.response.PageablePostDTO;
import caner.blog.dto.response.PostDTO;
import caner.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostRestController {

    private final PostService postService;

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPostsByTitle(@RequestParam String title,
                                                            @RequestParam(value = "page",
                                                                    required = false,
                                                                    defaultValue = "1") String page) {

        return ResponseEntity.ok(postService.searchPostsByTitle(title, page));
    }

    @GetMapping("/pageable")
    public ResponseEntity<List<PageablePostDTO>> getAllPostsByUserIdPageable(@RequestParam(value = "page",
            required = false,
            defaultValue = "1") String page, @RequestParam(value = "userId") Long userId, Principal principal) {

        return ResponseEntity.ok(postService.getAllPostsByUserIdPageable(userId, page, principal));
    }

}
