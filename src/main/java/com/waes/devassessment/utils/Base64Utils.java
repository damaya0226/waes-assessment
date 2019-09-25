package com.waes.devassessment.utils;

import java.util.Base64;

/**
 * Utility to work with base64
 * Created by Diego Amaya on 25/09/2019.
 */
public class Base64Utils {
    //private constructor as it is a utility class
    private Base64Utils(){}

    /**
     * Decode base64 into String
     * @param encoded encoded base64
     * @return decoded String
     * @throws IllegalArgumentException when decode fails
     */
    public static String decode(byte[] encoded){
        return new String(Base64.getDecoder().decode(encoded));
    }
}
