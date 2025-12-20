//package org.olaz.instasprite_be.domain.mention.service;
//
//import static org.olaz.instasprite_be.domain.alarm.dto.AlarmType.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import lombok.RequiredArgsConstructor;
//
//import org.olaz.instasprite_be.domain.alarm.service.AlarmService;
//import org.olaz.instasprite_be.domain.feed.entity.Comment;
//import org.olaz.instasprite_be.domain.feed.entity.Post;
//import org.olaz.instasprite_be.domain.member.entity.Member;
//import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
//import org.olaz.instasprite_be.domain.mention.entity.Mention;
//import org.olaz.instasprite_be.domain.mention.repository.MentionRepository;
//import org.olaz.instasprite_be.global.util.StringExtractUtil;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class MentionService {
//
//	private final MentionRepository mentionRepository;
//	private final MemberRepository memberRepository;
//	private final AlarmService alarmService;
//	private final StringExtractUtil stringExtractUtil;
//
//	@Transactional
//	public void deleteAll(Post post) {
//		final List<Mention> mentions = mentionRepository.findAllByPost(post);
//		mentionRepository.deleteAllInBatch(mentions);
//	}
//
//	@Transactional
//	public void deleteAll(List<Comment> comments) {
//		final List<Mention> mentions = mentionRepository.findAllByCommentIn(comments);
//		mentionRepository.deleteAllInBatch(mentions);
//	}
//
//	@Transactional
//	public void mentionMembers(Member member, Post post) {
//		final List<String> usernames = stringExtractUtil.extractMentionsWithExceptList(post.getContent(),
//			List.of(member.getUsername()));
//		final List<Member> mentionedMembers = memberRepository.findAllByUsernameIn(usernames);
//		mentionRepository.savePostMentionsBatch(member.getId(), mentionedMembers, post.getId(), LocalDateTime.now());
//		alarmService.alertBatch(MENTION_POST, mentionedMembers, post);
//	}
//
//	@Transactional
//	public void mentionMembers(Member member, Comment comment) {
//		final List<String> usernames = stringExtractUtil.extractMentionsWithExceptList(comment.getContent(),
//			List.of(member.getUsername()));
//		final List<Member> mentionedMembers = memberRepository.findAllByUsernameIn(usernames);
//		mentionRepository.saveCommentMentionsBatch(member.getId(), mentionedMembers, comment.getPost().getId(),
//			comment.getId(), LocalDateTime.now());
//		alarmService.alertBatch(MENTION_COMMENT, mentionedMembers, comment.getPost(), comment);
//	}
//
//	public List<Mention> getMentionsWithTargetByPostId(Long postId) {
//		return mentionRepository.findAllWithTargetByPostId(postId);
//	}
//
//	public List<Mention> getMentionsWithTargetByCommentId(Long commentId) {
//		return mentionRepository.findAllWithTargetByPostId(commentId);
//	}
//
//}
