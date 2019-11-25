package io.jsantoku.sensorservice;

import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import io.jsantoku.sensorservice.model.DataPoint;
import io.jsantoku.sensorservice.service.DataPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private DataPointService dataPointService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Running Sensor Service...");
    }

    @PostConstruct
    public void init() {
        CreateTableResult createTableResult;
        try {
            createTableResult = dataPointService.initTable();
            if (createTableResult == null) {
                LOG.info("Datapoint table already initialized");
            } else if (createTableResult.getSdkHttpMetadata().getHttpStatusCode() .sdkHttpResponse().isSuccessful()) {
                LOG.info("Successfully initialized datapoint table");
            } else {
                String statusText = createTableResult.sdkHttpResponse().statusText().isPresent() ?
                        createTableResult.sdkHttpResponse().statusText().get() : "Failed to get status text";
                LOG.error("Failed to initialized datapoint table, {}: {}",
                        createTableResult.sdkHttpResponse().statusCode(),
                        statusText);
            }
        } catch (AmazonDynamoDBException e) {
            LOG.error("Failed to create table {}, {}\n", DataPoint.TABLE_NAME, e.getMessage());
        }

    }

}
