package com.velikiyprikalel.myBlog.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.velikiyprikalel.myBlog.posts.api.PostService;

@Controller
public class PostController {

    private final PostService postService;
    
    public PostController(@Qualifier("DatabasePostService") PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String getAll(Model model, @RequestParam(required = false) String query) {
        model.addAttribute("posts",
                query == null || query.isEmpty() ? postService.getAll()
                        : postService.getWhereTitleContainString(query));
        return "hello";
    }
}
