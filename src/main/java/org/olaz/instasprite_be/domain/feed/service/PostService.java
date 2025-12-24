package org.olaz.instasprite_be.domain.feed.service;

//import static org.olaz.instasprite_be.domain.alarm.dto.AlarmType.*;
import static org.olaz.instasprite_be.global.error.ErrorCode.*;
import static org.olaz.instasprite_be.global.util.ConstantUtils.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

//import org.olaz.instasprite_be.domain.alarm.service.AlarmService;
import org.olaz.instasprite_be.domain.feed.dto.CommentDto;
import org.olaz.instasprite_be.domain.feed.dto.CursorPageResponse;
import org.olaz.instasprite_be.domain.feed.dto.PostDto;
import org.olaz.instasprite_be.domain.feed.dto.PostImageDto;
//import org.olaz.instasprite_be.domain.feed.dto.PostImageTagRequest;
import org.olaz.instasprite_be.domain.feed.dto.PostLikeCountDto;
import org.olaz.instasprite_be.domain.feed.dto.PostLikeDto;
//import org.olaz.instasprite_be.domain.feed.dto.PostTagDto;
import org.olaz.instasprite_be.domain.feed.dto.PostUploadRequest;
import org.olaz.instasprite_be.domain.feed.dto.PostUploadResponse;
import org.olaz.instasprite_be.domain.feed.entity.Bookmark;
import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.feed.entity.PostLike;
import org.olaz.instasprite_be.domain.feed.exception.BookmarkMyselfFailException;
import org.olaz.instasprite_be.domain.feed.exception.CantDeletePostException;
import org.olaz.instasprite_be.domain.feed.repository.BookmarkRepository;
import org.olaz.instasprite_be.domain.feed.repository.CommentRepository;
import org.olaz.instasprite_be.domain.feed.repository.PostImageRepository;
import org.olaz.instasprite_be.domain.feed.repository.PostLikeRepository;
import org.olaz.instasprite_be.domain.feed.repository.PostRepository;
//import org.olaz.instasprite_be.domain.feed.repository.PostTagRepository;
import org.olaz.instasprite_be.domain.follow.entity.Follow;
import org.olaz.instasprite_be.domain.follow.service.FollowService;
//import org.olaz.instasprite_be.domain.hashtag.repository.HashtagPostRepository;
//import org.olaz.instasprite_be.domain.hashtag.repository.HashtagRepository;
//import org.olaz.instasprite_be.domain.hashtag.service.HashtagService;
import org.olaz.instasprite_be.domain.member.dto.LikeMemberDto;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
//import org.olaz.instasprite_be.domain.mention.service.MentionService;
import org.olaz.instasprite_be.global.error.ErrorResponse.FieldError;
import org.olaz.instasprite_be.global.error.exception.EntityAlreadyExistException;
import org.olaz.instasprite_be.global.error.exception.EntityNotFoundException;
import org.olaz.instasprite_be.global.error.exception.InvalidInputException;
import org.olaz.instasprite_be.global.util.AuthUtil;
import org.olaz.instasprite_be.global.util.StringExtractUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

	private final AuthUtil authUtil;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final PostLikeRepository postLikeRepository;
	private final PostImageRepository postImageRepository;
//	private final PostTagRepository postTagRepository;
	private final BookmarkRepository bookmarkRepository;
	private final CommentRepository commentRepository;
//	private final HashtagRepository hashtagRepository;
//	private final HashtagPostRepository hashtagPostRepository;
//	private final AlarmService alarmService;
	private final CommentService commentService;
