package com.toy.everyday.controller;

import com.toy.everyday.dto.PostDto;
import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.CommentRepository;
import com.toy.everyday.repository.MemberRepository;
import com.toy.everyday.repository.PostRepository;
import com.toy.everyday.service.PostService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostService postService;
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="sort", defaultValue = "recent") String sort,
                       @RequestParam(value="page", defaultValue = "0") int page,
                       @RequestParam(value="kw", defaultValue = "") String kw){

        Page<Post> list = postService.getList(page, kw, sort);

        model.addAttribute("paging", list);
        model.addAttribute("kw", kw);
        model.addAttribute("sort", sort);

        return "post_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String postCreate(PostDto postDto){
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String postCreate(@Valid PostDto postDto, BindingResult bindingResult, Principal principal){

        Optional<Member> byEmail = memberRepository.findByEmail(principal.getName());

        if(bindingResult.hasErrors()){
            return "post_form";
        }

        if (byEmail.isPresent()){
            Member member = byEmail.get();
            Post post = Post.builder()
                    .subject(postDto.getSubject())
                    .content(postDto.getContent())
                    .author(member)
                    .viewCount(0)
                    .isModified(false)
                    .createDate(LocalDateTime.now())
                    .build();
            postRepository.save(post);
        } else {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }



        return "redirect:/post/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        // 해당 post가져오기
        Optional<Post> byId = postRepository.findById(id.longValue());
        if(byId.isPresent()){
            Post post = byId.get();
            model.addAttribute("post", post);
            postService.updateView(id.longValue(), post.getViewCount());
        }else{
            new RuntimeException("게시글이 존재하지 않습니다.");
        }

        return "post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String postModify(PostDto postDto, Principal principal, @PathVariable("id") Integer id){
        Optional<Post> byId = postRepository.findById(Long.valueOf(id));

        if(byId.isPresent()){
            Post post = byId.get();
            if (!post.getAuthor().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            //수정 코드 만들기
            postDto.setSubject(post.getSubject());
            postDto.setContent(post.getContent());
            return "post_form";

        } else {
            throw new RuntimeException("해당 게시글이 존재하지 않습니다.");
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String postModify(@Valid PostDto postDto, BindingResult bindingResult,
                             Principal principal, @PathVariable("id") Integer id){
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        Post post = postRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        if (!post.getAuthor().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postService.update(id.longValue(), postDto.getSubject(), postDto.getContent());

        return String.format("redirect:/post/detail/%s", id);


    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String postDelete(@PathVariable("id") Integer id, Principal principal){
        Post post = postRepository.findById(id.longValue()).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        if (!post.getAuthor().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.postRepository.delete(post);

        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String postVote(Principal principal, @PathVariable("id") Integer id){
        Post post = postRepository.findById(id.longValue()).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        postService.vote(post, member);

        return String.format("redirect:/post/detail/%s", id);

    }






}
