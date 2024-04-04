package com.shop.doubleu.common.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.shop.doubleu.member.entity.Member;
import com.shop.doubleu.member.service.MemberService;
import com.shop.global.config.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findById(userId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}

