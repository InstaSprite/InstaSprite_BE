package org.olaz.instasprite_be.domain.hashtag.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import org.olaz.instasprite_be.domain.hashtag.dto.HashtagProfileResponse;
import org.olaz.instasprite_be.domain.hashtag.service.HashtagService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Hashtag API")
@Validated
@RestController
@RequestMapping(API_BASE_V1 + HASHTAGS)
@RequiredArgsConstructor
public class HashtagController {

	private final HashtagService hashtagService;

	@Operation(summary = "Follow hashtag")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "H002 - Hashtag followed successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "G007 - Unsupported image type.\n"
			+ "G008 - File cannot be converted.\n"
			+ "H001 - Hashtag does not exist.\n"
			+ "H002 - Failed to follow hashtag.\n"
			+ "H004 - Hashtag must start with #."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@PostMapping(HASHTAGS_FOLLOW_PATH)
	public ResponseEntity<ResultResponse> followHashtag(
		@Parameter(description = "Hashtag to follow", required = true, example = "#dumpling")
		@NotBlank(message = "Hashtag is required.") @RequestParam String hashtag) {
		hashtagService.followHashtag(hashtag);

		return ResponseEntity.ok(ResultResponse.of(FOLLOW_HASHTAG_SUCCESS));
	}

	@Operation(summary = "Unfollow hashtag")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "H003 - Hashtag unfollowed successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "G007 - Unsupported image type.\n"
			+ "G008 - File cannot be converted.\n"
			+ "H001 - Hashtag does not exist.\n"
			+ "H003 - Failed to unfollow hashtag.\n"
			+ "H004 - Hashtag must start with #."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@DeleteMapping(HASHTAGS_FOLLOW_PATH)
	public ResponseEntity<ResultResponse> unfollowHashtag(
		@Parameter(description = "Hashtag to unfollow", required = true, example = "#dumpling")
		@NotBlank(message = "Hashtag is required.") @RequestParam String hashtag) {
		hashtagService.unfollowHashtag(hashtag);

		return ResponseEntity.ok(ResultResponse.of(UNFOLLOW_HASHTAG_SUCCESS));
	}

	@Operation(summary = "Get hashtag profile")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "H004 - Hashtag profile retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "H001 - Hashtag does not exist.\n"),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(HASHTAGS_NAME_PATH)
	public ResponseEntity<ResultResponse> getHashtagProfile(
		@Parameter(description = "Hashtag name", required = true, example = "dumpling")
		@NotBlank(message = "Hashtag is required.") @PathVariable String hashtag) {
		final HashtagProfileResponse response = hashtagService.getHashtagProfileByHashtagName(hashtag);

		return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_PROFILE_SUCCESS, response));
	}

	@Operation(summary = "Get hashtag profile without login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "H004 - Hashtag profile retrieved successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "H001 - Hashtag does not exist.\n")
	})
	@GetMapping(HASHTAGS_NAME_WITHOUT_PATH)
	public ResponseEntity<ResultResponse> getHashtagProfileWithoutLogin(
		@Parameter(description = "Hashtag name", required = true, example = "dumpling")
		@NotBlank(message = "Hashtag is required.") @PathVariable String hashtag) {
		final HashtagProfileResponse response = hashtagService.getHashtagProfileByHashtagNameWithoutLogin(hashtag);

		return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_PROFILE_SUCCESS, response));
	}

}
