package com.waes.devassessment.repositories;

import com.waes.devassessment.entities.JsonDifferenceRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link JsonDifferenceRecord} repository
 * Created by Diego Amaya on 25/09/2019.
 */
@Repository
public interface JsonDifferenceRecordRepository extends CrudRepository<JsonDifferenceRecord, String> {
}
