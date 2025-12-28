package org.olaz.instasprite_be.domain.notification.repository;

import org.olaz.instasprite_be.domain.notification.entity.MemberFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberFcmTokenRepository extends JpaRepository<MemberFcmToken, Long> {

    boolean existsByMember_IdAndToken(Long memberId, String token);

    void deleteByMember_IdAndToken(Long memberId, String token);

    @Query("select t.token from MemberFcmToken t where t.member.id = :memberId")
    List<String> findTokensByMemberId(@Param("memberId") Long memberId);
}


