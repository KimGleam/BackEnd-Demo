package com.shop.doubleu.member.service;

import org.springframework.stereotype.Service;

import com.shop.doubleu.member.entity.Member;
import com.shop.doubleu.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberDetailService {

    private final MemberRepository memberRepository;

    public Member loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                 .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
