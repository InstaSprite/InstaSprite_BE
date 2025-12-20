//package org.olaz.instasprite_be.domain.hashtag.repository.jdbc;
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
//import org.olaz.instasprite_be.domain.hashtag.entity.HashtagPost;
//
//@RequiredArgsConstructor
//public class HashtagPostRepositoryJdbcImpl implements HashtagPostRepositoryJdbc {
//
//	private final JdbcTemplate jdbcTemplate;
//
//	@Value("${spring.jpa.properties.hibernate.default_schema}")
//	private String dbSchema;
//
//	@Override
//	public void saveAllBatch(List<HashtagPost> newHashtagPost) {
//		final String sql = String.format("INSERT INTO %s.hashtag_posts (`hashtag_id`, `post_id`) " +
//			"VALUES(?, ?)", dbSchema);
//
//		jdbcTemplate.batchUpdate(
//			sql,
//			new BatchPreparedStatementSetter() {
//				@Override
//				public void setValues(PreparedStatement ps, int i) throws SQLException {
//					ps.setString(1, newHashtagPost.get(i).getHashtag().getId().toString());
//					ps.setString(2, newHashtagPost.get(i).getPost().getId().toString());
//				}
//
//				@Override
//				public int getBatchSize() {
//					return newHashtagPost.size();
//				}
//			});
//	}
//
//}
