package org.rb.happylife.customers.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

//Create the Customer table
@DynamoDbBean
public  class BankCustomer {
    // @DynamoDBHashKey(attributeName = "id")
    private String id;
    private String name;
    private String email;
    private String regDate;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    };

    public void setId(String id) {

        this.id = id;
    }

    @DynamoDbSortKey
    public String getName() {
        return this.name;

    }

    public void setName(String name) {

        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String registrationDate) {

        this.regDate = registrationDate;
    }
}
