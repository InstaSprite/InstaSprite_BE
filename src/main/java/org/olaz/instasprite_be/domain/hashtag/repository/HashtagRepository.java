package org.olaz.instasprite_be.domain.hashtag.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	List<Hashtag> findAllByNameIn(Set<String> names);

	List<Hashtag> findAllByIdIn(Collection<Long> ids);

	Optional<Hashtag> findByName(String name);

}
