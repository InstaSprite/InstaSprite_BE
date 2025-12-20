//package org.olaz.instasprite_be.domain.feed.repository.jdbc;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import lombok.RequiredArgsConstructor;
//
////import org.olaz.instasprite_be.domain.feed.dto.PostImageTagRequest;
//
//@RequiredArgsConstructor
//public class PostTagRepositoryJdbcImpl implements PostTagRepositoryJdbc {
//
//	private final JdbcTemplate jdbcTemplate;
//
//	@Value("${spring.jpa.properties.hibernate.default_schema}")
//	private String dbSchema;
//
//	@Override
//	public void savePostTags(List<PostImageTagRequest> postImageTags) {
//		final String sql = String.format(
//			"INSERT INTO %s.post_tags (post_tag_username, post_image_id) " +
//				"VALUES(?, ?)", dbSchema);
//
//		jdbcTemplate.batchUpdate(
//			sql,
//			new BatchPreparedStatementSetter() {
//				@Override
//				public void setValues(PreparedStatement ps, int i) throws SQLException {
//					ps.setString(1, postImageTags.get(i).getUsername());
//					ps.setLong(4, postImageTags.get(i).getId());
//				}
//
//				@Override
//				public int getBatchSize() {
//					return postImageTags.size();
//				}
//			}
//		);
//	}
//
//}
