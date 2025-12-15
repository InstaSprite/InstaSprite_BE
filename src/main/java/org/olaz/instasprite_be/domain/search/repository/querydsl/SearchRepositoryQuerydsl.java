package org.olaz.instasprite_be.domain.search.repository.querydsl;

import java.util.List;
import java.util.Map;

import org.olaz.instasprite_be.domain.member.dto.MemberDto;
import org.olaz.instasprite_be.domain.search.dto.RecommendMemberDto;
import org.olaz.instasprite_be.domain.search.dto.SearchHashtagDto;
import org.olaz.instasprite_be.domain.search.dto.SearchMemberDto;
import org.olaz.instasprite_be.domain.search.entity.Search;

public interface SearchRepositoryQuerydsl {

	List<Search> findHashtagsByTextLike(String text);

	List<Search> findAllByTextLike(String text);

	List<Long> findMemberIdsByTextLike(String text);

	List<Long> findHashtagIdsByTextLike(String text);

	List<RecommendMemberDto> findRecommendMemberDtosOrderByPostCounts(Long loginId);

	void checkMatchingMember(String text, List<Search> searches, List<Long> searchIds);

	void checkMatchingHashtag(String text, List<Search> searches, List<Long> searchIds);

	void checkMatchingMember(String text, List<Long> memberIds);

	void checkMatchingHashtag(String text, List<Long> hashtagIds);

	Map<Long, SearchHashtagDto> findAllSearchHashtagDtoByIdIn(List<Long> searchIds);

	Map<Long, SearchMemberDto> findAllSearchMemberDtoByIdIn(Long loginId, List<Long> ids);

	Map<Long, MemberDto> findAllMemberDtoByIdIn(List<Long> memberIds);

}
