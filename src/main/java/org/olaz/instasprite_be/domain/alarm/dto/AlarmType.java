package org.olaz.instasprite_be.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

	FOLLOW("{agent.username} started following you."),

	LIKE_POST("{agent.username} liked your photo."),
	MENTION_POST("{agent.username} mentioned you in a post: {post.content}"),

	COMMENT("{agent.username} commented: {comment.content}"),
	LIKE_COMMENT("{agent.username} liked your comment: {comment.content}"),
	MENTION_COMMENT("{agent.username} mentioned you in a comment: {comment.content}"),
	;

	private String message;

}
