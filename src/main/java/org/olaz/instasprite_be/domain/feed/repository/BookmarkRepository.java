package org.olaz.instasprite_be.domain.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.feed.entity.Bookmark;
import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.member.entity.Member;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Optional<Bookmark> findByMemberAndPost(Member member, Post post);

}
