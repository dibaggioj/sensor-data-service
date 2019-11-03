package io.jsantoku.sensorservice.model;

import io.jsantoku.sensorservice.model.data.Humidity;
import io.jsantoku.sensorservice.model.data.Temperature;
import io.jsantoku.sensorservice.util.DbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public static boolean tableExists() {
        DynamoDbClient ddb = DbClient.build();

        ListTablesRequest request = ListTablesRequest.builder().build();
        ListTablesResponse response = ddb.listTables(request);

        return response.tableNames().contains(TABLE_NAME);
    }

    public static CreateTableResponse initTable() throws DynamoDbException {
        if (tableExists()) {
            LOG.info("Table {} already exists \n", TABLE_NAME);
            return null;
        }

        LOG.info("Creating table {}\n", TABLE_NAME);

        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(
                        buildAttributeDefinitions()
                )
                .keySchema(
                        KeySchemaElement.builder()      // Primary key
                                .attributeName(FIELD_DEVICE)
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()      // Sort Key
                                .attributeName(FIELD_TIMESTAMP)
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .globalSecondaryIndexes(
                        Temperature.buildSecondaryIndex(),
                        Humidity.buildSecondaryIndex()
                )
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(5L)
                                .writeCapacityUnits(5L).build())
                .tableName(TABLE_NAME)
                .build();

        DynamoDbClient ddb = DbClient.build();

        CreateTableResponse result = ddb.createTable(request);
        LOG.info("Successfully created table {}\n", TABLE_NAME);

        return result;
    }

    private static List<AttributeDefinition> buildAttributeDefinitions() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(AttributeDefinition.builder()
                .attributeName(FIELD_DEVICE)
                .attributeType(ScalarAttributeType.S)
                .build());
        attributeDefinitions.add(AttributeDefinition.builder()
                .attributeName(FIELD_TIMESTAMP)
                .attributeType(ScalarAttributeType.N)
                .build());
        attributeDefinitions.addAll(Temperature.getAttributeDefinitions());
        attributeDefinitions.addAll(Humidity.getAttributeDefinitions());
        return attributeDefinitions;
    }

    public PutItemResponse addToTable() {
        HashMap<String, AttributeValue> item_values = new HashMap<>();

        ArrayList<String[]> extra_fields = new ArrayList<>();

        item_values.put(FIELD_DEVICE, AttributeValue.builder().s(this.device).build());
        item_values.put(FIELD_TIMESTAMP, AttributeValue.builder().n(Long.toString(this.timestamp)).build());

        if (this.temperature != null) {
            this.temperature.addFieldsToTableItem(item_values);
        }
        if (this.humidity != null) {
            this.humidity.addFieldsToTableItem(item_values);
        }

        DynamoDbClient ddb = DynamoDbClient.create();
        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item_values)
                .build();

        PutItemResponse result = ddb.putItem(request);

        return result;

    }
}
