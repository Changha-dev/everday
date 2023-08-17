package com.toy.everyday.controller;

import com.toy.everyday.dto.CommentDto;
import com.toy.everyday.dto.PostDto;
import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.CommentRepository;
import com.toy.everyday.repository.MemberRepository;
import com.toy.everyday.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(@PathVariable("id") Integer id, @Valid CommentDto commentDto,
                                BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "post_form";
        }
        Optional<Post> post = postRepository.findById(id.longValue());
        Optional<Member> member = memberRepository.findByEmail(principal.getName());
        if(post.isPresent()){
            Post post1 = post.get();

            if(member.isPresent()){

                Member member1 = member.get();

                Comment comment = Comment.builder()
                        .content(commentDto.getContent())
                        .createDate(LocalDateTime.now())
                        .author(member1)
                        .post(post1)
                        .build();
                commentRepository.save(comment);

            } else {
                throw new RuntimeException("유저가 존재하지 않습니다.");
            }
            // 댓글 저장




        }else{
            new RuntimeException("게시글이 존재하지 않습니다.");
        }

        return String.format("redirect:/post/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(CommentDto commentDto, @PathVariable("id") Integer id, Principal principal) {
        Optional<Comment> byId = commentRepository.findById(id.longValue());
        if (byId.isPresent()){
            Comment comment = byId.get();
            if(!comment.getAuthor().getEmail().equals(principal.getName())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }

            commentDto.setContent(comment.getContent());
            // commentDto 어떻게 수정하지

        }else {
            throw new RuntimeException("해당 댓글이 존재하지 않습니다. ");
        }

        return "comment_form";
    }
}