//	private final MentionService mentionService;
//	private final HashtagService hashtagService;
	private final PostLikeService postLikeService;
	private final PostImageService postImageService;
	private final FollowService followService;
	private final StringExtractUtil stringExtractUtil;

	@Transactional
	public PostUploadResponse upload(PostUploadRequest request) {
		final List<MultipartFile> postImages = request.getPostImages();
		final List<String> altTexts = request.getAltTexts();
//		final List<PostImageTagRequest> postImageTags = request.getPostImageTags();
		validateParameters(postImages, altTexts);
		final List<String> normalizedAltTexts = normalizeAltTexts(postImages, altTexts);

		final Member loginMember = authUtil.getLoginMember();
		final Post post = Post.builder()
			.content(request.getContent())
			.member(loginMember)
			.commentFlag(request.isCommentFlag())
			.build();

		postRepository.save(post);
		postImageService.saveAll(post, postImages, normalizedAltTexts);
//		hashtagService.registerHashtags(post);
//		mentionService.mentionMembers(loginMember, post);

//		final Set<String> taggedMemberUsernames = request.getPostImageTags().stream()
//			.map(PostImageTagRequest::getUsername)
//			.collect(toSet());
//		taggedMemberUsernames.remove(loginMember.getUsername());

		return new PostUploadResponse(post.getId());
	}

	@Transactional
	public void delete(Long postId) {
		final Member loginMember = authUtil.getLoginMember();
		final Post post = getPostWithMember(postId);

		if (!post.getMember().getId().equals(loginMember.getId())) {
			throw new CantDeletePostException();
		}

//		alarmService.deleteAll(post);
//		hashtagService.unregisterHashtagsByDeletingPost(post);
//		mentionService.deleteAll(post);
		postLikeService.deleteAll(post);
		postImageService.deleteAll(post);
		commentService.deleteAllInPost(post);
		postRepository.delete(post);
	}

	public Page<PostDto> getPostDtoPageForFollowedUsers(int size, int page) {
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size);
		final Page<PostDto> postDtoPage = postRepository.findPostDtoPageOfFollowingMembersOrHashtagsByMemberId(
			loginMember.getId(), pageable);
		setContents(loginMember, postDtoPage.getContent());
		return postDtoPage;
	}

	public Page<PostDto> getPostDtoPageForAllUsers(int size, int page) {
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size);
		final Page<PostDto> postDtoPage = postRepository.findAllPostDtoPage(pageable, loginMember.getId());
		setContents(loginMember, postDtoPage.getContent());
		return postDtoPage;
	}

	public CursorPageResponse<PostDto> getPostDtoWithCursor(Long cursor, int size) {
		final Member loginMember = authUtil.getLoginMember();
		final List<PostDto> postDtos = postRepository.findAllPostDtoWithCursor(cursor, size, loginMember.getId());
		
		Long nextCursor = null;
		if (postDtos.size() > size) {
			nextCursor = postDtos.get(size - 1).getPostId();
			postDtos.remove(size);
		}
		
		setContents(loginMember, postDtos);
		return CursorPageResponse.of(postDtos, nextCursor);
	}

	public Page<PostDto> searchPostsByContent(String query, int page, int size) {
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
		final Page<PostDto> postPage = postRepository.searchPostDtoByContentContains(query, pageable, loginMember.getId());
		setContents(loginMember, postPage.getContent());
		return postPage;
	}

	public PostDto getPostDto(Long postId) {
		final Member loginMember = authUtil.getLoginMember();
		final PostDto postDto = postRepository.findPostDtoByPostIdAndMemberId(postId, loginMember.getId())
			.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
		setContent(loginMember, postDto);
		return postDto;
	}

	public PostDto getPostDtoWithoutLogin(Long postId) {
		final PostDto postDto = postRepository.findPostDtoWithoutLoginByPostId(postId)
			.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
		setContentWithoutLogin(postDto);
		return postDto;
	}

	public PostDto getMostLikedPostDto() {
		final PostDto postDto = postRepository.findMostLikedPostDto()
			.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
		setContentWithoutLogin(postDto);
		return postDto;
	}

	/**
	 * Among the people who liked this post, <br>
	 * count how many people the currently logged-in user is following.
	 */
	public int countOfFollowingsFromPostLikes(Long postId, Member loginMember) {
		final Set<Member> postLikeMembers = postLikeService.getAllWithMember(postId).stream()
			.map(PostLike::getMember)
			.collect(toSet());

		final List<Member> followings = followService.getFollowings(loginMember).stream()
			.map(Follow::getFollowMember)
			.toList();

		int count = 0;
		for (Member following : followings) {
			if (postLikeMembers.contains(following)) {
				count++;
			}
		}

		if (postLikeMembers.contains(loginMember)) {
			count++;
		}

		return count;
	}

	@Transactional
	public void likePost(Long postId) {
		final Post post = getPostWithMember(postId);
		final Member loginMember = authUtil.getLoginMember();

		if (postLikeRepository.findByMemberAndPost(loginMember, post).isPresent()) {
			throw new EntityAlreadyExistException(POST_LIKE_ALREADY_EXIST);
		}

		postLikeRepository.save(new PostLike(loginMember, post));
//		alarmService.alert(LIKE_POST, post.getMember(), post);
	}

	@Transactional
	public void unlikePost(Long postId) {
		final Post post = getPostWithMember(postId);
		final Member loginMember = authUtil.getLoginMember();
		postLikeRepository.delete(getPostLike(loginMember, post));
//		alarmService.delete(LIKE_POST, post.getMember(), post);
	}

	public Page<LikeMemberDto> getPostLikeMembersDtoPage(Long postId, int page, int size) {
		final Post post = getPost(postId);
		final Member loginMember = authUtil.getLoginMember();
		final Pageable pageable = PageRequest.of(page, size);

		Page<LikeMemberDto> likeMemberDtoPage;
		if (post.getMember().equals(loginMember)) {
			likeMemberDtoPage = postLikeRepository.findPostLikeMembersDtoPageExceptMeByPostIdAndMemberId(pageable,
				postId, loginMember.getId());
		} else {
			likeMemberDtoPage = postLikeRepository.findPostLikeMembersDtoPageOfFollowingsByMemberIdAndPostId(pageable,
				loginMember.getId(), postId);
		}

		final List<MemberDto> memberDtos = likeMemberDtoPage.getContent().stream()
			.map(LikeMemberDto::getMember)
			.toList();

		return likeMemberDtoPage;
	}

	@Transactional
	public void bookmark(Long postId) {
		final Post post = getPost(postId);
		final Member loginMember = authUtil.getLoginMember();

		if (post.getMember().getId().equals(loginMember.getId())) {
			throw new BookmarkMyselfFailException();
		}

		if (bookmarkRepository.findByMemberAndPost(loginMember, post).isPresent()) {
			throw new EntityAlreadyExistException(BOOKMARK_ALREADY_EXIST);
		}

		bookmarkRepository.save(new Bookmark(loginMember, post));
	}

	@Transactional
	public void unBookmark(Long postId) {
		bookmarkRepository.delete(getBookmark(authUtil.getLoginMember(), getPost(postId)));
	}


