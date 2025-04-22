package com.academix.userservice.repository;

import com.academix.userservice.dao.RefreshToken;
import com.academix.userservice.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);

    @Query("FROM RefreshToken rt WHERE rt.user.id=:userId")
    Optional<RefreshToken> findByUserId(Long userId);

}
