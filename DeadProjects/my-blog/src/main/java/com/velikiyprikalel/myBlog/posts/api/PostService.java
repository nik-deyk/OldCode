package com.velikiyprikalel.myBlog.posts.api;

import java.util.List;

import com.velikiyprikalel.myBlog.plains.Post;

public interface PostService {
    List<? extends Post> getAll();

    List<? extends Post> getWhereTitleContainString(String query);
}
