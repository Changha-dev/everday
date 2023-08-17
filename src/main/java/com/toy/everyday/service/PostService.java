package com.toy.everyday.service;

import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void update(Long id, String subject, String content){
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        post.update(subject, content);


    }
}
