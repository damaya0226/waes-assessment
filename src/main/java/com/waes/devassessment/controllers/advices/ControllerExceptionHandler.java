package com.waes.devassessment.controllers.advices;

import com.waes.devassessment.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * Created by Diego Amaya on 26/09/2019.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        LOGGER.warn(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.warn(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
