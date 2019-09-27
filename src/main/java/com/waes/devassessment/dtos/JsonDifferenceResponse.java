package com.waes.devassessment.dtos;

import lombok.Data;

/**
 * Created by Diego Amaya on 25/09/2019.
 */
@Data
public class JsonDifferenceResponse {
    private JsonComparisonResult result;
    private JsonComparisonDetail detail;
}
