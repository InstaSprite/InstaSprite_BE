package org.olaz.instasprite_be.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Result response data model")
@Getter
public class ResultResponse {

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Business status code", example = "P001")
    private String code;

    @Schema(description = "Response message", example = "Post uploaded successfully.")
    private String message;

    @Schema(description = "Response data")
    private Object data;

    public ResultResponse(ResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public static ResultResponse of(ResultCode resultCode) {
        return new ResultResponse(resultCode, "");
    }
}
