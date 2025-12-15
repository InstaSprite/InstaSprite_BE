package org.olaz.instasprite_be.domain.feed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Comment upload request data model")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUploadRequest {

	@Schema(description = "Post PK", example = "1", required = true)
	@NotNull(message = "Post PK is required.")
	private Long postId;

	@Schema(description = "Parent comment PK", example = "0", required = true)
	@NotNull(message = "Parent comment PK is required.")
	private Long parentId;

	@Schema(description = "Comment content", example = "Great post!", required = true)
	@NotBlank(message = "Comment content is required.")
	@Length(max = 100, message = "Maximum 100 characters allowed.")
	private String content;

}
