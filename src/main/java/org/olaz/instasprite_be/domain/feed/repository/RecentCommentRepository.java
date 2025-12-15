package org.olaz.instasprite_be.domain.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.feed.entity.RecentComment;

public interface RecentCommentRepository extends JpaRepository<RecentComment, Long> {

	@Query("select rc from RecentComment rc join fetch rc.comment where rc.post.id = :id")
	List<RecentComment> findAllWithCommentByPostId(@Param("id") Long id);

	List<RecentComment> findAllByPost(Post post);

}
