//package org.olaz.instasprite_be.domain.search.repository.querydsl;
//
//import static org.olaz.instasprite_be.domain.search.entity.QRecentSearch.*;
//import static org.olaz.instasprite_be.domain.search.entity.QSearchHashtag.*;
//import static org.olaz.instasprite_be.domain.search.entity.QSearchMember.*;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.domain.Pageable;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.olaz.instasprite_be.domain.search.entity.RecentSearch;
//import org.olaz.instasprite_be.domain.search.entity.Search;
//
//@Slf4j
//@RequiredArgsConstructor
//public class RecentSearchRepositoryQuerydslImpl implements RecentSearchRepositoryQuerydsl {
//
//	private final JPAQueryFactory queryFactory;
//
//	@Override
//	public Optional<RecentSearch> findRecentSearchByUsername(Long loginId, String username) {
//		return Optional.ofNullable(queryFactory
//			.select(recentSearch)
//			.from(recentSearch)
//			.innerJoin(recentSearch.search, searchMember._super)
//			.where(recentSearch.member.id.eq(loginId).and(searchMember.member.username.eq(username)))
//			.fetchOne());
//	}
//
//	@Override
//	public Optional<RecentSearch> findRecentSearchByHashtagName(Long loginId, String name) {
//		return Optional.ofNullable(queryFactory
//			.select(recentSearch)
//			.from(recentSearch)
//			.innerJoin(recentSearch.search, searchHashtag._super)
//			.where(recentSearch.member.id.eq(loginId).and(searchHashtag.hashtag.name.eq(name)))
//			.fetchOne());
//	}
//
//	@Override
//	public List<Search> findAllByMemberId(Long loginId, Pageable pageable) {
//		return queryFactory
//			.select(recentSearch.search)
//			.from(recentSearch)
//			.where(recentSearch.member.id.eq(loginId))
//			.offset(pageable.getOffset())
//			.limit(pageable.getPageSize())
//			.orderBy(recentSearch.lastSearchedDate.desc())
//			.distinct()
//			.fetch();
//	}
//
//	@Override
//	public Long getRecentSearchCount(Long loginId) {
//		return queryFactory
//			.selectOne()
//			.from(recentSearch)
//			.where(recentSearch.member.id.eq(loginId))
//			.fetchCount();
//	}
//
//}
