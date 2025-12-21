package org.olaz.instasprite_be.domain.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.olaz.instasprite_be.domain.feed.dto.PostDto;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;

@Getter
@AllArgsConstructor
public class SearchResponse {

	private SearchType type;
	private List<MemberDto> members;
	private List<PostDto> posts;

	public static SearchResponse members(List<MemberDto> members) {
		return new SearchResponse(SearchType.MEMBER, members, List.of());
	}

	public static SearchResponse posts(List<PostDto> posts) {
		return new SearchResponse(SearchType.POST, List.of(), posts);
	}
}

