package org.olaz.instasprite_be.domain.member.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.querydsl.MemberPostRepositoryQuerydsl;
import org.olaz.instasprite_be.domain.member.repository.querydsl.MemberRepositoryQuerydsl;

public interface MemberRepository
	extends JpaRepository<Member, Long>, MemberPostRepositoryQuerydsl, MemberRepositoryQuerydsl {

	Optional<Member> findByUsername(String username);

	Optional<Member> findByGoogleId(String googleId);

	Optional<Member> findByEmail(String email);

	boolean existsByGoogleId(String googleId);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	List<Member> findAllByUsernameIn(Collection<String> usernames);

	List<Member> findAllByIdIn(Collection<Long> ids);

}

