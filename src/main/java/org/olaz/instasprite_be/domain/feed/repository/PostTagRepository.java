package org.olaz.instasprite_be.domain.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.olaz.instasprite_be.domain.feed.dto.PostTagDto;
import org.olaz.instasprite_be.domain.feed.entity.PostImage;
import org.olaz.instasprite_be.domain.feed.entity.PostTag;
import org.olaz.instasprite_be.domain.feed.repository.jdbc.PostTagRepositoryJdbc;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagRepositoryJdbc {

	List<PostTag> findAllByPostImageIn(List<PostImage> postImages);

	@Query("select new org.olaz.instasprite_be.domain.feed.dto.PostTagDto("
		+ "pt.postImage.id, pt.id, pt.tag) "
		+ "from PostTag pt "
		+ "where pt.postImage.id in :postImageIds")
	List<PostTagDto> findAllPostTagDto(@Param(value = "postImageIds") List<Long> postImageIds);

}
