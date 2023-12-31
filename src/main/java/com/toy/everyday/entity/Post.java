package com.toy.everyday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @ManyToOne
    private Member author;

    private boolean isModified;

    private int viewCount;

    @OneToMany(mappedBy = "post")
    private List<MemberPost> memberPosts = new ArrayList<>();

    private int recommended = memberPosts.size();

    public void update(String subject, String content) {
        this.subject = subject;
        this.content = content;
        this.isModified = true;
    }

    public void update(int viewCount){
        this.viewCount = viewCount;
    }
}
