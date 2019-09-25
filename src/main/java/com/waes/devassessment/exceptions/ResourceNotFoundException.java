package com.waes.devassessment.exceptions;

/**
 * Exception that represents when a resource is not found in the application
 * Created by Diego Amaya on 25/09/2019.
 */
public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
