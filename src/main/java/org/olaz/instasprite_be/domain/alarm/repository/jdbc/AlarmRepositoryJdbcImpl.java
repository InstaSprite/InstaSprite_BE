package org.olaz.instasprite_be.domain.alarm.repository.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.domain.alarm.dto.AlarmType;
import org.olaz.instasprite_be.domain.feed.entity.Comment;
import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.member.entity.Member;

@RequiredArgsConstructor
@Slf4j
public class AlarmRepositoryJdbcImpl implements AlarmRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String dbSchema;

	@Override
	public void saveMentionPostAlarms(Member agent, List<Member> targets, Post post, LocalDateTime now) {
		final String sql = String.format(
			"INSERT INTO %s.alarms (alarm_created_date, alarm_type, agent_id, post_id, target_id) " +
				"VALUES(?, ?, ?, ?, ?)", dbSchema);

		try {
			jdbcTemplate.batchUpdate(
				sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						final Long targetId = targets.get(i).getId();
						ps.setTimestamp(1, Timestamp.valueOf(now));
						ps.setString(2, AlarmType.MENTION_POST.name());
						ps.setLong(3, agent.getId());
						ps.setLong(4, post.getId());
						ps.setLong(5, targetId);
					}

					@Override
					public int getBatchSize() {
						return targets.size();
					}
				});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveMentionCommentAlarms(Member agent, List<Member> targets, Post post, Comment comment,
		LocalDateTime now) {
		final String sql = String.format(
			"INSERT INTO %s.alarms (alarm_created_date, alarm_type, agent_id, comment_id, post_id, target_id) "
				+ "VALUES(?, ?, ?, ?, ?, ?)", dbSchema);

		try {
			jdbcTemplate.batchUpdate(
				sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						final Long targetId = targets.get(i).getId();
						ps.setTimestamp(1, Timestamp.valueOf(now));
						ps.setString(2, AlarmType.MENTION_COMMENT.name());
						ps.setLong(3, agent.getId());
						ps.setLong(4, comment.getId());
						ps.setLong(5, post.getId());
						ps.setLong(6, targetId);
					}

					@Override
					public int getBatchSize() {
						return targets.size();
					}
				});
		} catch (Exception e) {
			throw e;
		}
	}

}
