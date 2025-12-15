package org.olaz.instasprite_be.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EditProfileRequest {

	@Schema(description = "Username", example = "dlwlrma", required = true)
	@NotBlank(message = "Please enter username")
	@Length(min = 4, max = 12, message = "ID must be between 4 and 12 characters")
	private String memberUsername;

	@Schema(description = "Name", example = "John Doe", required = true)
	@NotBlank(message = "Please enter name")
	@Length(min = 2, max = 12, message = "Name must be between 2 and 12 characters")
	private String memberName;

	@Schema(description = "Introduction", example = "Hello everyone", required = false)
	private String memberIntroduce;

	@Schema(description = "Email", example = "aaa@gmail.com", required = true)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String memberEmail;

}

