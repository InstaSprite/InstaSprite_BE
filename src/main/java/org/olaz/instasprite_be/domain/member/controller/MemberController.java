package org.olaz.instasprite_be.domain.member.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.member.dto.EditProfileRequest;
import org.olaz.instasprite_be.domain.member.dto.EditProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.MenuMemberProfile;
import org.olaz.instasprite_be.domain.member.dto.MiniProfileResponse;
import org.olaz.instasprite_be.domain.member.dto.UserProfileResponse;
import org.olaz.instasprite_be.domain.member.service.MemberService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Slf4j
@Validated
@Tag(name = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping(API_BASE_V1)
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "Get logged-in user's top menu profile")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M016 - Retrieved top menu profile."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(value = ACCOUNTS_PROFILE)
	public ResponseEntity<ResultResponse> getMenuMemberProfile() {
		final UserProfileResponse userProfileResponse = memberService.getCurrentUserProfile();

		return ResponseEntity.ok(ResultResponse.of(GET_MENU_MEMBER_SUCCESS, userProfileResponse));
	}

	@Operation(summary = "Get user profile")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M004 - Retrieved user profile."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(value = ACCOUNTS_USERNAME)
	public ResponseEntity<ResultResponse> getUserProfile(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final UserProfileResponse userProfileResponse = memberService.getUserProfile(username);

		return ResponseEntity.ok(ResultResponse.of(GET_USER_PROFILE_SUCCESS, userProfileResponse));
	}

	@Operation(summary = "Get user profile without login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M004 - Retrieved user profile."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist.")
	})
	@GetMapping(value = ACCOUNTS_USERNAME_WITHOUT)
	public ResponseEntity<ResultResponse> getUserProfileWithoutLogin(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final UserProfileResponse userProfileResponse = memberService.getUserProfileWithoutLogin(username);

		return ResponseEntity.ok(ResultResponse.of(GET_USER_PROFILE_SUCCESS, userProfileResponse));
	}

	@Operation(summary = "Get mini profile")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M005 - Retrieved mini profile."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(value = ACCOUNTS_USERNAME_MINI)
	public ResponseEntity<ResultResponse> getMiniProfile(
			@Parameter(description = "Username", required = true, example = "dlwlrma")
			@PathVariable("username") String username) {
		final MiniProfileResponse miniProfileResponse = memberService.getMiniProfile(username);

		return ResponseEntity.ok(ResultResponse.of(GET_MINIPROFILE_SUCCESS, miniProfileResponse));
	}

	@Operation(summary = "Upload member profile image")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M006 - Member image uploaded."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "G007 - Unsupported image type.\n"
			+ "G008 - File cannot be converted."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@PostMapping(value = ACCOUNTS_IMAGE)
	public ResponseEntity<ResultResponse> uploadImage(
			@Parameter(description = "Image file to upload")
			@RequestParam MultipartFile uploadedImage) {
		memberService.uploadMemberImage(uploadedImage);

		return ResponseEntity.ok(ResultResponse.of(UPLOAD_MEMBER_IMAGE_SUCCESS));
	}

	@Operation(summary = "Delete member profile image")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M007 - Member image deleted."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@DeleteMapping(value = ACCOUNTS_IMAGE)
	public ResponseEntity<ResultResponse> deleteImage() {
		memberService.deleteMemberImage();

		return ResponseEntity.ok(ResultResponse.of(DELETE_MEMBER_IMAGE_SUCCESS));
	}

	@Operation(summary = "Get profile edit information")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M008 - Retrieved profile edit information."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(value = ACCOUNTS_EDIT)
	public ResponseEntity<ResultResponse> getMemberEdit() {
		final EditProfileResponse editProfileResponse = memberService.getEditProfile();

		return ResponseEntity.ok(ResultResponse.of(GET_EDIT_PROFILE_SUCCESS, editProfileResponse));
	}

	@Operation(summary = "Edit member profile")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "M009 - Profile updated successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@PutMapping(value = ACCOUNTS_EDIT)
	public ResponseEntity<ResultResponse> editProfile(@Valid @RequestBody EditProfileRequest editProfileRequest) {
		memberService.editProfile(editProfileRequest);

		return ResponseEntity.ok(ResultResponse.of(EDIT_PROFILE_SUCCESS));
	}

}

