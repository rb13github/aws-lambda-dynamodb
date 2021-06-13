package org.rb.happylife.customers.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.rb.happylife.customers.persistent.CustomersDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  This is the entry point for the Lambda function
 */

public class Handler {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public List handleRequest(Context context) {

        CustomersDB customersDB = new CustomersDB();

       List ans =  customersDB.getCustomers();

        if (null!=ans) {
            logger.info("Messages sent : " + ans);
        }
        return ans;
    }

    public static void main(String []arg)
    {
        CustomersDB customersDB = new CustomersDB();

     //  List ans =  customersDB.getCustomers();

       String ans =  customersDB.getCustomerByName("Customer","Id","101");
        if (null!=ans) {
            logger.info("Messages sent : " + ans);
        }

    }
}
