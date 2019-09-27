package com.waes.devassessment.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test of class {@link JsonUtils}
 * Created by Diego Amaya on 26/09/2019.
 */
@RunWith(SpringRunner.class)
public class JsonUtilsTest {

    private static final String VALID_JSON_1 = "\"Hello world\"";
    private static final String VALID_JSON_2 = "{\"key\":\"value\"}";
    private static final String VALID_JSON_3 = "{\"key\":\"value\",\"array\":[]}";
    private static final String INVALID_JSON = "Inv4l1d,{json}";

    @Test
    public void testValidateJsonSucceedWithString(){
        JsonUtils.validateJson(VALID_JSON_1);
    }

    @Test
    public void testValidateJsonSucceedWithObject(){
        JsonUtils.validateJson(VALID_JSON_2);
    }

    @Test
    public void testValidateJsonSucceedWithArray(){
        JsonUtils.validateJson(VALID_JSON_3);
    }

    @Test
    public void testValidateJsonFailedInvalidJson(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> JsonUtils.validateJson(INVALID_JSON));
    }
}
