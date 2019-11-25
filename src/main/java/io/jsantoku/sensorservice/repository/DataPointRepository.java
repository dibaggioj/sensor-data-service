package io.jsantoku.sensorservice.repository;

import io.jsantoku.sensorservice.model.DataPoint;
import org.springframework.data.repository.CrudRepository;

public interface DataPointRepository extends CrudRepository<DataPoint, String> {

}
