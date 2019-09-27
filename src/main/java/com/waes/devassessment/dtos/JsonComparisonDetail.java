package com.waes.devassessment.dtos;

import lombok.Data;

import java.util.List;

/**
 * Created by Diego Amaya on 26/09/2019.
 */
@Data
public class JsonComparisonDetail {
    private List<JsonDifference> differences;
}
