package com.waes.devassessment.controllers;

import com.waes.devassessment.DevAssessmentApplication;
import com.waes.devassessment.dtos.JsonDifference;
import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.entities.JsonDifferenceRecord;
import com.waes.devassessment.repositories.JsonDifferenceRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.waes.devassessment.dtos.JsonComparisonResult.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for class {@link JsonDifferenceV1Controller}
 * Created by Diego Amaya on 26/09/2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevAssessmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDifferenceV1ControllerTest {

    private static final String BASE_URL = "/v1/diff/";

    //Test attributes
    private static final String PLAIN_JSON = "{\"key\":\"value\"}";

    private static final String BASE64_JSON = "eyJrZXkiOiJ2YWx1ZSJ9";
    private static final String BASE64_JSON_2 = "eyJrZXkiOiJ2YWx1ZSIsImFycmF5IjpbXX0=";
    private static final String BASE64_JSON_3 = "eyJ0ZXkiOiJoZWxsbyJ9";

    private static final String BASE64_NO_JSON = "NDU1NDYxMkBmZSQ=";

    @Autowired
    private TestRestTemplate restTemplate;
    //Real repository to make assertions on inserted records
    @Autowired
    private JsonDifferenceRecordRepository repository;

    @Test
    public void testSaveLeftPartFailedMissingBody_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/left";
        String base64 = null;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveLeftPartFailedInvalidBase64_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/left";
        String base64 = "Invalid[Base\\64]";

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveLeftPartFailedValidBase64NoJson_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/left";
        String base64 = BASE64_NO_JSON;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveLeftPartSucceed(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/left";
        String base64 = BASE64_JSON;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //Database assertions
        Optional<JsonDifferenceRecord> optional = repository.findById(id);
        assertThat(optional.isPresent()).isTrue();

        JsonDifferenceRecord differenceRecord = optional.get();
        assertThat(differenceRecord).isNotNull();
        assertThat(differenceRecord.getLeftJson()).isNotNull().isEqualTo(PLAIN_JSON);
    }

    @Test
    public void testSaveRightPartFailedMissingBody_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/right";
        String base64 = null;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveRightPartFailedInvalidBase64_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/right";
        String base64 = "Invalid[Base\\64]";

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveRightPartFailedValidBase64NoJson_400(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/right";
        String base64 = BASE64_NO_JSON;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveRightPartSucceed(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id + "/right";
        String base64 = BASE64_JSON;

        //When
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        //Database assertions
        Optional<JsonDifferenceRecord> optional = repository.findById(id);
        assertThat(optional.isPresent()).isTrue();

        JsonDifferenceRecord differenceRecord = optional.get();
        assertThat(differenceRecord).isNotNull();
        assertThat(differenceRecord.getRightJson()).isNotNull().isEqualTo(PLAIN_JSON);
    }

    @Test
    public void testEvaluateDifferenceFailedDueToIdNotFound(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testEvaluateDifferenceFailedDueToMissingRightPart(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;
        String base64 = BASE64_JSON;
        //Insert Left part
        restTemplate.exchange(url + "/left", HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testEvaluateDifferenceFailedDueToMissingLeftPart(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;
        String base64 = BASE64_JSON;
        //Insert Right part
        restTemplate.exchange(url + "/right", HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testEvaluateDifferenceSucceedEqualJson(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;
        String base64 = BASE64_JSON;
        //Insert Left part
        restTemplate.exchange(url + "/left", HttpMethod.POST, new HttpEntity<>(base64), Void.class);
        //Insert Right part
        restTemplate.exchange(url + "/right", HttpMethod.POST, new HttpEntity<>(base64), Void.class);

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        JsonDifferenceResponse comparisonResult = response.getBody();
        assertThat(comparisonResult.getResult()).isEqualTo(EQUAL);
        assertThat(comparisonResult.getDetail()).isNull();
    }

    @Test
    public void testEvaluateDifferenceSucceedDifferentSize(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;
        String base64_1 = BASE64_JSON;
        String base64_2 = BASE64_JSON_2;
        //Insert Left part
        restTemplate.exchange(url + "/left", HttpMethod.POST, new HttpEntity<>(base64_1), Void.class);
        //Insert Right part
        restTemplate.exchange(url + "/right", HttpMethod.POST, new HttpEntity<>(base64_2), Void.class);

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        JsonDifferenceResponse comparisonResult = response.getBody();
        assertThat(comparisonResult.getResult()).isEqualTo(DIFFERENT_SIZE);
        assertThat(comparisonResult.getDetail()).isNull();
    }

    @Test
    public void testEvaluateDifferenceSucceedSameSizeDifferentContent(){
        //Given
        String id = UUID.randomUUID().toString();
        String url = BASE_URL + id ;
        String base64_1 = BASE64_JSON;
        String base64_2 = BASE64_JSON_3;
        //Insert Left part
        restTemplate.exchange(url + "/left", HttpMethod.POST, new HttpEntity<>(base64_1), Void.class);
        //Insert Right part
        restTemplate.exchange(url + "/right", HttpMethod.POST, new HttpEntity<>(base64_2), Void.class);

        //When
        ResponseEntity<JsonDifferenceResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), JsonDifferenceResponse.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        JsonDifferenceResponse comparisonResult = response.getBody();
        assertThat(comparisonResult.getResult()).isEqualTo(NOT_EQUAL);
        assertThat(comparisonResult.getDetail()).isNotNull();

        List<JsonDifference> differences = comparisonResult.getDetail().getDifferences();
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
}