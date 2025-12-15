package org.olaz.instasprite_be.domain.alarm.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.olaz.instasprite_be.domain.alarm.dto.AlarmType;
import org.olaz.instasprite_be.domain.feed.entity.Comment;
import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.follow.entity.Follow;
import org.olaz.instasprite_be.domain.member.entity.Member;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "alarms")
public class Alarm {

	@Id
	@Column(name = "alarm_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "alarm_type")
	@Enumerated(EnumType.STRING)
	private AlarmType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	private Member agent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_id")
	private Member target;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follow_id")
	private Follow follow;

	@CreatedDate
	@Column(name = "alarm_created_date")
	private LocalDateTime createdDate;

	@Builder
	public Alarm(AlarmType type, Member agent, Member target, Post post, Comment comment, Follow follow) {
		this.type = type;
		this.agent = agent;
		this.target = target;
		this.post = post;
		this.comment = comment;
		this.follow = follow;
	}

}
