package org.olaz.instasprite_be.domain.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.feed.entity.Comment;
import org.olaz.instasprite_be.domain.feed.entity.CommentLike;
import org.olaz.instasprite_be.domain.member.entity.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

	List<CommentLike> findAllByCommentIn(List<Comment> comments);

	Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);

}
