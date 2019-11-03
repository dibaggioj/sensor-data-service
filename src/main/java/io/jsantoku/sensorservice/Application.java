package io.jsantoku.sensorservice;

import io.jsantoku.sensorservice.model.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        System.out.println("Running Sensor Service...");

        initialize();
    }

    public static void initialize() {
        CreateTableResponse createTableResponse;
        try {
            createTableResponse = DataPoint.initTable();
            if (createTableResponse == null) {
                LOG.info("Datapoint table already initialized");
            } else if (createTableResponse.sdkHttpResponse().isSuccessful()) {
                LOG.info("Successfully initialized datapoint table");
            } else {
                String statusText = createTableResponse.sdkHttpResponse().statusText().isPresent() ?
                        createTableResponse.sdkHttpResponse().statusText().get() : "Failed to get status text";
                LOG.error("Failed to initialized datapoint table, {}: {}",
                        createTableResponse.sdkHttpResponse().statusCode(),
                        statusText);
            }
        } catch (DynamoDbException e) {
            LOG.error("Failed to create table {}, {}\n", DataPoint.TABLE_NAME, e.getMessage());
        }

    }

}
