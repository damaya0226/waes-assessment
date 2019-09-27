package com.waes.devassessment.services.impl;

import com.waes.devassessment.dtos.JsonComparisonDetail;
import com.waes.devassessment.dtos.JsonComparisonResult;
import com.waes.devassessment.dtos.JsonDifference;
import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.services.JsonComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.waes.devassessment.dtos.JsonComparisonResult.*;

/**
 * Implementation of {@link JsonComparator} which has the comparison logic
 * Created by Diego Amaya on 25/09/2019.
 */
@Service
public class JsonComparatorImpl implements JsonComparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonComparatorImpl.class);

    @Override
    public JsonDifferenceResponse compare(String leftJson, String rightJson) {
        LOGGER.debug("Performing comparison between \n{}\n{}", leftJson, rightJson);
        JsonDifferenceResponse result = new JsonDifferenceResponse();
        JsonComparisonResult comparisonResult;

        if(leftJson.equals(rightJson)){
            comparisonResult = EQUAL;
        }else if(leftJson.length() != rightJson.length()){
            comparisonResult = DIFFERENT_SIZE;
        }else{
            comparisonResult = NOT_EQUAL;
            result.setDetail(calculateDifferenceDetails(leftJson, rightJson));
        }

        result.setResult(comparisonResult);
        LOGGER.debug("Comparison finished and its result is {}", comparisonResult);

        return result;
    }

    /**
     * Calculates the difference details that the given json have
     * @param leftJson left json
     * @param rightJson right json
     * @return comparison detail
     */
    private JsonComparisonDetail calculateDifferenceDetails(String leftJson, String rightJson) {
        JsonComparisonDetail detail = new JsonComparisonDetail();
        List<JsonDifference> differences = new ArrayList<>();

        boolean difference = false;
        int length = 0;
        for(int i = 0; i < leftJson.length(); i++){
            //if chars are difference and t
            if(leftJson.charAt(i) != rightJson.charAt(i)){
                difference = true;
                length++;
            }else if(difference){
                differences.add(new JsonDifference(i-length, length));
                difference = false;
                length = 0;
            }
        }
        //Limit case
        if(difference)
            differences.add(new JsonDifference(leftJson.length() - length, length));

        detail.setDifferences(differences);
        return detail;
    }
}
