package io.jsantoku.sensorservice.controller;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import io.jsantoku.sensorservice.model.DataPoint;
import io.jsantoku.sensorservice.model.request.CreateRequest;
import io.jsantoku.sensorservice.model.response.CreateResponse;
import io.jsantoku.sensorservice.model.response.ErrorResponse;
import io.jsantoku.sensorservice.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

@RestController
public class DataController {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DataPointService dataPointService;

    @RequestMapping(path = "/data/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDataPoint(@PathVariable("id") String id) {
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @RequestMapping(path = "/data", method = RequestMethod.POST)
    public ResponseEntity<?> createDataPoint(@RequestBody CreateRequest createRequest) {
        DataPoint datapoint = createRequest.buildDataPoint();
        PutItemResult result = dataPointService.create(datapoint);
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
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @RequestMapping(path = "/data/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDataPoint(@PathVariable("id") String id) {
        // TODO: implement
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}
