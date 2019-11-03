package io.jsantoku.sensorservice.controller;

import io.jsantoku.sensorservice.model.DataPoint;
import io.jsantoku.sensorservice.model.request.CreateRequest;
import io.jsantoku.sensorservice.model.response.CreateResponse;
import io.jsantoku.sensorservice.model.response.ErrorResponse;
import io.jsantoku.sensorservice.util.DbClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Optional;

@RestController
public class DataController {

    @RequestMapping(path = "/data/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataPoint(@PathVariable("id") String id) {
        DynamoDbClient ddb = DbClient.build();
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @RequestMapping(path = "/data", method = RequestMethod.POST)
    public ResponseEntity<?> createDataPoint(@RequestBody CreateRequest createRequest) {
        DataPoint datapoint = createRequest.buildDataPoint();
        PutItemResponse result = datapoint.addToTable();
        if (result.sdkHttpResponse().isSuccessful()) {
            String key = datapoint.getDevice() + "-" + datapoint.getTimestamp();
            CreateResponse response = new CreateResponse(key);
            return ResponseEntity.ok(response);
        } else {
            String statusText = result.sdkHttpResponse().statusText().isPresent() ?
                    result.sdkHttpResponse().statusText().get() : "Failed to get status text";
            ErrorResponse response = new ErrorResponse(result.sdkHttpResponse().statusCode(), statusText);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    @RequestMapping(path = "/data/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateDataPoint(@PathVariable("id") String id) {
        DynamoDbClient ddb = DbClient.build();
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @RequestMapping(path = "/data/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDataPoint(@PathVariable("id") String id) {
        DynamoDbClient ddb = DbClient.build();
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}
