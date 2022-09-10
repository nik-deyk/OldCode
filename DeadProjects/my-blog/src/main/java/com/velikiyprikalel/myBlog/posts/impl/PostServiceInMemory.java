package com.velikiyprikalel.myBlog.posts.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.velikiyprikalel.myBlog.plains.Post;
import com.velikiyprikalel.myBlog.posts.api.PostService;

@Service
public class PostServiceInMemory implements PostService {

    private List<Post> posts = new ArrayList<>(Arrays.asList(Post.builder()
            .tittle("How to hack Pentagon?")
            .body("It's simple: just run <i>import hack</i> - that`s all!").build(),
            Post.builder()
                .tittle("How to be a good boy?")
                .body("It's simple: just run <i>import good_boy</i> - that`s all!").build()));

    @Override
    public List<Post> getAll() {
        return posts;
    }

}
