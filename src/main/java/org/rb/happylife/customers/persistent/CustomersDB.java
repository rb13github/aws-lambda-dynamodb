package org.rb.happylife.customers.persistent;

import org.rb.happylife.customers.model.Customer;
import org.rb.happylife.customers.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;



import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 Sends a text message to any employee that reached the one year anniversary mark
*/

public class CustomersDB {

    private static final Logger logger = LoggerFactory.getLogger(CustomersDB.class);

   static Region region = Region.US_EAST_1;

   static DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .build();

    // Create a DynamoDbEnhancedClient and use the DynamoDbClient object
   static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    // Create a DynamoDbTable object based on Employee
   static  DynamoDbTable<Customer> custTable = enhancedClient.table("Customer", TableSchema.fromBean(Customer.class));
static JSONUtils JSONUtilsObj = new JSONUtils();
 static ArrayList customersJSONList = new ArrayList<>();

    //created by RB
    public List<String> getCustomers() {
        logger.info("CustomersDB.getCustomers:START  ");
        System.out.println("CustomersDB.getCustomers:START \n");
        String employeeStr="";
        logger.info("CustomersDB.getCustomers:ddb  "+ddb);
        logger.info("CustomersDB.getCustomers:enhancedClient  "+enhancedClient);
        logger.info("CustomersDB.getCustomers: table  "+custTable.tableName());
        try
        {

        Iterator<Customer> results = custTable.scan().items().iterator();
        while (results.hasNext()) {

            Customer rec = results.next();
           // System.out.println(rec.toString().);
            //System.out.println("The record id is "+rec.getFirst());
            System.out.println("The customer is " +JSONUtilsObj.toJson(rec));
            customersJSONList.add(JSONUtilsObj.toJson(rec));
        }

    } catch (DynamoDbException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
        logger.info("CustomersDB.getCustomers:END  ");
        return customersJSONList;
    }

    public String getCustomerByName(String tableName,String key,String keyVal) {
        logger.info("CustomersDB.getCustomerByName:START  ");
        Customer custObj=new Customer();
        logger.info("ScanEmployees.getEmployees:key  " + key);

try
{
        //Create a KEY object
        Key keyObj = Key.builder()
                .partitionValue(keyVal)
                .build();

        // Get the item by using the key
         custObj = custTable.getItem(r->r.key(keyObj));
        return "The record id is "+custObj.getId();

    } catch (DynamoDbException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
        ddb.close();
return custObj.getFirst();

    }
public String getCustomerByPhone(String phone,String index)
{
      logger.info("CustomersDB.getCustomerByName:START  ");

        logger.info("ScanEmployees.getEmployees:phone  "+phone);
        String employeeStr="";
        logger.info("CustomersDB.getCustomers:ddb  "+ddb);
        logger.info("CustomersDB.getCustomers: table  "+custTable.tableName());



    try {
        AttributeValue attVal = AttributeValue.builder()
                .s(phone)
                .build();
        logger.info("CustomersDB.getCustomers: AttributeValue  "+attVal);
        // Get only items in the Employee table that match the date
        Map<String, AttributeValue> expressionValues  = new HashMap<>();
        expressionValues .put(":value", attVal);

        Map<String, String> myExMap = new HashMap<>();
        myExMap.put("#startDate", "startDate");

        Expression expression = Expression.builder()
                .expression("phone = :value")
               //.expressionNames(myExMap)
                .expressionValues(expressionValues)
                .build();


        logger.info("CustomersDB.getCustomers: expression  "+expression.expressionValues());

        // Create a QueryConditional object that is used in the query operation
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(index)
                        .build());
        logger.info("CustomersDB.getCustomers: queryConditional  "+queryConditional);
        // Get items in the Customer table and write out the ID value
        Iterator<Customer> results = custTable.query(r -> r.queryConditional(queryConditional).filterExpression(expression)).items().iterator();

        while (results.hasNext()) {

            Customer rec = results.next();
            System.out.println("The record id is "+rec.getId());
        }



//        QueryEnhancedRequest  enhancedRequest = QueryEnhancedRequest.builder()
//                .filterExpression(expression)
//                .limit(15) // you can increase this value
//                .build();
//
//        logger.info("CustomersDB.getCustomers: enhancedRequest  "+enhancedRequest.toString());
//        // Get items in the Customer table
//        Iterator<Customer> customersIter = custTable.query(enhancedRequest).items().iterator();
//
//        while (customersIter.hasNext()) {
//            Customer customerObj = customersIter.next();
//            String first = customerObj.getFirst();
//           // String phone = customersIter.getPhone();
//            employeeStr+=first+ " ";
//            // Send an anniversary message!
//            // sentTextMessage(first, phone);
//            //commented by RB
//            //send = true;
//            logger.info("ScanEmployees.getEmployees:name  "+first);
//        }
    } catch (DynamoDbException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
    ddb.close();
        logger.info("CustomersDB.getCustomerByName:END  ");
        return employeeStr;




}
    public String getDate() {

        String DATE_FORMAT = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);

        Date currentDate = new Date();
        System.out.println("date : " + dateFormat.format(currentDate));
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("localDateTime : " + dateFormat8.format(localDateTime));

        localDateTime = localDateTime.minusYears(1);
        String ann = dateFormat8.format(localDateTime);
        return ann;
    }
//https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-items.html
    public static void getDynamoDBItem(DynamoDbClient ddb,String tableName,String key,String keyVal ) {

        HashMap<String, AttributeValue> keyToGet = new HashMap<String, AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            } else {
                System.out.format("No item found with the key %s!\n", key);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        ddb.close();
    }
}