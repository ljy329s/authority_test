package com.ljy.authority.authority_test.handler;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception e){
//        return new ResponseEntity<>(
//            new ErrorResponse(
//                ErrorCode.INTERNAL_ERROR.getCode(),
//                e.getMessage()
//            ),
//            ErrorCode.INTERNAL_ERROR.getHttpStatus()
//        );
//    }
//
//    private ResponseEntity<ErrorResponse> makeErrorResponse(
//        ErrorCode errorCode
//    ){
//        return new ResponseEntity<>(
//            new ErrorResponse(errorCode.getCode(), errorCode.getMessage()),
//            errorCode.getHttpStatus()
//        );
//    }
//    @Data
//    private static class ErrorResponse{
//        private final Integer code;
//        private final String message;
//    }
}