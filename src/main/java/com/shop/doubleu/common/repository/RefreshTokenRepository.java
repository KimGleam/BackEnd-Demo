package com.shop.doubleu.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.doubleu.common.model.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

