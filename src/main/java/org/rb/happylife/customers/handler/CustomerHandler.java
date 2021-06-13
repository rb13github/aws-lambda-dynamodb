package org.rb.happylife.customers.handler;

import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.rb.happylife.customers.model.BankCustomer;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import java.time.Instant;
import java.util.Iterator;

public class CustomerHandler {
    public static void main(String[] args) {

        Region region = Region.US_EAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();

        String result = getItem(enhancedClient);
        System.out.println(result);
        ddb.close();
    }

    // snippet-start:[dynamodb.java2.mapping.getitem.main]
    public static String getItem(DynamoDbEnhancedClient enhancedClient) {
        try {
            //Create a DynamoDbTable object
            DynamoDbTable<BankCustomer> mappedTable = enhancedClient.table("BankCustomer", TableSchema.fromBean(BankCustomer.class));
            QueryConditional keyValue = QueryConditional.keyEqualTo(Key.builder().partitionValue("101").build());
          //with sorted keys
            //QueryConditional.keyEqualTo(Key.builder().partitionValue(roomid).sortValue(createdDate).build());
            QueryEnhancedRequest enhancedRequest = QueryEnhancedRequest.builder()
                      //      .filterExpression(expression)
                    .queryConditional(keyValue)
                .limit(15) // you can increase this value
                .build();

    // logger.info("CustomersDB.getCustomers: enhancedRequest  "+enhancedRequest.toString());
    // Get items in the Customer table
    Iterator<BankCustomer> customersIter = mappedTable.query(enhancedRequest).items().iterator();

    while (customersIter.hasNext()) {
        BankCustomer customerObj = customersIter.next();
        String first = customerObj.getEmail();
        System.out.println(first);



    }

}catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        // snippet-end:[dynamodb.java2.mapping.getitem.main]
        return "";
    }

}
