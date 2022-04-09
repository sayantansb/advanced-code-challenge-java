package com.statista.code.challenge.controller;

import com.statista.code.challenge.exception.ObjectNotFoundException;
import com.statista.code.challenge.exception.ObjectNotPersistedException;
import com.statista.code.challenge.exception.ObjectNotUpdatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ObjectNotPersistedException.class , ObjectNotUpdatedException.class})
    public ResponseEntity<Object> handleObjectNotPersistedException(
            ObjectNotPersistedException ex, WebRequest request) {
        log.error("In ExceptionHndler for exception : ",ex);
        List<String> errors = new ArrayList<String>();
        errors.add("Exception in Persisting Object Type ".concat(ex.getType()));
        ApiError apiError =  new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ObjectNotFoundException.class })
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException ex, WebRequest request) {
        log.error("In ExceptionHndler for exception : ",ex);
        List<String> errors = new ArrayList<String>();
        errors.add("No Object Found for type ".concat(ex.getType()));
        ApiError apiError =  new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
