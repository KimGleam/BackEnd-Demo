package com.example.demo.common.service;

import org.springframework.stereotype.Service;

import com.example.demo.common.model.entity.RefreshToken;
import com.example.demo.common.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}

