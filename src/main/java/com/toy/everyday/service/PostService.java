package com.toy.everyday.service;

import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.MemberPost;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.MemberPostRepository;
import com.toy.everyday.repository.PostRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberPostRepository memberPostRepository;

    @Transactional
    public void update(Long id, String subject, String content){
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        post.update(subject, content);
    }

    @Transactional
    public void updateView(Long id, int viewCount){
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        post.update(viewCount + 1);
    }

    @Transactional
    public void vote(Post post, Member member){
        List<MemberPost> byMemberAndPost = memberPostRepository.findByMemberAndPost(member, post);

        if (byMemberAndPost.isEmpty()){
            MemberPost memberPost = MemberPost.builder()
                    .member(member)
                    .post(post)
                    .build();
            memberPostRepository.save(memberPost);
        }

    }
    private Specification<Post> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Post> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Post, Member> u1 = q.join("author", JoinType.LEFT);
                Join<Post, Comment> a = q.join("commentList", JoinType.LEFT);
                Join<Comment, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
    public Page<Post> getList(int page, String kw, String sort) {
        Pageable pageable = null;
        if (sort.equals("1")){
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("createDate"));
            pageable = PageRequest.of(page, 20,Sort.by(sorts));
            return this.postRepository.findAllByKeyword(kw, pageable);

        }else if (sort.equals("2")){
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("recommended"));
            pageable = PageRequest.of(page, 20,Sort.by(sorts));
            return this.postRepository.findAllByKeyword(kw, pageable);
        } else {
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("viewCount"));
            pageable = PageRequest.of(page, 20,Sort.by(sorts));
            return this.postRepository.findAllByKeyword(kw, pageable);

        }

    }

}
