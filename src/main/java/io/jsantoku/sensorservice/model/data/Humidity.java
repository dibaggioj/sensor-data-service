package io.jsantoku.sensorservice.model.data;

import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Humidity implements Serializable {

    private static final String DELIMITER = "-";
    private static final String FIELD_DEVICE = "device_humidity";    // e.g., DHT
    private static final String FIELD_VALUE = "value_humidity";
    private static final String FIELD_LOCATION = "location_humidity";
    private static final String PARTITION_KEY = FIELD_DEVICE + DELIMITER + FIELD_LOCATION;
    private static final String SECONDARY_INDEX_DESCRIPTION = "humidity-index";

    private String device;
    private float value;
    private String location;

    public Humidity() { }

    public Humidity(float value, String location) {
        this.value = value;
        this.location = location;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addFieldsToTableItem(HashMap<String, AttributeValue> itemValues) {
        itemValues.put(FIELD_DEVICE, AttributeValue.builder().s(this.device).build());
        itemValues.put(FIELD_LOCATION, AttributeValue.builder().s(this.location).build());
        itemValues.put(FIELD_VALUE, AttributeValue.builder().n(Float.toString(this.value)).build());
    }

    public static List<AttributeDefinition> getAttributeDefinitions() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(AttributeDefinition.builder()
                .attributeName(FIELD_DEVICE)
                .attributeType(ScalarAttributeType.S)
                .build());
        attributeDefinitions.add(AttributeDefinition.builder()
                .attributeName(FIELD_LOCATION)
                .attributeType(ScalarAttributeType.S)
                .build());
// Don't need attribute definitions for non-key fields
//        attributeDefinitions.add(AttributeDefinition.builder()
//                .attributeName(FIELD_VALUE)
//                .attributeType(ScalarAttributeType.N)
//                .build());
        return attributeDefinitions;
    }

    public static GlobalSecondaryIndex buildSecondaryIndex() {
        return GlobalSecondaryIndex.builder()
                .indexName(SECONDARY_INDEX_DESCRIPTION)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(FIELD_DEVICE)
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName(FIELD_LOCATION)
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .projection(
                        Projection.builder()
                                .nonKeyAttributes(
                                        FIELD_VALUE
                                )
                                .projectionType(
                                        ProjectionType.INCLUDE
                                )
                                .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L).build())

                .build();
    }
}
