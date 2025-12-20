//package org.olaz.instasprite_be.domain.feed.dto;
//
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class PostImageTagRequest {
//
//	@Schema(description = "Post image sequence number", example = "1", required = true)
//	@NotNull(message = "Post image sequence number is required.")
//	private Long id;
//
//	@Schema(description = "Post image tag x coordinate", example = "50", required = true)
//	@NotNull(message = "Post image tag x coordinate is required.")
//	@Min(value = 0, message = "X coordinate must be between 0 and 100.")
//	@Max(value = 100, message = "X coordinate must be between 0 and 100.")
//	private Double tagX;
//
//	@Schema(description = "Post image tag y coordinate", example = "50", required = true)
//	@NotNull(message = "Post image tag y coordinate is required.")
//	@Min(value = 0, message = "Y coordinate must be between 0 and 100.")
//	@Max(value = 100, message = "Y coordinate must be between 0 and 100.")
//	private Double tagY;
//
//	@Schema(description = "Tagged user username", example = "john_doe", required = true)
//	@NotBlank(message = "Tagged user username is required.")
//	private String username;
//
//}
