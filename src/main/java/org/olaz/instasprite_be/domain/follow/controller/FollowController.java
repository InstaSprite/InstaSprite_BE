package org.olaz.instasprite_be.domain.follow.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.follow.dto.FollowerDto;
import org.olaz.instasprite_be.domain.follow.service.FollowService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Follow API")
@RestController
@RequestMapping(API_BASE_V1)
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	@Operation(summary = "Follow a user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F001 - Followed member successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.\n"
			+ "F001 - Already following this user.\n"
			+ "F003 - Cannot follow yourself."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@PostMapping(FOLLOW_MEMBER_USERNAME)
	public ResponseEntity<ResultResponse> follow(
			@Parameter(description = "Username of the account to follow", required = true, example = "dlwlrma")
			@PathVariable("followMemberUsername") @Validated
			@NotBlank(message = "Username is required") String followMemberUsername) {
		final boolean success = followService.follow(followMemberUsername);
		if (success) {
			return ResponseEntity.ok(ResultResponse.of(FOLLOW_SUCCESS, success));
		} else {
			return ResponseEntity.ok(ResultResponse.of(FOLLOW_FAIL, success));
		}
	}

	@Operation(summary = "Unfollow a user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F002 - Unfollowed member successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.\n"
			+ "F002 - Cannot unfollow a user you're not following.\n"
			+ "F004 - Cannot unfollow yourself."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@DeleteMapping(FOLLOW_MEMBER_USERNAME)
	public ResponseEntity<ResultResponse> unfollow(
			@Parameter(description = "Username of the account to unfollow", required = true, example = "dlwlrma")
			@PathVariable("followMemberUsername") @Validated
			@NotBlank(message = "Username is required") String followMemberUsername) {
		final boolean success = followService.unfollow(followMemberUsername);

		return ResponseEntity.ok(ResultResponse.of(UNFOLLOW_SUCCESS, success));
	}

	@Operation(summary = "Remove a follower")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F005 - Follower removed successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.\n"
			+ "F005 - Cannot remove this follower."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@DeleteMapping(FOLLOWER_MEMBER_USERNAME)
	public ResponseEntity<ResultResponse> deleteFollower(
			@Parameter(description = "Username of the follower to remove", required = true, example = "dlwlrma")
			@PathVariable("followMemberUsername") @Validated
			@NotBlank(message = "Username is required") String followMemberUsername) {
		final boolean success = followService.deleteFollower(followMemberUsername);

		return ResponseEntity.ok(ResultResponse.of(DELETE_FOLLOWER_SUCCESS, success));
	}

	@Operation(summary = "Get following list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F003 - Retrieved following list."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(FOLLOWING_MEMBER_USERNAME)
	public ResponseEntity<ResultResponse> getFollowings(
			@Parameter(description = "Username of the account to get following list", required = true, example = "dlwlrma")
			@PathVariable("memberUsername") @Validated
			@NotBlank(message = "Username is required") String memberUsername) {
		final List<FollowerDto> followings = followService.getFollowings(memberUsername);

		return ResponseEntity.ok(ResultResponse.of(GET_FOLLOWINGS_SUCCESS, followings));
	}

	@Operation(summary = "Get follower list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "F004 - Retrieved follower list."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(FOLLOWERS_MEMBER_USERNAME)
	public ResponseEntity<ResultResponse> getFollowers(
			@Parameter(description = "Username of the account to get follower list", required = true, example = "dlwlrma")
			@PathVariable("memberUsername") @Validated
			@NotBlank(message = "Username is required") String memberUsername) {
		final List<FollowerDto> followings = followService.getFollowers(memberUsername);

		return ResponseEntity.ok(ResultResponse.of(GET_FOLLOWERS_SUCCESS, followings));
	}

}
