package org.olaz.instasprite_be.domain.hashtag.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;
import org.olaz.instasprite_be.domain.hashtag.entity.HashtagPost;
import org.olaz.instasprite_be.domain.hashtag.repository.jdbc.HashtagPostRepositoryJdbc;
import org.olaz.instasprite_be.domain.hashtag.repository.querydsl.HashtagPostRepositoryQuerydsl;

public interface HashtagPostRepository extends JpaRepository<HashtagPost, Long>, HashtagPostRepositoryJdbc,
	HashtagPostRepositoryQuerydsl {

	List<HashtagPost> findAllByPost(Post post);

	List<HashtagPost> findByHashtagAndPost(Hashtag hashtag, Post post);

	List<HashtagPost> findAllByPostAndHashtagIn(Post post, List<Hashtag> hashtags);

	@Query("select hp from HashtagPost hp join fetch hp.post where hp.hashtag.id = :hashtagId")
	List<HashtagPost> findAllWithPostByHashtagId(Pageable pageable, @Param("hashtagId") Long hashtagId);

}
