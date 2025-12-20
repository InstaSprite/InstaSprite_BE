//package org.olaz.instasprite_be.domain.feed.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import lombok.RequiredArgsConstructor;
//
//import org.olaz.instasprite_be.domain.feed.entity.Comment;
//import org.olaz.instasprite_be.domain.feed.entity.Post;
//import org.olaz.instasprite_be.domain.feed.entity.RecentComment;
//import org.olaz.instasprite_be.domain.feed.repository.CommentRepository;
//import org.olaz.instasprite_be.domain.feed.repository.RecentCommentRepository;
//import org.olaz.instasprite_be.domain.member.entity.Member;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class RecentCommentService {
//
//	private final RecentCommentRepository recentCommentRepository;
//	private final CommentRepository commentRepository;
//
//	@Transactional
//	public void deleteAll(Post post) {
//		final List<RecentComment> recentComments = recentCommentRepository.findAllByPost(post);
//		recentCommentRepository.deleteAllInBatch(recentComments);
//	}
//
//	@Transactional
//	public void updateByUploadingComment(Post post, Member member, Comment comment) {
//		final List<RecentComment> recentComments = recentCommentRepository.findAllByPost(post);
//		if (recentComments.size() == 2) {
//			final RecentComment recentComment =
//				recentComments.get(0).getId() < recentComments.get(1).getId() ? recentComments.get(0) :
//					recentComments.get(1);
//			recentCommentRepository.delete(recentComment);
//		}
//		recentCommentRepository.save(new RecentComment(member, post, comment));
//	}
//
//	@Transactional
//	public void updateByDeletingComment(Member member, Post post, Comment comment) {
//		final List<RecentComment> recentComments = recentCommentRepository.findAllWithCommentByPostId(post.getId());
//
//		for (RecentComment recentComment : recentComments) {
//			if (recentComment.getComment().getId().equals(comment.getId())) {
//				final List<Long> ids = recentComments.stream()
//					.map(rc -> rc.getComment().getId())
//					.collect(Collectors.toList());
//
//				recentCommentRepository.delete(recentComment);
//				commentRepository.findFirstByPostAndIdNotInOrderByIdDesc(post, ids).ifPresent(
//					c -> recentCommentRepository.save(new RecentComment(member, post, c))
//				);
//
//				return;
//			}
//		}
//	}
//
//}
