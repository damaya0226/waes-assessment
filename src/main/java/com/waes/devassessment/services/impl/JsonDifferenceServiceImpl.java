package com.waes.devassessment.services.impl;

import com.waes.devassessment.dtos.JsonDifferenceResponse;
import com.waes.devassessment.entities.JsonDifferenceRecord;
import com.waes.devassessment.entities.Part;
import com.waes.devassessment.exceptions.ResourceNotFoundException;
import com.waes.devassessment.repositories.JsonDifferenceRecordRepository;
import com.waes.devassessment.services.JsonComparator;
import com.waes.devassessment.services.JsonDifferenceService;
import com.waes.devassessment.utils.Base64Utils;
import com.waes.devassessment.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link JsonDifferenceService} which contains business logic
 * Created by Diego Amaya on 25/09/2019.
 */
@Service
public class JsonDifferenceServiceImpl implements JsonDifferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDifferenceServiceImpl.class);

    @Autowired
    private JsonDifferenceRecordRepository repository;
    @Autowired
    private JsonComparator jsonComparator;

    @Override
    public void savePart(final String id, final Part part, final byte[] encodedJson) {
        LOGGER.debug("Saving part JSON difference part ...");

        //Perform validations
        String json = Base64Utils.decode(encodedJson);
        JsonUtils.validateJson(json);

        // Find a record if exist otherwise creates a new one
        JsonDifferenceRecord record = repository.findById(id).orElse(new JsonDifferenceRecord(id));
        if(part.equals(Part.LEFT))
            record.setLeftJson(json);
        else
            record.setRightJson(json);

        repository.save(record);
    }

    @Override
    public JsonDifferenceResponse evaluateDifference(final String id) throws ResourceNotFoundException {
        LOGGER.debug("Evaluating differences for record with id: {}", id);
        JsonDifferenceRecord record = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Operation not found"));
        //Validations are done using javax.validation
        return jsonComparator.compare(record.getLeftJson(), record.getRightJson());
    }
}
