package org.olaz.instasprite_be.domain.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.dto.PostDto;
import org.olaz.instasprite_be.domain.feed.service.PostService;
import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.search.dto.SearchResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

	private final MemberRepository memberRepository;
	private final PostService postService;

	public SearchResponse search(String rawQuery, int page, int size) {
		final String query = rawQuery.trim();
		final Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

		if (query.startsWith("@")) {
			final String username = query.substring(1);
			final Page<MemberDto> members = memberRepository.searchMemberDtoByUsernameContains(username, pageable);
			return SearchResponse.members(members.getContent());
		}

		final Page<PostDto> posts = postService.searchPostsByContent(query, page, size);
		return SearchResponse.posts(posts.getContent());
	}
}

