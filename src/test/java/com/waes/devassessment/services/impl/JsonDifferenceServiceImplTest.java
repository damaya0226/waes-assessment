package com.waes.devassessment.services.impl;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.entities.JsonDifferenceRecord;
import com.waes.devassessment.entities.Part;
import com.waes.devassessment.exceptions.ResourceNotFoundException;
import com.waes.devassessment.repositories.JsonDifferenceRecordRepository;
import com.waes.devassessment.services.JsonComparator;
import com.waes.devassessment.services.JsonDifferenceService;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test of class {@link JsonDifferenceServiceImpl}
 * Created by Diego Amaya on 26/09/2019.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonDifferenceServiceImplTest {

    private static final String BASE64_JSON = "eyJrZXkiOiJ2YWx1ZSJ9";
    private static final String PLAIN_JSON = "{\"key\":\"value\"}";

    @Autowired
    private JsonDifferenceService service;
    @MockBean
    private JsonComparator comparator;
    @MockBean
    private JsonDifferenceRecordRepository repository;

    //FAILED PATHS -> PARAMETERS VALIDATION
    @Test
    public void testSavePartFailedDueToNullId() throws UnsupportedEncodingException {
        //Given
        String id = null;
        Part part = Part.LEFT;
        byte[] encodedJson = BASE64_JSON.getBytes("UTF-8");

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.savePart(id, part, encodedJson))
            .matches(e -> e.getConstraintViolations().size() == 1)
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                return name.equals("id") || name.equals("arg0");
        }));

        verify(repository, times(0)).findById(anyString());
        verify(repository, times(0)).save(any(JsonDifferenceRecord.class));
    }

    @Test
    public void testSavePartFailedDueToBlankId() throws UnsupportedEncodingException {
        //Given
        String id = "   ";
        Part part = Part.LEFT;
        byte[] encodedJson = BASE64_JSON.getBytes("UTF-8");

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.savePart(id, part, encodedJson))
            .matches(e -> e.getConstraintViolations().size() == 1)
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> ((String)v.getInvalidValue()).trim().isEmpty()))
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                return name.equals("id") || name.equals("arg0");
        }));

        verify(repository, times(0)).findById(anyString());
        verify(repository, times(0)).save(any(JsonDifferenceRecord.class));
    }

    @Test
    public void testSavePartFailedDueToNullPart() throws UnsupportedEncodingException {
        //Given
        String id = UUID.randomUUID().toString();
        Part part = null;
        byte[] encodedJson = BASE64_JSON.getBytes("UTF-8");

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.savePart(id, part, encodedJson))
            .matches(e -> e.getConstraintViolations().size() == 1)
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                return name.equals("part") || name.equals("arg1");
        }));

        verify(repository, times(0)).findById(anyString());
        verify(repository, times(0)).save(any(JsonDifferenceRecord.class));
    }

    @Test
    public void testSavePartFailedDueToNullEncodedJson() throws UnsupportedEncodingException {
        //Given
        String id = UUID.randomUUID().toString();
        Part part = Part.LEFT;
        byte[] encodedJson = null;

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> service.savePart(id, part, encodedJson))
            .matches(e -> e.getConstraintViolations().size() == 1)
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
            .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                return name.equals("encodedJson") || name.equals("arg2");
        }));

        verify(repository, times(0)).findById(anyString());
        verify(repository, times(0)).save(any(JsonDifferenceRecord.class));
    }

    //HAPPY PATHS

    @Test
    public void testSavePartSucceedWithRecordNotFound() throws UnsupportedEncodingException {
        //Given
        String id = UUID.randomUUID().toString();
        Part part = Part.LEFT;
        byte[] encodedJson = BASE64_JSON.getBytes("UTF-8");
        when(repository.findById(id)).thenReturn(Optional.empty());

        //When
        service.savePart(id, part, encodedJson);

        //Then
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(JsonDifferenceRecord.class));

    }

    @Test
    public void testSavePartSucceedWithRecordFound() throws UnsupportedEncodingException {
        //Given
        String id = UUID.randomUUID().toString();
        Part part = Part.RIGHT;
        byte[] encodedJson = BASE64_JSON.getBytes("UTF-8");
        JsonDifferenceRecord storedRecord = new JsonDifferenceRecord(id);

        when(repository.findById(id)).thenReturn(Optional.of(storedRecord));

        //When
        service.savePart(id, part, encodedJson);

        //Then
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(storedRecord);
        assertThat(storedRecord.getRightJson()).isNotNull().isEqualTo(PLAIN_JSON);
    }

    @Test
    public void testEvaluateDifferenceFailedDueToNullId() {
        //Given
        String id = null;

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> service.evaluateDifference(id))
                .matches(e -> e.getConstraintViolations().size() == 1)
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> Objects.isNull(v.getInvalidValue())))
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                    String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                    return name.equals("id") || name.equals("arg0");
                }));

        verify(repository, times(0)).findById(anyString());
        verify(comparator, times(0)).compare(anyString(), anyString());
    }

    @Test
    public void testEvaluateDifferenceFailedDueToBlankId() {
        //Given
        String id = "   ";

        //When and Then
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> service.evaluateDifference(id))
                .matches(e -> e.getConstraintViolations().size() == 1)
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> ((String)v.getInvalidValue()).trim().isEmpty()))
                .matches(e -> e.getConstraintViolations().stream().allMatch(v -> {
                    String name = ((PathImpl) v.getPropertyPath()).getLeafNode().getName();
                    return name.equals("id") || name.equals("arg0");
                }));

        verify(repository, times(0)).findById(anyString());
        verify(comparator, times(0)).compare(anyString(), anyString());
    }

    @Test
    public void testEvaluateDifferenceFailedDueToIdNotFound() {
        //Given
        String id = UUID.randomUUID().toString();
        when(repository.findById(id)).thenReturn(Optional.empty());

        //When and Then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> service.evaluateDifference(id));

        verify(repository, times(1)).findById(id);
        verify(comparator, times(0)).compare(anyString(), anyString());
    }

    @Test
    public void testEvaluateDifferenceSucceed() throws ResourceNotFoundException {
        //Given
        String id = UUID.randomUUID().toString();
        JsonDifferenceRecord storedRecord = new JsonDifferenceRecord(id, PLAIN_JSON, PLAIN_JSON);
        JsonDifferenceResponse jsonDifferenceResponse = new JsonDifferenceResponse();
        when(repository.findById(id)).thenReturn(Optional.of(storedRecord));
        when(comparator.compare(storedRecord.getLeftJson(), storedRecord.getRightJson())).thenReturn(jsonDifferenceResponse);

        //When
        JsonDifferenceResponse response = service.evaluateDifference(id);

        //Then
        assertThat(response).isEqualTo(jsonDifferenceResponse);
        verify(repository, times(1)).findById(id);
        verify(comparator, times(1)).compare(storedRecord.getLeftJson(), storedRecord.getRightJson());
    }
}
