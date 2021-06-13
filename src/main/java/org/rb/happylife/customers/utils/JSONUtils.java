package org.rb.happylife.customers.utils;

import com.google.gson.Gson;
import org.rb.happylife.customers.model.Customer;

public class JSONUtils implements IJSONUtils {
    static Gson g = new Gson();

    public  String toJson(Object obj) {

        String str = g.toJson(obj);
        return str;
    }
    public Object fromJson(String jsonString, Object obj) {

        obj = g.fromJson(jsonString, obj.getClass());
        return obj;
}
}
