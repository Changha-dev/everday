package com.toy.everyday.repository;

import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.MemberComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCommentRepository extends JpaRepository<MemberComment, Long> {

    List<MemberComment> findByMemberAndComment(Member member, Comment comment);
}
