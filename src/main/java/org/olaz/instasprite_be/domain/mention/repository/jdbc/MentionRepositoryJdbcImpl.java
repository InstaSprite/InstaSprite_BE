package org.olaz.instasprite_be.domain.mention.repository.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.mention.entity.MentionType;

@RequiredArgsConstructor
@Slf4j
public class MentionRepositoryJdbcImpl implements MentionRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String dbSchema;

	@Override
	public void savePostMentionsBatch(Long memberId, List<Member> mentionedMembers, Long postId, LocalDateTime now) {
		final String sql = String.format(
			"INSERT INTO %s.mentions (mention_created_date, mention_type, agent_id, comment_id, post_id, target_id) "
				+ "VALUES(?, ?, ?, ?, ?, ?)", dbSchema);

		try {
			jdbcTemplate.batchUpdate(
				sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						final Long targetId = mentionedMembers.get(i).getId();
						ps.setTimestamp(1, Timestamp.valueOf(now));
						ps.setString(2, MentionType.POST.name());
						ps.setLong(3, memberId);
						ps.setNull(4, Types.BIGINT);
						ps.setLong(5, postId);
						ps.setLong(6, targetId);
					}

					@Override
					public int getBatchSize() {
						return mentionedMembers.size();
					}
				}
			);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveCommentMentionsBatch(Long memberId, List<Member> mentionedMembers, Long postId, Long commentId,
		LocalDateTime now) {
		final String sql = String.format(
			"INSERT INTO %s.mentions (mention_created_date, mention_type, agent_id, comment_id, post_id, target_id) "
				+ "VALUES(?, ?, ?, ?, ?, ?)", dbSchema);

		try {
			jdbcTemplate.batchUpdate(
				sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						final Long targetId = mentionedMembers.get(i).getId();
						ps.setTimestamp(1, Timestamp.valueOf(now));
						ps.setString(2, MentionType.COMMENT.name());
						ps.setLong(3, memberId);
						ps.setLong(4, commentId);
						ps.setNull(5, Types.BIGINT);
						ps.setLong(6, targetId);
					}

					@Override
					public int getBatchSize() {
						return mentionedMembers.size();
					}
				}
			);
		} catch (Exception e) {
			throw e;
		}
	}

}
