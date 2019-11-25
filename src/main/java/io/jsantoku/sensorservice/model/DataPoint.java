package io.jsantoku.sensorservice.model;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import io.jsantoku.sensorservice.model.data.Humidity;
import io.jsantoku.sensorservice.model.data.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = DataPoint.TABLE_NAME)
public class DataPoint implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(DataPoint.class);

    public static final String TABLE_NAME = "SensorData";
    public static final String DELIMITER = "-";
    public static final String FIELD_DEVICE = "device";    // e.g., ESP32
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String PARTITION_KEY = FIELD_DEVICE + DELIMITER + FIELD_TIMESTAMP;

    private String device;
    private long timestamp;
    private Temperature temperature;
    private Humidity humidity;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public DataPoint() { }

    public DataPoint(String device, long timestamp) {
        this.device = device;
        this.timestamp = timestamp;
    }

    public DataPoint(String id, String device, long timestamp, Temperature temperature) {
        this(device, timestamp);
        this.temperature = temperature;
    }

    public DataPoint(String id, String device, long timestamp, Humidity humidity) {
        this(device, timestamp);
        this.humidity = humidity;
    }

    public DataPoint(String id, String device, long timestamp, Temperature temperature, Humidity humidity) {
        this(device, timestamp);
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @DynamoDBHashKey(attributeName = FIELD_DEVICE)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @DynamoDBRangeKey(attributeName = FIELD_TIMESTAMP)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute
    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    @DynamoDBFlattened
    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public static CreateTableRequest buildTableRequest() {
        return new CreateTableRequest()
                .withAttributeDefinitions(buildAttributeDefinitions())
                .withKeySchema(new KeySchemaElement()      // Primary key
                        .withAttributeName(FIELD_DEVICE)
                        .withKeyType(KeyType.HASH),
                new KeySchemaElement()       // Sort Key
                        .withAttributeName(FIELD_TIMESTAMP)
                        .withKeyType(KeyType.RANGE))
                .withGlobalSecondaryIndexes(
                        Temperature.buildSecondaryIndex(),
                        Humidity.buildSecondaryIndex()
                )
                .withProvisionedThroughput(new ProvisionedThroughput()
                                .withReadCapacityUnits(5L)
                                .withWriteCapacityUnits(5L)
                )
                .withTableName(TABLE_NAME);
    }

    private static List<AttributeDefinition> buildAttributeDefinitions() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName(FIELD_DEVICE)
                .withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName(FIELD_TIMESTAMP)
                .withAttributeType(ScalarAttributeType.N));
        attributeDefinitions.addAll(Temperature.getAttributeDefinitions());
        attributeDefinitions.addAll(Humidity.getAttributeDefinitions());
        return attributeDefinitions;
    }

}
