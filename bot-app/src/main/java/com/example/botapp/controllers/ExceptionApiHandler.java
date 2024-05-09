package com.example.botapp.controllers;

import com.example.botapp.controllers.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(@NotNull Exception e) {
        List<String> errors = List.of(Arrays.toString(e.getStackTrace()));
        log.error(errors.toString());
        ApiErrorResponse response = new ApiErrorResponse(e.getClass().getSimpleName(), e.getMessage());
        log.error(response.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
