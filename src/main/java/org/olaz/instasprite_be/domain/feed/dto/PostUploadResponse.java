package org.olaz.instasprite_be.domain.feed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Post upload response data model")
@Getter
@AllArgsConstructor
public class PostUploadResponse {

	@Schema(description = "Post PK", example = "1")
	private Long postId;

}
