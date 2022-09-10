package com.velikiyprikalel.myBlog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.velikiyprikalel.myBlog.posts.api.PostService;

@Controller
public class PostController {
    
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "hello";
    }
}
