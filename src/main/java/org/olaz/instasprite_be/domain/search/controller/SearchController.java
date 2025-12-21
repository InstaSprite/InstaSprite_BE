package org.olaz.instasprite_be.domain.search.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.SEARCH_SUCCESS;
import static org.olaz.instasprite_be.global.util.ConstantUtils.BASE_PAGE_NUMBER;
import static org.olaz.instasprite_be.global.util.ConstantUtils.PAGE_ADJUSTMENT_VALUE;
import static org.olaz.instasprite_be.global.util.UrlConstant.API_BASE_V1;
import static org.olaz.instasprite_be.global.util.UrlConstant.SEARCH;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.search.dto.SearchResponse;
import org.olaz.instasprite_be.domain.search.service.SearchService;
import org.olaz.instasprite_be.global.result.ResultResponse;

@Tag(name = "Search API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(API_BASE_V1)
public class SearchController {

	private final SearchService searchService;

	@Operation(summary = "Unified search")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SE001 - Search was successful."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type."),
		@ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
	@GetMapping(SEARCH)
	public ResponseEntity<ResultResponse> search(
		@Parameter(description = "Search query. Prefix with @ to search members.", example = "@john")
		@RequestParam("q") @NotBlank String query,
		@Parameter(description = "Page number (1-based)", example = "1")
		@RequestParam(defaultValue = "1") int page,
		@Parameter(description = "Page size", example = "10")
		@RequestParam(defaultValue = "10") int size
	) {
		final int zeroBasedPage = (page == BASE_PAGE_NUMBER ? BASE_PAGE_NUMBER : page - PAGE_ADJUSTMENT_VALUE);
		final SearchResponse response = searchService.search(query, zeroBasedPage, size);
		return ResponseEntity.ok(ResultResponse.of(SEARCH_SUCCESS, response));
	}
}