//	public Page<PostDto> getHashTagPosts(int page, int size, String name) {
//		final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
//		return hashtagRepository.findByName(name)
//			.map(hashtag -> {
//				final List<Long> postIds = hashtagPostRepository.findAllWithPostByHashtagId(pageable, hashtag.getId())
//					.stream()
//					.map(hashtagPost -> hashtagPost.getPost().getId())
//					.collect(toList());
//				final Member loginMember = authUtil.getLoginMember();
//				final Page<PostDto> postDtoPage = postRepository.findPostDtoPageByMemberIdAndPostIdIn(pageable,
//					loginMember.getId(), postIds);
//				setContents(loginMember, postDtoPage.getContent());
//				return postDtoPage;
//			})
//			.orElse(new PageImpl<>(new ArrayList<>()));
//	}

	private void setContents(Member loginMember, List<PostDto> postDtos) {
		final List<Long> postIds = postDtos.stream()
			.map(PostDto::getPostId)
			.collect(toList());
		final List<MemberDto> memberDtos = postDtos.stream()
			.map(PostDto::getMember)
			.toList();

		setPostImages(postDtos, postIds);
//		setRecentComments(loginMember.getId(), postDtos, postIds);
		setFollowingMemberUsernameLikedPost(loginMember, postDtos, postIds);
		setMentionAndHashtagList(postDtos);
	}

	private void setContent(Member loginMember, PostDto postDto) {
		setPostImages(List.of(postDto), List.of(postDto.getPostId()));
		setFollowingMemberUsernameLikedPost(loginMember, List.of(postDto), List.of(postDto.getPostId()));
		setComments(postDto);
		setMentionAndHashtagList(postDto);
	}

	private void setContentWithoutLogin(PostDto postDto) {
		setPostImages(List.of(postDto), List.of(postDto.getPostId()));
		setComments(postDto);
		postDto.setPostLikesCount(0);
//		setMentionAndHashtagList(postDto);
//		if (postDto.isPostLikeFlag()) {
//			postDto.setPostLikesCount(0);
//		}
	}

	private void setMentionAndHashtagList(PostDto postDto) {
		setMentionAndHashtagList(List.of(postDto));
	}

	private void setMentionAndHashtagList(List<PostDto> postDtos) {
		final List<String> mentionedUsernames = new ArrayList<>();
		postDtos.forEach(postDto -> mentionedUsernames.addAll(
			stringExtractUtil.extractMentionsWithExceptList(postDto.getPostContent(), mentionedUsernames)));
		final List<String> existentUsernames = memberRepository.findAllByUsernameIn(mentionedUsernames).stream()
			.map(Member::getUsername)
			.toList();

		postDtos.forEach(postDto -> {
			final List<String> mentionsOfContent = stringExtractUtil.extractMentions(postDto.getPostContent()).stream()
				.filter(existentUsernames::contains)
				.collect(toList());
			postDto.setMentionsOfContent(mentionsOfContent);

			final List<String> hashtagsOfContent = stringExtractUtil.extractHashtags(postDto.getPostContent());
			postDto.setHashtagsOfContent(hashtagsOfContent);
		});
	}

	private void validateParameters(List<MultipartFile> postImages, List<String> altTexts) {
		final List<FieldError> errors = new ArrayList<>();

		if (altTexts != null && !altTexts.isEmpty() && postImages.size() != altTexts.size()) {
			errors.add(new FieldError("postImages.size" + COMMA_WITH_BLANK + "altTexts.size",
				postImages.size() + COMMA_WITH_BLANK + altTexts.size(),
				POST_IMAGES_AND_ALT_TEXTS_MISMATCH.getMessage()));
		}

//		final Map<Long, List<PostImageTagRequest>> tagMapGroupByImageId = tags.stream()
//			.collect(groupingBy(PostImageTagRequest::getId));
//		for (Long imageId : tagMapGroupByImageId.keySet()) {
//			final int tagSize = tagMapGroupByImageId.get(imageId).size();
//			if (tagSize > 20) {
//				errors.add(new FieldError("postImageTags.size", String.valueOf(tagSize),
//					POST_TAGS_EXCEED.getMessage()));
//			}
//		}

//		final List<String> usernames = tags.stream()
//			.map(PostImageTagRequest::getUsername)
//			.collect(toList());
//		final Map<String, Member> usernameMap = memberRepository.findAllByUsernameIn(usernames).stream()
//			.collect(toMap(Member::getUsername, m -> m));

//		for (int i = 0; i < usernames.size(); i++) {
//			final String username = usernames.get(i);
//			if (!usernameMap.containsKey(username)) {
//				final String field = String.format("postImageTags[%s].username", i);
//				errors.add(new FieldError(field, username, MEMBER_NOT_FOUND.getMessage()));
//			}
//		}

		if (!errors.isEmpty()) {
			throw new InvalidInputException(errors);
		}
	}

	private List<String> normalizeAltTexts(List<MultipartFile> postImages, List<String> altTexts) {
		final List<String> normalizedAltTexts = new ArrayList<>();

		if (altTexts == null || altTexts.isEmpty()) {
			for (int i = 0; i < postImages.size(); i++) {
				normalizedAltTexts.add(null);
			}
			return normalizedAltTexts;
		}

		if (altTexts.size() != postImages.size()) {
			for (int i = 0; i < postImages.size(); i++) {
				normalizedAltTexts.add(i < altTexts.size() ? altTexts.get(i) : null);
			}
			return normalizedAltTexts;
		}

		normalizedAltTexts.addAll(altTexts);
		return normalizedAltTexts;
	}

	private void setPostImages(List<PostDto> postDtos, List<Long> postIds) {
		final List<PostImageDto> postImageDtos = postImageRepository.findAllPostImageDtoByPostIdIn(postIds);
		final List<Long> postImageIds = postImageDtos.stream()
			.map(PostImageDto::getId)
			.collect(toList());

//		setPostTags(postImageDtos, postImageIds);

		final Map<Long, List<PostImageDto>> postDtoMap = postImageDtos.stream()
			.collect(groupingBy(PostImageDto::getPostId));
		postDtos.forEach(p -> p.setPostImages(postDtoMap.get(p.getPostId())));
	}

