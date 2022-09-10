package com.velikiyprikalel.myBlog.posts.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.velikiyprikalel.myBlog.jpa.PostRepository;
import com.velikiyprikalel.myBlog.plains.Post;
import com.velikiyprikalel.myBlog.posts.api.PostService;

@Service()
@Qualifier("DatabasePostService")
public class DatabasePostService implements PostService {

    private final PostRepository postRepository;

    public DatabasePostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<? extends Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<? extends Post> getWhereTitleContainString(String query) {
        return postRepository.findByTitleContainingIgnoreCase(query);
    }
    
}
