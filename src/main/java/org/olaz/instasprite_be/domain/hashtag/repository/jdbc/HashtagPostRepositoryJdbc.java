package org.olaz.instasprite_be.domain.hashtag.repository.jdbc;

import java.util.List;

import org.olaz.instasprite_be.domain.hashtag.entity.HashtagPost;

public interface HashtagPostRepositoryJdbc {

	void saveAllBatch(List<HashtagPost> newHashtagPost);

}
