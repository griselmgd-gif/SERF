package com.financorp.serf.web;

import java.time.DateTimeException;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, DateTimeException.class})
    public ResponseEntity<Map<String, String>> invalido(RuntimeException e) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", String.valueOf(e.getMessage())));
    }
}
