package org.olaz.instasprite_be.domain.feed.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.CommentDto;
import org.olaz.instasprite_be.domain.feed.dto.CommentUploadRequest;
import org.olaz.instasprite_be.domain.feed.dto.CommentUploadResponse;
import org.olaz.instasprite_be.domain.feed.service.CommentService;
import org.olaz.instasprite_be.domain.member.dto.LikeMemberDto;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Comment API")
@RestController
@RequiredArgsConstructor
@RequestMapping(API_BASE_V1 + COMMENTS)
public class CommentController {

	private final CommentService commentService;

	@Operation(summary = "Upload comment", description = "For parentId, enter 0 for comments, or the parent comment PK for replies.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F010 - Comment uploaded successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G005 - Request message body is missing or value type is incorrect.\n"
			+ "F001 - Post does not exist.\n"
			+ "F012 - Cannot write comments on posts with comments disabled.\n"
			+ "F013 - Replies can only be uploaded to top-level comments."),
	})
	@PostMapping
	public ResponseEntity<ResultResponse> createComment(@Valid @RequestBody CommentUploadRequest request) {
		final CommentUploadResponse response = commentService.uploadComment(request);

		return ResponseEntity.ok(ResultResponse.of(CREATE_COMMENT_SUCCESS, response));
	}

	@Operation(summary = "Delete comment")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F011 - Comment deleted successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F008 - Comment does not exist.\n"
			+ "F009 - Cannot delete comments written by others."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@DeleteMapping(COMMENT_ID_PATH)
	public ResponseEntity<ResultResponse> deleteComment(
			@Parameter(description = "Comment PK", example = "1", required = true) @PathVariable Long commentId) {
		commentService.deleteComment(commentId);

		return ResponseEntity.ok(ResultResponse.of(DELETE_COMMENT_SUCCESS));
	}

	@Operation(summary = "Get paginated comment list",
		description = "Since post retrieval returns the 10 most recent comments, please <b>start from page 2</b>.<br>" +
			"If new comments are added during retrieval, duplicate data may occur on additional queries, " +
			"so please filter duplicate data when displaying in the view.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F012 - Comment list page retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(COMMENT_POSTS_ID_PATH)
	public ResponseEntity<ResultResponse> getCommentPage(
			@Parameter(description = "Post PK", example = "1", required = true) @PathVariable Long postId,
			@Parameter(description = "Comment page", example = "1", required = true) @RequestParam int page) {
		final Page<CommentDto> response = commentService.getCommentDtoPage(postId, page);

		return ResponseEntity.ok(ResultResponse.of(GET_COMMENT_PAGE_SUCCESS, response.getContent()));
	}

	@Operation(summary = "Get paginated reply list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F013 - Reply list page retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(COMMENT_ID_PATH)
	public ResponseEntity<ResultResponse> getReplyPage(
			@Parameter(description = "Parent comment PK", example = "1", required = true) @PathVariable Long commentId,
			@Parameter(description = "Reply page", example = "1", required = true) @RequestParam int page) {
		final Page<CommentDto> response = commentService.getReplyDtoPage(commentId, page);

		return ResponseEntity.ok(ResultResponse.of(GET_REPLY_PAGE_SUCCESS, response.getContent()));
	}

	@Operation(summary = "Like comment")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F015 - Comment liked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F008 - Comment does not exist.\n"
			+ "F010 - Member has already liked this comment."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@PostMapping(COMMENT_LIKE_PATH)
	public ResponseEntity<ResultResponse> likeComment(
			@Parameter(description = "Comment PK", example = "1", required = true) @RequestParam Long commentId) {
		commentService.likeComment(commentId);

		return ResponseEntity.ok(ResultResponse.of(LIKE_COMMENT_SUCCESS));
	}

	@Operation(summary = "Unlike comment")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F016 - Comment unliked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "F008 - Comment does not exist.\n"
			+ "F011 - Member has not liked this comment."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@DeleteMapping(COMMENT_LIKE_PATH)
	public ResponseEntity<ResultResponse> unlikeComment(
			@Parameter(description = "Comment PK", example = "1", required = true) @RequestParam Long commentId) {
		commentService.unlikeComment(commentId);

		return ResponseEntity.ok(ResultResponse.of(UNLIKE_COMMENT_SUCCESS));
	}

	@Operation(summary = "Get paginated list of members who liked comment")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F017 - Member list page who liked comment retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required."),
	})
	@GetMapping(COMMENT_ID_LIKES_PATH)
	public ResponseEntity<ResultResponse> getCommentLikes(
			@Parameter(description = "Comment PK", example = "1", required = true) @PathVariable Long commentId,
			@Parameter(description = "Page number", example = "1", required = true) @RequestParam int page,
			@Parameter(description = "Items per page", example = "10", required = true) @RequestParam int size) {
		final Page<LikeMemberDto> response = commentService.getCommentLikeMembersDtoPage(commentId, page, size);

		return ResponseEntity.ok(ResultResponse.of(GET_COMMENT_LIKES_SUCCESS, response));
	}

}
