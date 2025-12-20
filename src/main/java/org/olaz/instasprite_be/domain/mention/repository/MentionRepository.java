//package org.olaz.instasprite_be.domain.mention.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import org.olaz.instasprite_be.domain.feed.entity.Comment;
//import org.olaz.instasprite_be.domain.feed.entity.Post;
//import org.olaz.instasprite_be.domain.mention.entity.Mention;
//import org.olaz.instasprite_be.domain.mention.repository.jdbc.MentionRepositoryJdbc;
//
//public interface MentionRepository extends JpaRepository<Mention, Long>, MentionRepositoryJdbc {
//
//	List<Mention> findAllByPost(Post post);
//
//	List<Mention> findAllByCommentIn(List<Comment> comments);
//
//	@Query("select m from Mention m join fetch m.target where m.post.id = :postId")
//	List<Mention> findAllWithTargetByPostId(@Param("postId") Long postId);
//
//	@Query("select m from Mention m join fetch m.target where m.comment.id = :commentId")
//	List<Mention> findAllWithTargetByCommentId(@Param("commentId") Long commentId);
//
//}
