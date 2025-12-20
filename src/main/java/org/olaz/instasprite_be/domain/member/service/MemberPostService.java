package org.olaz.instasprite_be.domain.member.service;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.MemberPostDto;
import org.olaz.instasprite_be.domain.feed.dto.PostImageDto;
import org.olaz.instasprite_be.domain.feed.repository.PostImageRepository;
import org.olaz.instasprite_be.domain.feed.service.PostService;
import org.olaz.instasprite_be.domain.member.entity.Member;
//import org.olaz.instasprite_be.domain.member.repository.BlockRepository;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.global.error.exception.EntityNotFoundException;
import org.olaz.instasprite_be.global.util.AuthUtil;

import static org.olaz.instasprite_be.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPostService {

	private final AuthUtil authUtil;
	private final MemberRepository memberRepository;
//	private final BlockRepository blockRepository;
	private final PostImageRepository postImageRepository;
	private final PostService postService;

	public Page<MemberPostDto> getMemberPostDtoPageWithoutLogin(String username, int size, int page) {
		final Pageable pageable = PageRequest.of(page, size);
		final Page<MemberPostDto> posts = getMemberPostDtoPage(-1L, username, pageable);
		final List<MemberPostDto> content = posts.getContent();
		setMemberPostImageDtos(content);
		setPostLikesCount(null, content);
		return posts;
	}

	public Page<MemberPostDto> getMemberPostDtoPage(String username, int size, int page) {
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size);
		final Page<MemberPostDto> posts = getMemberPostDtoPage(loginMember.getId(), username, pageable);
		final List<MemberPostDto> content = posts.getContent();
		setMemberPostImageDtos(content);
		setPostLikesCount(loginMember, content);
		return posts;
	}

	public Page<MemberPostDto> getMemberSavedPostPage(int size, int page) {
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size);
		final Page<MemberPostDto> posts = memberRepository.findMemberSavedPostDtoPageByLoginMemberId(
			loginMember.getId(), pageable);
		final List<MemberPostDto> content = posts.getContent();
		setMemberPostImageDtos(content);
		setPostLikesCount(loginMember, content);
		return posts;
	}

//	public Page<MemberPostDto> getMemberTaggedPostDtoPage(String username, int size, int page) {
//		final Member loginMember = authUtil.getLoginMember();
//		final Member member = memberRepository.findByUsername(username)
//			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
//
////		if (blockRepository.isBlockingOrIsBlocked(loginMember.getId(), member.getId())) {
////			return Page.empty();
////		}
//
//		final Pageable pageable = PageRequest.of(page, size);
//		final Page<MemberPostDto> posts = memberRepository.findMemberTaggedPostDtoPageByLoginMemberIdAndTargetUsername(
//			loginMember.getId(), username,
//			pageable);
//		final List<MemberPostDto> content = posts.getContent();
//		setMemberPostImageDtos(content);
//		setPostLikesCount(loginMember, content);
//		return posts;
//	}

	private Page<MemberPostDto> getMemberPostDtoPage(Long memberId, String username, Pageable pageable) {
		final Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

//		if (blockRepository.isBlockingOrIsBlocked(memberId, member.getId())) {
//			return Page.empty();
//		}

		return memberRepository.findMemberPostDtoPageByLoginMemberIdAndTargetUsername(memberId, username, pageable);
	}

	private void setPostLikesCount(Member loginMember, List<MemberPostDto> content) {
		content.forEach(post -> {
			if (loginMember != null && !post.getMember().getId().equals(loginMember.getId())) {
				final int count = postService.countOfFollowingsFromPostLikes(post.getPostId(), loginMember);
				post.setPostLikesCount(count);
			} else if (post.isPostLikeFlag()) {
				post.setPostLikesCount(post.getPostLikesCount() + 1);
			}
		});
	}

	private void setMemberPostImageDtos(List<MemberPostDto> memberPostDtos) {
		final List<Long> postIds = memberPostDtos.stream()
			.map(MemberPostDto::getPostId)
			.collect(Collectors.toList());

		final List<PostImageDto> postImageDtos = postImageRepository.findAllPostImageDtoByPostIdIn(postIds);

		final Map<Long, List<PostImageDto>> postDTOMap = postImageDtos.stream()
			.collect(Collectors.groupingBy(PostImageDto::getPostId));

		memberPostDtos.forEach(p -> p.setPostImage(postDTOMap.get(p.getPostId()).get(0)));
	}

}
