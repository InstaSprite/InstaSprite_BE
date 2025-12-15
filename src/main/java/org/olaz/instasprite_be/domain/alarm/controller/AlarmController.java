package org.olaz.instasprite_be.domain.alarm.controller;

import static org.olaz.instasprite_be.global.result.ResultCode.*;
import static org.olaz.instasprite_be.global.util.UrlConstant.*;

import org.olaz.instasprite_be.domain.alarm.dto.AlarmDto;
import org.olaz.instasprite_be.domain.alarm.service.AlarmService;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Alarm API")
@Validated
@RestController
@RequestMapping(API_BASE_V1)
@RequiredArgsConstructor
public class AlarmController {

	private final AlarmService alarmService;

	@Operation(summary = "Get paginated alarm list")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "A001 - Successfully retrieved alarms."),
		@ApiResponse(responseCode = "400", description = "G003 - Invalid input.\n"
			+ "G004 - Invalid input type."),
	})
	@GetMapping(value = ALARMS)
	public ResponseEntity<ResultResponse> getAlarms(
			@Parameter(description = "Page number", example = "1", required = true) @RequestParam int page,
			@Parameter(description = "Items per page", example = "10", required = true) @RequestParam int size) {
		final Page<AlarmDto> response = alarmService.getAlarms(page, size);

		return ResponseEntity.ok(ResultResponse.of(GET_ALARMS_SUCCESS, response));
	}

}
