package org.rb.happylife.customers.persistent;

import org.rb.happylife.customers.handler.CustomerHandler;
import org.rb.happylife.customers.model.BankCustomer;
import org.rb.happylife.customers.model.Customer;
import org.rb.happylife.customers.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BankCustomerDB {
    private static final Logger logger = LoggerFactory.getLogger(BankCustomerDB.class);


    //The ISO 8601 standard defines textual formats for date-time values.
    static Instant begin = Instant.now() ;  // Capture the current moment in UTC.
    static Instant end = Instant.now() ;
    static Instant mainbegin ;  // Capture the current moment in UTC.
    static Instant mainend ;

    static Instant methodbegin  ;  // Capture the current moment in UTC.
    static Instant methodend  ;

    //declared static at class level as it takes around 3-4 seconds to intialize
    static DynamoDbClient ddb = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    // Create a DynamoDbEnhancedClient and use the DynamoDbClient object
    static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    // Create a DynamoDbTable object based on Employee
    static DynamoDbTable<BankCustomer> custTable = enhancedClient.table("BankCustomer", TableSchema.fromBean(BankCustomer.class));
    static JSONUtils JSONUtilsObj = new JSONUtils();
    static ArrayList customersJSONList = new ArrayList<>();

    //created by RB
    public List<String> getCustomers() {
        methodbegin= Instant.now();
        logger.info("getCustomers:START  "+methodbegin);
        try
        {
           Iterator<BankCustomer> results = custTable.scan().items().iterator();
            while (results.hasNext()) {
                BankCustomer rec = results.next();
                System.out.println("The customer is " +JSONUtilsObj.toJson(rec));
                customersJSONList.add(JSONUtilsObj.toJson(rec));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            logger.error("getItem:END  " +e.getMessage());
            System.exit(1);
        }
        methodend=Instant.now();
        logger.info("getCustomers:END  " +Duration.between( methodbegin , methodend ));
        return customersJSONList;
    }


    // snippet-start:[dynamodb.java2.mapping.getitem.main]
    public  BankCustomer getItem(String partitionKey) {
            methodbegin= Instant.now();
            logger.info("getItem:START  "+methodbegin);
            BankCustomer customerObj=new BankCustomer();

        try {
            //Create a DynamoDbTable object
           // DynamoDbTable<BankCustomer> mappedTable = enhancedClient.table("BankCustomer", TableSchema.fromBean(BankCustomer.class));
            QueryConditional keyValue = QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).build());
            //with sorted keys
            //QueryConditional.keyEqualTo(Key.builder().partitionValue(roomid).sortValue(createdDate).build());
            QueryEnhancedRequest enhancedRequest = QueryEnhancedRequest.builder()
                    //      .filterExpression(expression)
                    .queryConditional(keyValue)
                    .limit(15) // you can increase this value
                    .build();

            // logger.info("CustomersDB.getCustomers: enhancedRequest  "+enhancedRequest.toString());
            // Get items in the Customer table
            begin=Instant.now();
            Iterator<BankCustomer> customersIter = custTable.query(enhancedRequest).items().iterator();
            end=Instant.now();
            logger.info("getItem:customersIter query Duration  " + Duration.between( begin , end ));


            while (customersIter.hasNext()) {
                customerObj = customersIter.next();
                String email = customerObj.getEmail();
                System.out.println(email);
                logger.info("getItem:email  "+email);

            }

        }catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            logger.error("getItem:Error  "+e.getMessage());
            System.exit(1);
        }

        methodend=Instant.now();
        logger.info("getItem:END  " +Duration.between( methodbegin , methodend ));

        // snippet-end:[dynamodb.java2.mapping.getitem.main]

        return customerObj;
    }
}
