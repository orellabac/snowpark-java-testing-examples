package org.example.procedure;

import com.snowflake.snowpark_java.*;
import com.snowflake.snowpark_java.sproc.JavaSProc0;
import com.snowflake.snowpark_java.types.*;

import java.math.BigDecimal;

import org.example.util.SessionUtils;

/*
 * Sample procedure. It just inserts 2 rows into PUBLIC table
 */
public class Proc1 implements JavaSProc0<String> {

    static StructType schema = StructType.create(
        new StructField("ProductID", DataTypes.IntegerType),
        new StructField("ProductName", DataTypes.StringType),
        new StructField("Category", DataTypes.StringType),
        new StructField("Price", DataTypes.createDecimalType(10,2)));

    @Override
    public String call(Session session) {
        Row[] data = {
            Row.create(200, "Jacket","Clothing",BigDecimal.valueOf(3000.00)),
            Row.create(201, "Short","Clothing" ,BigDecimal.valueOf(5000.00))
        };
        
        // insert new data
        DataFrame df = session.createDataFrame(data,schema);
        df.write().mode("append").saveAsTable("PUBLIC.PRODUCT");
        return "Done";    
    }

    /**
     * Main entrypoint. Runs the stored procedure locally for development.
     * @param args
     */
    public static void main(String[] args) {
        Session session = SessionUtils.from_snowsql().getOrCreate();

        new Proc1().call(session);
    }


}
