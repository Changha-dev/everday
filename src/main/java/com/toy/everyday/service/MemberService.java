package com.toy.everyday.service;

import com.toy.everyday.dto.JoinDto;
import com.toy.everyday.entity.Member;
import com.toy.everyday.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(JoinDto joinDto){
        Member member = Member.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .username(joinDto.getName())
                .build();

        memberRepository.save(member);

    }

}
