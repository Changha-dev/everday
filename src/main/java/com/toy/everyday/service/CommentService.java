package com.toy.everyday.service;

import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void update(Long id, String content){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        comment.update(content);

    }
}
