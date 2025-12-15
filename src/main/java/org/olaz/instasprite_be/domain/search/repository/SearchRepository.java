package org.olaz.instasprite_be.domain.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.olaz.instasprite_be.domain.search.entity.Search;
import org.olaz.instasprite_be.domain.search.repository.querydsl.SearchRepositoryQuerydsl;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryQuerydsl {

}
