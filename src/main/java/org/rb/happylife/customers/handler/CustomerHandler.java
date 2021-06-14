package org.rb.happylife.customers.handler;

import com.amazonaws.services.lambda.runtime.Context;
import org.rb.happylife.customers.model.BankCustomer;
import org.rb.happylife.customers.persistent.BankCustomerDB;
import org.rb.happylife.customers.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CustomerHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerHandler.class);

   //The ISO 8601 standard defines textual formats for date-time values.
    static Instant begin = Instant.now() ;  // Capture the current moment in UTC.
    static Instant end = Instant.now() ;
    static Instant mainbegin ;  // Capture the current moment in UTC.
    static Instant mainend ;

    public static void main(String[] args) {
        mainbegin = Instant.now();
        logger.info("main:START  "+mainbegin);

        JSONUtils JSONUtilsObj = new JSONUtils();
        BankCustomerDB aBankCustomerDB= new BankCustomerDB();
        //end = Instant.now();
        //logger.info("CustomerHandler.enhancedClient:created  Duration " +Duration.between( begin , end ));

        List list = aBankCustomerDB.getCustomers();
        logger.info("list: Result  " +list);


        BankCustomer aBankCustomer = aBankCustomerDB.getItem("101");
        logger.info("getItem: Result  " +JSONUtilsObj.toJson(aBankCustomer));

       // ddb.close();
        mainend= Instant.now();
        logger.info("main:END Duration  " +Duration.between( mainbegin , mainend ));
    }


    public List handleRequest(Context context) {

        BankCustomerDB customersDB = new BankCustomerDB();

        List ans =  customersDB.getCustomers();

        if (null!=ans) {
            logger.info("handleRequest Messages sent : " + ans);
        }
        return ans;
    }



}
