package org.olaz.instasprite_be.domain.feed.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.ConstantUtils.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;
import static org.springframework.http.MediaType.*;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.PostDto;
import org.olaz.instasprite_be.domain.feed.dto.PostUploadRequest;
import org.olaz.instasprite_be.domain.feed.dto.PostUploadResponse;
import org.olaz.instasprite_be.domain.feed.service.PostService;
import org.olaz.instasprite_be.domain.member.dto.LikeMemberDto;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Post API")
@RestController
@RequiredArgsConstructor
@RequestMapping(API_BASE_V1 + POSTS)
public class PostController {

	private static final int BASE_POST_SIZE = 10;

	private final PostService postService;

	@Operation(summary = "Upload post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F001 - Post uploaded successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G007 - Unsupported image type.\n"
			+ "G008 - File cannot be converted."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@PostMapping
	public ResponseEntity<ResultResponse> uploadPost(@Valid @ModelAttribute PostUploadRequest request) {
		final PostUploadResponse response = postService.upload(request);

		return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS, response));
	}

	@Operation(summary = "Get paginated post list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F003 - Post list page retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping
	public ResponseEntity<ResultResponse> getPostPage(
			@Parameter(description = "Post page", example = "1", required = true) @RequestParam int page) {
		page = (page == BASE_PAGE_NUMBER ? BASE_PAGE_NUMBER : page - PAGE_ADJUSTMENT_VALUE);
		final Page<PostDto> postPage = postService.getPostDtoPageForFollowedUsers(BASE_POST_SIZE, page);

		// Return only the content to avoid PageImpl serialization issues
		return ResponseEntity.ok(ResultResponse.of(GET_POST_PAGE_SUCCESS, postPage.getContent()));
	}

	@Operation(summary = "Get 10 most recent posts")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F005 - 10 most recent posts retrieved successfully."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(POST_RECENT_PATH)
	public ResponseEntity<ResultResponse> getRecent10Posts() {
		final List<PostDto> postList = postService.getPostDtoPageForAllUsers(BASE_POST_SIZE, BASE_PAGE_NUMBER).getContent();

		return ResponseEntity.ok(ResultResponse.of(GET_RECENT_POSTS_SUCCESS, postList));
	}

	@Operation(summary = "Get paginated recent posts from all users")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F005 - Recent posts page retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(POST_RECENT_PATH + "/page")
	public ResponseEntity<ResultResponse> getRecentPostsPage(
			@Parameter(description = "Post page", example = "1", required = true) @RequestParam int page) {
		page = (page == BASE_PAGE_NUMBER ? BASE_PAGE_NUMBER : page - PAGE_ADJUSTMENT_VALUE);
		final Page<PostDto> postPage = postService.getPostDtoPageForAllUsers(BASE_POST_SIZE, page);

		// Return only the content to avoid PageImpl serialization issues
		return ResponseEntity.ok(ResultResponse.of(GET_RECENT_POSTS_SUCCESS, postPage.getContent()));
	}

	@Operation(summary = "Delete post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F002 - Post deleted successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"
			+ "F002 - Only the post author can delete the post."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@DeleteMapping
	public ResponseEntity<ResultResponse> deletePost(
			@Parameter(description = "Post PK", example = "1", required = true) @RequestParam Long postId) {
		postService.delete(postId);

		return ResponseEntity.ok(ResultResponse.of(DELETE_POST_SUCCESS));
	}

	@Operation(summary = "Get post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F004 - Post retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(POST_ID_PATH)
	public ResponseEntity<ResultResponse> getPost(
			@Parameter(description = "Post PK", example = "1", required = true) @PathVariable Long postId) {
		final PostDto response = postService.getPostDto(postId);

		return ResponseEntity.ok(ResultResponse.of(GET_POST_SUCCESS, response));
	}

	@Operation(summary = "Get post without login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F004 - Post retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"),
	})
	@GetMapping(POST_ID_WITHOUT_PATH)
	public ResponseEntity<ResultResponse> getPostWithoutLogin(
			@Parameter(description = "Post PK", example = "1", required = true) @PathVariable Long postId) {
		final PostDto response = postService.getPostDtoWithoutLogin(postId);

		return ResponseEntity.ok(ResultResponse.of(GET_POST_SUCCESS, response));
	}

	@Operation(summary = "Like post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F006 - Post liked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"
			+ "F004 - Member has already liked this post."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@PostMapping(POST_LIKE_PATH)
	public ResponseEntity<ResultResponse> likePost(
			@Parameter(description = "Post PK", example = "1", required = true) @RequestParam Long postId) {
		postService.likePost(postId);

		return ResponseEntity.ok(ResultResponse.of(LIKE_POST_SUCCESS));
	}

	@Operation(summary = "Unlike post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F007 - Post unliked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"
			+ "F003 - Member has not liked this post."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@DeleteMapping(POST_LIKE_PATH)
	public ResponseEntity<ResultResponse> unlikePost(
			@Parameter(description = "Post PK", example = "1", required = true) @RequestParam Long postId) {
		postService.unlikePost(postId);

		return ResponseEntity.ok(ResultResponse.of(UNLIKE_POST_SUCCESS));
	}

	@Operation(summary = "Get paginated list of members who liked post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F014 - Member list page who liked post retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F014 - This post has hidden like list."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(POST_ID_LIKES_PATH)
	public ResponseEntity<ResultResponse> getMembersLikedPost(
		@Parameter(description = "Post PK", example = "1", required = true) @PathVariable Long postId,
		@Parameter(description = "Page number", example = "1", required = true) @RequestParam int page,
		@Parameter(description = "Items per page", example = "10", required = true) @RequestParam int size) {
		page = (page == BASE_PAGE_NUMBER ? BASE_PAGE_NUMBER : page - PAGE_ADJUSTMENT_VALUE);
		final Page<LikeMemberDto> response = postService.getPostLikeMembersDtoPage(postId, page, size);

		return ResponseEntity.ok(ResultResponse.of(GET_POST_LIKES_SUCCESS, response));
	}

	@Operation(summary = "Bookmark post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F008 - Post bookmarked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"
			+ "F006 - This post has already been saved."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@PostMapping(POST_SAVE_PATH)
	public ResponseEntity<ResultResponse> bookmarkPost(
			@Parameter(description = "Post PK", example = "1", required = true) @RequestParam Long postId) {
		postService.bookmark(postId);

		return ResponseEntity.ok(ResultResponse.of(BOOKMARK_POST_SUCCESS));
	}

	@Operation(summary = "Remove bookmark from post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F009 - Post bookmark removed successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F001 - Post does not exist.\n"
			+ "F007 - This post has not been saved yet."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@DeleteMapping(POST_SAVE_PATH)
	public ResponseEntity<ResultResponse> unBookmarkPost(
			@Parameter(description = "Post PK", example = "1", required = true) @RequestParam Long postId) {
		postService.unBookmark(postId);

		return ResponseEntity.ok(ResultResponse.of(UNBOOKMARK_POST_SUCCESS));
	}

	@Operation(summary = "Get paginated hashtag post list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F019 - Hashtag post list retrieved successfully"),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(POST_HASHTAGS_PATH)
	public ResponseEntity<ResultResponse> getHashtagPosts(
			@Parameter(description = "Page number", example = "1", required = true) @RequestParam int page,
			@Parameter(description = "Items per page", example = "10", required = true) @RequestParam int size,
			@Parameter(description = "Hashtag", example = "#dumpling", required = true) @NotBlank(message = "hashtag is required.") @RequestParam String hashtag) {
		page = (page == BASE_PAGE_NUMBER ? BASE_PAGE_NUMBER : page - PAGE_ADJUSTMENT_VALUE);
		final Page<PostDto> response = postService.getHashTagPosts(page, size, hashtag.substring(HASHTAG_PREFIX_LENGTH));

		return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_POSTS_SUCCESS, response));
	}

	@Operation(summary = "Get most liked post")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F020 - Most liked post retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
	})
	@GetMapping("/most-liked")
	public ResponseEntity<ResultResponse> getMostLikedPost() {
		final PostDto response = postService.getMostLikedPostDto();

		return ResponseEntity.ok(ResultResponse.of(MOST_LIKED_POSTS_SUCCESS, response));
	}

}
