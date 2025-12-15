package org.olaz.instasprite_be.domain.member.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.member.service.BlockService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Block API")
@RestController
@RequestMapping(API_BASE_V1)
@RequiredArgsConstructor
@Validated
public class BlockController {

	private final BlockService blockService;

	@Operation(summary = "Block a user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "B001 - Member blocked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@PostMapping(BLOCKS_USERNAME)
	public ResponseEntity<ResultResponse> block(
			@Parameter(description = "Username to block", required = true, example = "dlwlrma")
			@PathVariable("username") String blockMemberUsername) {
		final boolean success = blockService.block(blockMemberUsername);

		return ResponseEntity.ok(ResultResponse.of(BLOCK_SUCCESS, success));
	}

	@Operation(summary = "Unblock a user")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "B002 - Member unblocked successfully."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type.\n"
			+ "M001 - User does not exist."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@DeleteMapping(BLOCKS_USERNAME)
	public ResponseEntity<ResultResponse> unblock(
			@Parameter(description = "Username to unblock", required = true, example = "dlwlrma")
			@PathVariable("username") String blockMemberUsername) {
		final boolean success = blockService.unblock(blockMemberUsername);

		return ResponseEntity.ok(ResultResponse.of(UNBLOCK_SUCCESS, success));
	}

}

