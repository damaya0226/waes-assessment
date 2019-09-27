package com.waes.devassessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Diego Amaya on 26/09/2019.
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class JsonDifference {
    private int index;
    private int length;
}
