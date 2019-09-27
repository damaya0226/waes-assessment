package com.waes.devassessment.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test of class {@link Base64Utils}
 * Created by Diego Amaya on 26/09/2019.
 */
@RunWith(SpringRunner.class)
public class Base64UtilsTest {

    private static final String BASE64_JSON = "eyJrZXkiOiJ2YWx1ZSJ9";
    private static final String PLAIN_JSON = "{\"key\":\"value\"}";
    private static final String INVALID_BASE64 = "TestInvalidB4se64*\\||";

    @Test
    public void testDecodeSucceed() throws UnsupportedEncodingException {
        String decoded = Base64Utils.decode(BASE64_JSON.getBytes("UTF-8"));
        assertThat(decoded).isNotNull().isEqualTo(PLAIN_JSON);
    }

    @Test
    public void testDecodeFailedDueToInvalidBase64() throws UnsupportedEncodingException {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Base64Utils.decode(INVALID_BASE64.getBytes("UTF-8")));
    }
}
