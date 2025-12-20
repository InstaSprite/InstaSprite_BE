//package org.olaz.instasprite_be.domain.member.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.olaz.instasprite_be.domain.member.entity.Block;
//import org.olaz.instasprite_be.domain.member.repository.querydsl.BlockRepositoryQuerydsl;
//
//public interface BlockRepository extends JpaRepository<Block, Long>, BlockRepositoryQuerydsl {
//
//	boolean existsByMemberIdAndBlockMemberId(Long memberId, Long blockMemberId);
//
//	Optional<Block> findByMemberIdAndBlockMemberId(Long memberId, Long blockMemberId);
//
//}
//
