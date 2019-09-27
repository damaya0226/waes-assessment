package com.waes.devassessment.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity that keeps the information about a json difference record
 * Created by Diego Amaya on 25/09/2019.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JsonDifferenceRecord {
    @Id
    private String id;
    private String leftJson;
    private String rightJson;

    public JsonDifferenceRecord(String id) {
        this.id = id;
    }
}
