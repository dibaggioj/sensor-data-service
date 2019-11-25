package io.jsantoku.sensorservice.model.data;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@DynamoDBDocument
public class Temperature implements Serializable {

    private static final String DELIMITER = "-";
    private static final String FIELD_DEVICE = "device_temperature";    // e.g., DHT
    private static final String FIELD_VALUE = "value_temperature";
    private static final String FIELD_UNIT = "unit_temperature";
    private static final String FIELD_LOCATION = "location_temperature";
    private static final String PARTITION_KEY = FIELD_DEVICE + DELIMITER + FIELD_LOCATION;
    private static final String SECONDARY_INDEX_DESCRIPTION = "temperature-index";

    private String device;
    private float value;
    private char unit;  // F, C, K, or R
    private String location;

    public Temperature() { }

    public Temperature(float value, char unit, String location, String device) {
        this.device = device;
        this.value = value;
        this.unit = unit;
        this.location = location;
    }

    @DynamoDBIndexHashKey(attributeName = FIELD_DEVICE)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @DynamoDBIndexRangeKey(attributeName = FIELD_LOCATION)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDBAttribute(attributeName = FIELD_VALUE)
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @DynamoDBAttribute(attributeName = FIELD_UNIT)
    public char getUnit() {
        return unit;
    }

    public void setUnit(char unit) {
        this.unit = unit;
    }

    public void addFieldsToTableItem(HashMap<String, AttributeValue> itemValues) {
        itemValues.put(FIELD_DEVICE, new AttributeValue().withS(this.device));
        itemValues.put(FIELD_LOCATION, new AttributeValue().withS(this.location));
        itemValues.put(FIELD_VALUE, new AttributeValue().withN(Float.toString(this.value)));
        itemValues.put(FIELD_UNIT, new AttributeValue().withS(Character.toString(this.unit)));
    }

    public static List<AttributeDefinition> getAttributeDefinitions() {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName(FIELD_DEVICE)
                .withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName(FIELD_LOCATION)
                .withAttributeType(ScalarAttributeType.S));
// Don't need attribute definitions for non-key fields
//        attributeDefinitions.add(new AttributeDefinition()
//                .withAttributeName(FIELD_VALUE)
//                .withAttributeType(ScalarAttributeType.N));
//        attributeDefinitions.add(new AttributeDefinition()
//                .withAttributeName(FIELD_UNIT)
//                .withAttributeType(ScalarAttributeType.S));
        return attributeDefinitions;
    }

    public static GlobalSecondaryIndex buildSecondaryIndex() {
        return new GlobalSecondaryIndex()
                .withIndexName(SECONDARY_INDEX_DESCRIPTION)
                .withKeySchema(
                        new KeySchemaElement()
                                .withAttributeName(FIELD_DEVICE)
                                .withKeyType(KeyType.HASH),
                        new KeySchemaElement()
                                .withAttributeName(FIELD_LOCATION)
                                .withKeyType(KeyType.RANGE)
                )
                .withProjection(
                        new Projection()
                                .withNonKeyAttributes(
                                        FIELD_VALUE,
                                        FIELD_UNIT
                                )
                                .withProjectionType(
                                        ProjectionType.INCLUDE
                                )

                )
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(5L));
    }

}
