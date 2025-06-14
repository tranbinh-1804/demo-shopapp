package com.tranbinh.demo_shopapp.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private int statusCode;

    @JsonProperty("errors")
    private List<?> errors;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .statusCode(status.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Success", HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return success(data, message, HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status, List<?> errors) {
        return ApiResponse.<T>builder()
                .message(message)
                .statusCode(status.value())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return error(message, status, null);
    }
}
