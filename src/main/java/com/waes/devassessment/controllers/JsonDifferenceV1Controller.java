package com.waes.devassessment.controllers;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.entities.Part;
import com.waes.devassessment.exceptions.ResourceNotFoundException;
import com.waes.devassessment.services.JsonDifferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: documentation
 * Created by Diego Amaya on 25/09/2019.
 */
@RestController
@RequestMapping(value = "/v1/diff", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class JsonDifferenceV1Controller {

    @Autowired
    private JsonDifferenceService service;

    @PostMapping("{id}/{part}")
    @ResponseStatus(HttpStatus.CREATED)
    public void savePart(@PathVariable("id") String id, @PathVariable Part part, @RequestBody byte[] encodedJson){
        service.savePart(id, part, encodedJson);
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonDifferenceResponse> evaluateDifference(@PathVariable("id") String id) throws ResourceNotFoundException {
        return ResponseEntity.ok(service.evaluateDifference(id));
    }
}