//	private void setPostTags(List<PostImageDto> postImageDtos, List<Long> postImageIds) {
//		final List<PostTagDto> postTagDtos = postTagRepository.findAllPostTagDto(postImageIds);
//
//		final Map<Long, List<PostTagDto>> postImageDtoMap = postTagDtos.stream()
//			.collect(groupingBy(PostTagDto::getPostImageId));
//		postImageDtos.forEach(i -> i.setPostTags(postImageDtoMap.get(i.getId())));
//	}

	private void setFollowingMemberUsernameLikedPost(Member member, List<PostDto> postDtos, List<Long> postIds) {
		final Map<Long, List<PostLikeDto>> postLikeDtoMap =
			postLikeRepository.findAllPostLikeDtoOfFollowingsByMemberIdAndPostIdIn(member.getId(), postIds)
				.stream()
				.collect(groupingBy(PostLikeDto::getPostId));
		postDtos.forEach(postDto ->
			postDto.setFollowingMemberUsernameLikedPost(postLikeDtoMap.containsKey(postDto.getPostId())
				? postLikeDtoMap.get(postDto.getPostId()).get(ANY_INDEX).getUsername() : EMPTY));
	}

//	private void setRecentComments(Long memberId, List<PostDto> postDtos, List<Long> postIds) {
//		final Map<Long, List<CommentDto>> recentCommentMap =
//			commentRepository.findAllRecentCommentDtoByMemberIdAndPostIdIn(memberId, postIds).stream()
//				.collect(groupingBy(CommentDto::getPostId));
//
//		final List<CommentDto> totalCommentDtos = new ArrayList<>();
//		postDtos.forEach(postDto -> {
//			if (recentCommentMap.containsKey(postDto.getPostId())) {
//				final List<CommentDto> commentDtos = recentCommentMap.get(postDto.getPostId());
//				totalCommentDtos.addAll(commentDtos);
//				postDto.setRecentComments(commentDtos);
//			}
//		});
//		commentService.setMentionAndHashtagList(totalCommentDtos);
//
//		final List<MemberDto> totalMemberDtos = totalCommentDtos.stream()
//			.map(CommentDto::getMember)
//			.toList();
//	}

	private void setComments(PostDto postDto) {
		final List<CommentDto> commentDtos = commentService.getCommentDtoPageWithoutLogin(postDto.getPostId(),
			BASE_PAGE_NUMBER).getContent();
		final List<MemberDto> memberDtos = commentDtos.stream()
			.map(CommentDto::getMember)
			.toList();
		commentService.setMentionAndHashtagList(commentDtos);
		postDto.setRecentComments(commentDtos);
	}

	private Post getPost(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
	}

	private PostLike getPostLike(Member member, Post post) {
		return postLikeRepository.findByMemberAndPost(member, post)
			.orElseThrow(() -> new EntityNotFoundException(POST_LIKE_NOT_FOUND));
	}

	private Post getPostWithMember(Long postId) {
		return postRepository.findWithMemberById(postId)
			.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
	}

	private Bookmark getBookmark(Member member, Post post) {
		return bookmarkRepository.findByMemberAndPost(member, post)
			.orElseThrow(() -> new EntityNotFoundException(BOOKMARK_NOT_FOUND));
	}

}
