package io.jsantoku.sensorservice.util;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

public abstract class DbClient {
    public static DynamoDbClient build() {
        DynamoDbClientBuilder ddbBuilder = DynamoDbClient.builder();
//        ddbBuilder.region(Region.US_WEST_2);
//        ddbBuilder.credentialsProvider()
        DynamoDbClient ddb = ddbBuilder.build();    // DynamoDbClient.create();
        return ddb;
    }
}
