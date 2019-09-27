package com.waes.devassessment.services.impl;

import com.waes.devassessment.dtos.JsonComparisonResult;
import com.waes.devassessment.dtos.JsonDifference;
import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.services.JsonComparator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

/**
 * Test of class {@link JsonComparatorImpl}
 * Created by Diego Amaya on 25/09/2019.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonComparatorImplTest {

    private static final String PLAIN_JSON = "{\"key\":\"value\"}";
    private static final String PLAIN_JSON_2 = "{\"key\":\"value\",\"array\":[]}";
    private static final String PLAIN_JSON_3 = "{\"tey\":\"hello\"}";
    private static final String PLAIN_JSON_NUMBER = "12345";
    private static final String PLAIN_JSON_NUMBER_2 = "12654";

    @Autowired
    private JsonComparator comparator;

    @Test
    public void testCompareFailedDueToNullLeftPart() {
        //Given
        String leftJson = null;
        String rightJson = PLAIN_JSON;

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> comparator.compare(leftJson, rightJson))
                .matches(e -> e.getConstraintViolations().size() == 1)
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                    String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                    return name.equals("leftJson") || name.equals("arg0");
                }));
    }

    @Test
    public void testCompareFailedDueToNullRightPart() {
        //Given
        String leftJson = PLAIN_JSON;
        String rightJson = null;

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> comparator.compare(leftJson, rightJson))
                .matches(e -> e.getConstraintViolations().size() == 1)
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                    String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                    return name.equals("rightJson") || name.equals("arg1");
                }));
    }

    @Test
    public void testCompareSucceedEqualJson(){
        //Given
        String leftJson = new String(PLAIN_JSON);
        String rightJson = new String(PLAIN_JSON);

        //When
        JsonDifferenceResponse result = comparator.compare(leftJson, rightJson);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResult()).isNotNull().isEqualTo(JsonComparisonResult.EQUAL);
        assertThat(result.getDetail()).isNull();
    }

    @Test
    public void testCompareSucceedDifferentSize(){
        //Given
        String leftJson = new String(PLAIN_JSON);
        String rightJson = new String(PLAIN_JSON_2);

        //When
        JsonDifferenceResponse result = comparator.compare(leftJson, rightJson);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResult()).isNotNull().isEqualTo(JsonComparisonResult.DIFFERENT_SIZE);
        assertThat(result.getDetail()).isNull();
    }

    @Test
    public void testCompareSucceedSameSizeDifferentContent(){
        //Given
        String leftJson = new String(PLAIN_JSON);
        String rightJson = new String(PLAIN_JSON_3);

        //When
        JsonDifferenceResponse result = comparator.compare(leftJson, rightJson);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResult()).isNotNull().isEqualTo(JsonComparisonResult.NOT_EQUAL);
        assertThat(result.getDetail()).isNotNull();

        List<JsonDifference> differences = result.getDetail().getDifferences();
        assertThat(differences).hasSize(3);
        //First Difference
        assertThat(differences.get(0).getIndex()).isEqualTo(2);
        assertThat(differences.get(0).getLength()).isEqualTo(1);

        //Second Difference
        assertThat(differences.get(1).getIndex()).isEqualTo(8);
        assertThat(differences.get(1).getLength()).isEqualTo(2);

        //Third Difference
        assertThat(differences.get(2).getIndex()).isEqualTo(11);
        assertThat(differences.get(2).getLength()).isEqualTo(2);

    }

    @Test
    public void testCompareSucceedSameSizeDifferentContent_2(){
        //Given
        String leftJson = new String(PLAIN_JSON_NUMBER);
        String rightJson = new String(PLAIN_JSON_NUMBER_2);

        //When
        JsonDifferenceResponse result = comparator.compare(leftJson, rightJson);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getResult()).isNotNull().isEqualTo(JsonComparisonResult.NOT_EQUAL);
        assertThat(result.getDetail()).isNotNull();

        List<JsonDifference> differences = result.getDetail().getDifferences();
        assertThat(differences).hasSize(1);
        //First Difference
        assertThat(differences.get(0).getIndex()).isEqualTo(2);
        assertThat(differences.get(0).getLength()).isEqualTo(3);

    }
}
