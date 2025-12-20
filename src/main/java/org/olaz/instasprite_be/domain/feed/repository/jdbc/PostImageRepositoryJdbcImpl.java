package org.olaz.instasprite_be.domain.feed.repository.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.global.vo.Image;

@RequiredArgsConstructor
public class PostImageRepositoryJdbcImpl implements PostImageRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String dbSchema;

	@Override
	public void savePostImages(List<Image> images, Long postId, List<String> altTexts) {
		final String sql = String.format(
			"INSERT INTO %s.post_image (post_id, post_image_url, post_image_type, post_image_name, "
				+ "post_image_uuid, image_width, image_height, post_image_alt_text) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
			dbSchema);

		jdbcTemplate.batchUpdate(
			sql,
			new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, postId);
					ps.setString(2, images.get(i).getImageUrl());
					ps.setString(3, images.get(i).getImageType().toString());
					ps.setString(4, images.get(i).getImageName());
					ps.setString(5, images.get(i).getImageUUID());
					ps.setInt(6, images.get(i).getImageWidth());
					ps.setInt(7, images.get(i).getImageHeight());
					ps.setString(8, altTexts.get(i));
				}

				@Override
				public int getBatchSize() {
					return images.size();
				}
			});
	}

}
