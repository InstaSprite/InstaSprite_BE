package org.olaz.instasprite_be.domain.feed.repository.jdbc;

import java.util.List;

import org.olaz.instasprite_be.global.vo.Image;

public interface PostImageRepositoryJdbc {

	void savePostImages(List<Image> images, Long postId, List<String> altTexts);

}
