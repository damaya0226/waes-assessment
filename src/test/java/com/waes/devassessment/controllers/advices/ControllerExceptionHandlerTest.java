package com.waes.devassessment.controllers.advices;

import com.waes.devassessment.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test of class {@link ControllerExceptionHandler}
 * Created by Diego Amaya on 26/09/2019.
 */
@RunWith(SpringRunner.class)
public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler controllerExceptionHandler;

    @Before
    public void setUp(){
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    public void testHandleConstraintViolationException(){
        ResponseEntity responseEntity = controllerExceptionHandler.handleConstraintViolationException(new ConstraintViolationException("Missing", null));
        assertThat(responseEntity).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        });
    }

    @Test
    public void testHandleIllegalArgumentException(){
        ResponseEntity responseEntity = controllerExceptionHandler.handleIllegalArgumentException(new IllegalArgumentException("Missing"));
        assertThat(responseEntity).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        });
    }

    @Test
    public void testHandleResourceNotFoundException(){
        ResponseEntity responseEntity = controllerExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("Not Found"));
        assertThat(responseEntity).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }
}
