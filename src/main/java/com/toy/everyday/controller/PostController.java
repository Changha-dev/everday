package com.toy.everyday.controller;

import com.toy.everyday.dto.PostDto;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;
    @GetMapping("/list")
    public String list(Model model){
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);
        return "post_list";
    }

    @GetMapping("/create")
    public String postCreate(PostDto postDto){
        return "post_form";
    }

    @PostMapping("/create")
    public String postCreate(@Valid PostDto postDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "post_form";
        }

        Post post = Post.builder()
                .subject(postDto.getSubject())
                .content(postDto.getContent())
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        return "redirect:/post/list";
    }

//    @PostMapping("/detail/{id}")
//    public String detail(Model model, @PathVariable("id") Integer id){
//        // 해당 post가져오기
//
//        return "post_detail";
//    }


}
