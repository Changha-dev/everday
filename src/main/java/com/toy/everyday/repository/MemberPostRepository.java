package com.toy.everyday.repository;

import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.MemberPost;
import com.toy.everyday.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPostRepository extends JpaRepository<MemberPost, Long> {
    List<MemberPost> findByMemberAndPost(Member member, Post post);
}
