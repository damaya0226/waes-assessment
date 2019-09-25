package com.waes.devassessment.services.impl;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.services.JsonComparator;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link JsonComparator} which has the comparison logic
 * Created by Diego Amaya on 25/09/2019.
 */
@Service
public class JsonComparatorImpl implements JsonComparator {

    @Override
    public JsonDifferenceResponse compare(String leftJson, String rightJson) {
        return null;
    }
}
