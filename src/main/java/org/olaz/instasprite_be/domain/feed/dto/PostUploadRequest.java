package org.olaz.instasprite_be.domain.feed.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUploadRequest {

	@Schema(description = "Post content", example = "Hello everyone!", required = true)
	@Size(max = 2200, message = "Post content allows maximum 2,200 characters.")
	private String content;

	@Schema(description = "Post images", required = true)
	@Size(min = 1, max = 10, message = "Post images must be between 1 and 10.")
	private List<MultipartFile> postImages = new ArrayList<>();

	@Schema(description = "Post image alt texts", required = true, example = "image")
	@Size(min = 1, message = "Post image alt text is required.")
	private List<@NotBlank(message = "Post image alt text is required.") String> altTexts;

//	@Schema(description = "Post image user tags")
//	@Valid
//	private List<PostImageTagRequest> postImageTags = new ArrayList<>();

	@Schema(description = "Comment feature enabled", required = true, example = "true")
	@NotNull(message = "Comment feature flag is required.")
	private boolean commentFlag;

//	@Schema(description = "Like count visibility", required = true, example = "true")
//	@NotNull(message = "Like visibility flag is required.")
//	private boolean likeFlag;

}
