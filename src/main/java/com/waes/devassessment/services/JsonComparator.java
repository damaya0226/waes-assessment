package com.waes.devassessment.services;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Contains json comparison signature
 * Created by Diego Amaya on 25/09/2019.
 */
@Validated
public interface JsonComparator {

    /**
     * Compares if the given jsons are equal or not
     * @param leftJson left json part
     * @param rightJson right json part
     * @return comparison result
     */
    JsonDifferenceResponse compare(@NotNull String leftJson, @NotNull String rightJson);
}
