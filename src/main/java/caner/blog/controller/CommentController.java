package caner.blog.controller;

import caner.blog.dto.request.CreateCommentRequest;
import caner.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{id}/create-comment")
    public String createComment(@Valid @ModelAttribute("comment") CreateCommentRequest createCommentRequest,
                                BindingResult result, @PathVariable Long id,
                                Principal principal,RedirectAttributes redirectAttributes) {

        if (createCommentRequest.getComment().length() >= 1000) {
            redirectAttributes.addFlashAttribute("commentSizeError",
                    "Yorumunuz maksimum 1000 karakter olmak zorundadır!");
            return "redirect:/posts/{id}";
        }

        if (result.hasErrors()) {
            return "redirect:/posts/{id}";
        }

        String userEmail = principal.getName();
        commentService.createComment(createCommentRequest, id, userEmail);

        return "redirect:/posts/{id}";
    }

}
