package io.jsantoku.sensorservice.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import io.jsantoku.sensorservice.model.DataPoint;
import io.jsantoku.sensorservice.model.response.CreateResponse;
import io.jsantoku.sensorservice.model.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DataPointService {

    private static final Logger LOG = LoggerFactory.getLogger(DataPointService.class);

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private boolean tableExists() {
        ListTablesRequest request = new ListTablesRequest();
        ListTablesResult result = amazonDynamoDB.listTables(DataPoint.TABLE_NAME);
        return !result.getTableNames().isEmpty();
    }

    public CreateTableResult initTable() throws AmazonDynamoDBException {
        if (tableExists()) {
            LOG.info("Table {} already exists \n", DataPoint.TABLE_NAME);
            return null;
        }

        LOG.info("Creating table {}\n", DataPoint.TABLE_NAME);

        CreateTableRequest request = DataPoint.buildTableRequest();
        CreateTableResult result = amazonDynamoDB.createTable(request);

        LOG.info("Successfully created table {}\n", DataPoint.TABLE_NAME);

        return result;
    }

    public PutItemResult create(DataPoint datapoint) {
        HashMap<String, AttributeValue> item_values = new HashMap<>();

        ArrayList<String[]> extra_fields = new ArrayList<>();

        item_values.put(DataPoint.FIELD_DEVICE, new AttributeValue().withS(datapoint.getDevice()));
        item_values.put(DataPoint.FIELD_TIMESTAMP, new AttributeValue().withS(Long.toString(datapoint.getTimestamp())));

        if (datapoint.getTemperature() != null) {
            datapoint.getTemperature().addFieldsToTableItem(item_values);
        }
        if (datapoint.getHumidity() != null) {
            datapoint.getHumidity().addFieldsToTableItem(item_values);
        }

        PutItemRequest request = new PutItemRequest()
                .withTableName(DataPoint.TABLE_NAME)
                .withItem(item_values);

        PutItemResult result = amazonDynamoDB.putItem(request);

        return result;
    }

}
