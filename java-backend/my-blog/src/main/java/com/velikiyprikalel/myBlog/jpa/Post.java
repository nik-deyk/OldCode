package com.velikiyprikalel.myBlog.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
public class Post extends com.velikiyprikalel.myBlog.plains.Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter 
    @Setter 
    private Long id;

    public Post() {
        super();
        id = null;
    }

    public Post(Long id, String title, String body, String image) {
        super(title, body, image);
        this.id = id;
    }
}
