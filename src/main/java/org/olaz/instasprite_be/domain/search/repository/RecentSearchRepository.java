//package org.olaz.instasprite_be.domain.search.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.olaz.instasprite_be.domain.search.entity.RecentSearch;
//import org.olaz.instasprite_be.domain.search.repository.querydsl.RecentSearchRepositoryQuerydsl;
//
//public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long>, RecentSearchRepositoryQuerydsl {
//
//	Optional<RecentSearch> findByMemberIdAndSearchId(Long memberId, Long searchId);
//
//	void deleteAllByMemberId(Long memberId);
//
//}
