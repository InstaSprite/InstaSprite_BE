package org.olaz.instasprite_be.domain.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.olaz.instasprite_be.domain.feed.dto.PostImageDto;
import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.feed.entity.PostImage;
import org.olaz.instasprite_be.domain.feed.repository.jdbc.PostImageRepositoryJdbc;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageRepositoryJdbc {

	List<PostImage> findAllByPost(Post post);

	@Query("select new org.olaz.instasprite_be.domain.feed.dto.PostImageDto("
		+ "pi.post.id, pi.id, pi.image.imageUrl, pi.altText) "
		+ "from PostImage pi "
		+ "where pi.post.id in :postIds")
	List<PostImageDto> findAllPostImageDtoByPostIdIn(@Param(value = "postIds") List<Long> postIds);

	List<PostImage> findAllByPostIdIn(List<Long> postIds);

}
