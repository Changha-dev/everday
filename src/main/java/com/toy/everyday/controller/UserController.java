package com.toy.everyday.controller;

import com.toy.everyday.UserCreateForm;
import com.toy.everyday.dto.JoinDto;
import com.toy.everyday.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(UserCreateForm userCreateForm){
        return "join_form";
    }
    @PostMapping("/join")
    public String join(@Valid UserCreateForm userCreateForm, BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "join_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "join_form";
        }

        try {
            memberService.join(JoinDto.builder()
                            .email(userCreateForm.getEmail())
                            .name(userCreateForm.getName())
                            .password(userCreateForm.getPassword1())
                    .build());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", "이미 등록된 사용자입니다.");
            return "join_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", e.getMessage());
            return "join_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login_form";
    }
}
