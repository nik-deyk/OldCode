package com.velikiyprikalel.myBlog.posts.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.velikiyprikalel.myBlog.plains.Post;
import com.velikiyprikalel.myBlog.posts.api.PostService;

@Service()
public class PostServiceInMemory implements PostService {

    private List<Post> posts = new ArrayList<>(Arrays.asList(Post.builder()
            .title("How to hack Pentagon?")
            .body("It's simple: just run <i>import hack</i> - that`s all!")
            .image("1.jpg")
            .build(),
            Post.builder()
                    .title("How to be a good boy?")
                    .body("It's simple: just run <i>import good_boy</i> - that`s all!")
                    .image("2.jpg").build()));

    @Override
    public List<? extends Post> getAll() {
        return posts;
    }

    @Override
    public List<? extends Post> getWhereTitleContainString(String query) {
        return getAll().stream().filter(p -> p.getTitle().toLowerCase().contains(query.toLowerCase())).toList();
    }

}
