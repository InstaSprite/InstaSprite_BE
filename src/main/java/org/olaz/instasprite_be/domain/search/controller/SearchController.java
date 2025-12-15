package org.olaz.instasprite_be.domain.search.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.API_BASE_V1;

import java.util.List;

import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.hashtag.dto.HashtagDto;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.search.dto.SearchDto;
import org.olaz.instasprite_be.domain.search.service.SearchService;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.UrlConstant;

@Slf4j
@Validated
@Tag(name = "Search API")
@RestController
@RequestMapping(API_BASE_V1)
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

    @Operation(summary = "Search")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE001 - Search succeeded."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @Parameter(name = "text", description = "Search text", required = true, example = "dlwl")
    @GetMapping(UrlConstant.TOP_SEARCH)
	public ResponseEntity<ResultResponse> searchText(@RequestParam String text) {
		final List<SearchDto> searchDtos = searchService.searchByText(text);

		return ResponseEntity.ok(ResultResponse.of(SEARCH_SUCCESS, searchDtos));
	}

    @Operation(summary = "Recommended members (following)")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE009 - Successfully retrieved recommended members."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @GetMapping(UrlConstant.TOP_SEARCH_RECOMMEND)
	public ResponseEntity<ResultResponse> getRecommendMembers() {
		final List<MemberDto> searchDtos = searchService.getRecommendMembers();

		return ResponseEntity.ok(ResultResponse.of(GET_RECOMMENDED_MEMBERS_SUCCESS, searchDtos));
	}

    @Operation(summary = "Member autocomplete")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE007 - Successfully retrieved member autocomplete results."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @Parameter(name = "text", description = "Search text", required = true, example = "dlwl")
    @GetMapping(UrlConstant.TOP_SEARCH_AUTO_MEMBER)
	public ResponseEntity<ResultResponse> getMemberAutoComplete(@RequestParam String text) {
		final List<MemberDto> searchDtos = searchService.getMemberAutoComplete(text);

		return ResponseEntity.ok(ResultResponse.of(GET_MEMBER_AUTO_COMPLETE_SUCCESS, searchDtos));
	}

    @Operation(summary = "Hashtag autocomplete")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE008 - Successfully retrieved hashtag autocomplete results."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type.\nH004 - Hashtags must start with #."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @Parameter(name = "text", description = "Search text", required = true, example = "#dlwl")
    @GetMapping(UrlConstant.TOP_SEARCH_AUTO_HASHTAG)
	public ResponseEntity<ResultResponse> getHashtagAutoComplete(@RequestParam String text) {
		final List<HashtagDto> searchDtos = searchService.getHashtagAutoComplete(text);

		return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_AUTO_COMPLETE_SUCCESS, searchDtos));
	}

    @Operation(summary = "Increment search count and update recent search history")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE002 - Successfully incremented search count and updated recent searches."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type.\nG009 - Invalid entity type.\nH004 - Hashtags must start with #."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @Parameters({
        @Parameter(name = "entityName", description = "Identifier name to increment count for", required = true, example = "dlwlrma"),
        @Parameter(name = "entityType", description = "Entity type to increment count for", required = true, example = "MEMBER")
    })
    @PostMapping(value = UrlConstant.TOP_SEARCH_MARK)
	public ResponseEntity<ResultResponse> markSearchedEntity(@RequestParam String entityName,
		@RequestParam String entityType) {
		searchService.markSearchedEntity(entityName, entityType);

		return ResponseEntity.ok(ResultResponse.of(MARK_SEARCHED_ENTITY_SUCCESS));
	}

    @Operation(summary = "Recent search history (top 15)")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE003 - Successfully retrieved top 15 recent searches."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @GetMapping(value = UrlConstant.TOP_SEARCH_RECENT_TOP)
	public ResponseEntity<ResultResponse> getTop15RecentSearch() {
		final Page<SearchDto> searchDtos = searchService.getTop15RecentSearches();

		return ResponseEntity.ok(ResultResponse.of(GET_RECENT_SEARCH_SUCCESS, searchDtos));
	}

    @Operation(summary = "Recent search history with infinite scroll")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE004 - Successfully retrieved recent search pages (infinite scroll)."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @GetMapping(value = UrlConstant.TOP_SEARCH_RECENT)
	public ResponseEntity<ResultResponse> getRecentSearch(@Min(1) @RequestParam int page) {
		final Page<SearchDto> searchDtos = searchService.getRecentSearches(page);

		return ResponseEntity.ok(ResultResponse.of(GET_RECENT_SEARCH_SUCCESS, searchDtos));
	}

    @Operation(summary = "Delete a recent search record")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE005 - Successfully deleted recent search record."),
        @ApiResponse(responseCode = "400", description = "G003 - Invalid input.\nG004 - Invalid input type.\nG009 - Invalid entity type.\nH004 - Hashtags must start with #."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @Parameters({
        @Parameter(name = "entityName", description = "Identifier name to delete", required = true, example = "dlwlrma"),
        @Parameter(name = "entityType", description = "Entity type to delete", required = true, example = "MEMBER")
    })
    @DeleteMapping(value = UrlConstant.TOP_SEARCH_RECENT)
	public ResponseEntity<ResultResponse> deleteRecentSearch(@RequestParam String entityName,
		@RequestParam String entityType) {
		searchService.deleteRecentSearch(entityName, entityType);

		return ResponseEntity.ok(ResultResponse.of(DELETE_RECENT_SEARCH_SUCCESS));
	}

    @Operation(summary = "Delete all recent search records")
	@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SE006 - Successfully deleted all recent search records."),
        @ApiResponse(responseCode = "401", description = "M003 - Login required.")
	})
    @DeleteMapping(value = UrlConstant.TOP_SEARCH_RECENT_ALL)
	public ResponseEntity<ResultResponse> deleteAllRecentSearch() {
		searchService.deleteAllRecentSearch();

		return ResponseEntity.ok(ResultResponse.of(DELETE_ALL_RECENT_SEARCH_SUCCESS));
	}

}
