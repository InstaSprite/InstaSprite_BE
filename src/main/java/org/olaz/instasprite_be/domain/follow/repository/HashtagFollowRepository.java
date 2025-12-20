//package org.olaz.instasprite_be.domain.follow.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.olaz.instasprite_be.domain.follow.entity.HashtagFollow;
//
//public interface HashtagFollowRepository extends JpaRepository<HashtagFollow, Long> {
//
//	boolean existsByMemberIdAndHashtagId(Long memberId, Long hashtagId);
//
//	Optional<HashtagFollow> findByMemberIdAndHashtagId(Long memberId, Long hashtagId);
//
//}
