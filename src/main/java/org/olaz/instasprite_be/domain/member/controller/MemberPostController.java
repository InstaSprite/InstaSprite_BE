package org.olaz.instasprite_be.domain.member.controller;

import java.util.List;

import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.MemberPostDto;
import org.olaz.instasprite_be.domain.member.service.MemberPostService;
import org.olaz.instasprite_be.global.result.ResultCode;
import org.olaz.instasprite_be.global.result.ResultResponse;

import static org.olaz.instasprite_be.global.util.UrlConstant.*;


@Tag(name = "Member Posts API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(API_BASE_V1)
public class MemberPostController {

	private static final int FIRST_PAGE_SIZE_FOR_PROFILE = 15;
	private static final int FIRST_PAGE_SIZE_FOR_POST = 6;
	private static final int PAGE_SIZE_FOR_PROFILE = 3;
	private static final int PAGE_OFFSET_FOR_PROFILE = 4;

	private final MemberPostService memberPostService;

	@Operation(summary = "Get member's 15 most recent posts")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP001 - Retrieved member's 15 most recent posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(ACCOUNTS_USERNAME_POSTS_RECENT)
	public ResponseEntity<ResultResponse> getRecent15Posts(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final List<MemberPostDto> postList = memberPostService.getMemberPostDtoPage(username,
				FIRST_PAGE_SIZE_FOR_PROFILE, 0).getContent();

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_RECENT_POSTS_SUCCESS, postList));
	}

	@Operation(summary = "Get member's 6 most recent posts")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP007 - Retrieved member's 6 most recent posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(ACCOUNTS_USERNAME_POSTS_RECENT_POST)
	public ResponseEntity<ResultResponse> getRecent6Posts(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final List<MemberPostDto> postList = memberPostService.getMemberPostDtoPage(username, FIRST_PAGE_SIZE_FOR_POST,
				0).getContent();

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_RECENT_POSTS_SUCCESS, postList));
	}

	@Operation(summary = "Get member's posts with pagination (infinite scroll)")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP002 - Retrieved member's posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(ACCOUNTS_USERNAME_POSTS)
	public ResponseEntity<ResultResponse> getPostPage(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username,
			@Parameter(description = "Page number", required = true, example = "1")
			@Min(1) @RequestParam int page) {
		final Page<MemberPostDto> postPage = memberPostService.getMemberPostDtoPage(username, PAGE_SIZE_FOR_PROFILE,
			page + PAGE_OFFSET_FOR_PROFILE);

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBER_POSTS_SUCCESS, postPage));
	}

	@Operation(summary = "Get member's 15 most recent posts without login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP001 - Retrieved member's 15 most recent posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.")
	})
	@GetMapping(ACCOUNTS_USERNAME_POSTS_RECENT_WITHOUT)
	public ResponseEntity<ResultResponse> getRecent15PostsWithoutLogin(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final List<MemberPostDto> postList = memberPostService.getMemberPostDtoPageWithoutLogin(username,
			FIRST_PAGE_SIZE_FOR_PROFILE, 0).getContent();

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_RECENT_POSTS_SUCCESS, postList));
	}

	@Operation(summary = "Get member's posts with pagination without login (infinite scroll)")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP002 - Retrieved member's posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.")
	})
	@GetMapping(ACCOUNTS_USERNAME_POSTS_WITHOUT)
	public ResponseEntity<ResultResponse> getPostPageWithoutLogin(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username,
			@Parameter(description = "Page number", required = true, example = "1")
			@Min(1) @RequestParam int page) {
		final Page<MemberPostDto> postPage = memberPostService.getMemberPostDtoPageWithoutLogin(username,
			PAGE_SIZE_FOR_PROFILE, page + PAGE_OFFSET_FOR_PROFILE);

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBER_POSTS_SUCCESS, postPage));
	}

	// ============== Saved Posts ================
	@Operation(summary = "Get member's 15 most recent saved posts")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP003 - Retrieved member's 15 most recent saved posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(ACCOUNTS_POSTS_SAVED_RECENT)
	public ResponseEntity<ResultResponse> getRecent15SavedPosts() {
		final List<MemberPostDto> postList = memberPostService.getMemberSavedPostPage(FIRST_PAGE_SIZE_FOR_PROFILE, 0)
			.getContent();

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_RECENT_POSTS_SUCCESS, postList));
	}

	@Operation(summary = "Get member's saved posts with pagination (infinite scroll)")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "MP004 - Retrieved member's saved posts."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(ACCOUNTS_POSTS_SAVED)
	public ResponseEntity<ResultResponse> getSavedPostPage(
			@Parameter(description = "Page number", required = true, example = "1")
			@Min(1) @RequestParam int page) {
		final Page<MemberPostDto> postPage = memberPostService.getMemberSavedPostPage(PAGE_SIZE_FOR_PROFILE,
			page + PAGE_OFFSET_FOR_PROFILE);

		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_BOOKMARKED_POSTS_SUCCESS, postPage));
	}

//	// ============== Tagged Posts ================
//	@Operation(summary = "Get member's 15 most recent tagged posts")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "MP005 - Retrieved member's 15 most recent tagged posts."),
//		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
//			+ "G004 - Invalid input type.\n"
//			+ "M001 - User does not exist."),
//		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
//	})
//	@GetMapping(ACCOUNTS_USERNAME_POSTS_TAGGED_RECENT)
//	public ResponseEntity<ResultResponse> getRecent10TaggedPosts(
//			@Parameter(description = "Username", required = true, example = "dlwlrma")
//			@PathVariable("username") String username) {
//		final List<MemberPostDto> postList = memberPostService.getMemberTaggedPostDtoPage(username,
//			FIRST_PAGE_SIZE_FOR_PROFILE, 0).getContent();
//
//		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBER_TAGGED_POSTS_SUCCESS, postList));
//	}

//	@Operation(summary = "Get member's tagged posts with pagination (infinite scroll)")
//	@ApiResponses({
//		@ApiResponse(responseCode = "200", description = "MP006 - Retrieved member's tagged posts."),
//		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
//			+ "G004 - Invalid input type.\n"
//			+ "M001 - User does not exist."),
//		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
//	})
//	@GetMapping(ACCOUNTS_USERNAME_POSTS_TAGGED)
//	public ResponseEntity<ResultResponse> getTaggedPostPage(
//			@Parameter(description = "Username", required = true, example = "dlwlrma")
//			@PathVariable("username") String username,
//			@Parameter(description = "Page number", required = true, example = "1")
//			@Min(1) @RequestParam int page) {
//		final Page<MemberPostDto> postPage = memberPostService.getMemberTaggedPostDtoPage(username,
//			PAGE_SIZE_FOR_PROFILE, page + PAGE_OFFSET_FOR_PROFILE);
//
//		return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBER_TAGGED_POSTS_SUCCESS, postPage));
//	}

}
