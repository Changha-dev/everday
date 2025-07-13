package com.toy.everyday.service;

import com.toy.everyday.entity.*;
import com.toy.everyday.repository.CommentRepository;
import com.toy.everyday.repository.MemberCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberCommentRepository memberCommentRepository;

    @Transactional
    public void update(Long id, String content){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        comment.update(content);

    }

    @Transactional
    public void vote(Comment comment, Member member){
        List<MemberComment> byMemberAndComment = memberCommentRepository.findByMemberAndComment(member, comment);



        if (byMemberAndComment.isEmpty()){
            MemberComment memberComment = MemberComment.builder()
                    .member(member)
                    .comment(comment)
                    .build();
            memberCommentRepository.save(memberComment);
        }

    }
}
