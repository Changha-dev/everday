package com.toy.everyday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Member author;

    private boolean isModified;

    public void update(String content) {
        this.content = content;
        this.isModified = true;
    }

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<MemberComment> memberComments = new ArrayList<>();

}
