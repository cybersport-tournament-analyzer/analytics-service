package com.vkr.analytics_service.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
    }
}