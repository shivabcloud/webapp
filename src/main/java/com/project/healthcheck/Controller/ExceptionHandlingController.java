package com.project.healthcheck.Controller;

import com.project.healthcheck.Pojo.User;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionHandlingController {
    private ResponseEntity<User> responseHandler(HttpStatus status){
        return ResponseEntity
                .status(status)
                .cacheControl(CacheControl.noCache())
                .body(null);
    }
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<User> handleInvalidInputException(InvalidInputException ie) {
        System.err.println(ie.getMessage());
        return responseHandler(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<User> handleMethodArgumentInvalidException(MethodArgumentNotValidException me) {
        System.err.println(me.getMessage());
        return responseHandler(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<User> handleHttpMessageNotReadable(HttpMessageNotReadableException he) {
        System.err.println(he.getMessage());
        return responseHandler(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<User> handleNoHandlerFoundException() {

        return responseHandler(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<User> handleMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException hme){
        System.out.println("handleMediaTypeNotSupportedExceptionHandler");
        System.err.println(hme.getMessage());
        return responseHandler(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<User> handleAccessDeniedExceptionHandler(AccessDeniedException ae){
        System.err.println(ae.getMessage());
        return responseHandler(HttpStatus.UNAUTHORIZED);
    }
}
