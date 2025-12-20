//package org.olaz.instasprite_be.domain.search.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;
//import org.olaz.instasprite_be.domain.search.entity.SearchHashtag;
//
//public interface SearchHashtagRepository extends JpaRepository<SearchHashtag, Long> {
//
//	Optional<SearchHashtag> findByHashtagName(String name);
//
//	List<SearchHashtag> findAllByHashtagIn(List<Hashtag> hashtags);
//
//}
