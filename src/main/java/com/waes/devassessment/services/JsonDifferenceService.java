package com.waes.devassessment.services;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.entities.Part;
import com.waes.devassessment.exceptions.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Diego Amaya on 25/09/2019.
 */
@Validated
public interface JsonDifferenceService {

    /**
     * Saves part of the operation, right or left with its content
     * @param id id of the operation
     * @param part part of the comparison operation left or right
     * @param encodedJson json encoded in base64
     */
    void savePart(@NotBlank String id, @NotNull Part part, @NotNull byte[] encodedJson);

    /**
     * Evaluates comparison of both left and rigth parts
     * @param id id of the operation
     * @return comparison result
     * @throws ResourceNotFoundException when there is no record with the given id
     */
    JsonDifferenceResponse evaluateDifference(@NotBlank String id) throws ResourceNotFoundException;
}
