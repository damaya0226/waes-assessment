package com.waes.devassessment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Utility to group all json functions
 * Created by Diego Amaya on 25/09/2019.
 */
public class JsonUtils {
    //private constructor as it is a utility class
    private JsonUtils() {}

    public static void validateJson(String json){
        try{
            new ObjectMapper().readTree(json);
        }catch (IOException e){
            throw new IllegalArgumentException("Invalid Json", e);
        }
    }
}
