package com.toy.everyday.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinDto {

    private String name;
    private String password;
    private String email;


}
