package org.olaz.instasprite_be.domain.member.repository;

import org.olaz.instasprite_be.domain.member.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            delete from PasswordResetToken t
            where t.member.id = :memberId
              and t.usedAt is null
            """)
    int deleteAllUnusedByMemberId(@Param("memberId") Long memberId);
}

