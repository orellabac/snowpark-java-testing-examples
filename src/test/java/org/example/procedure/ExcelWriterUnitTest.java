package org.example.procedure;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.Register;
import org.junit.Test;

import com.snowflake.snowpark_java.FileOperation;
import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Row;
import com.snowflake.snowpark_java.Session;
import com.snowflake.snowpark_java.types.DataTypes;
import com.snowflake.snowpark_java.types.StructField;
import com.snowflake.snowpark_java.types.StructType;

public class ExcelWriterUnitTest {
    Register register;
    Session session;

    static StructType schema = StructType.create(
    new StructField("ProductID", DataTypes.IntegerType),
    new StructField("ProductName", DataTypes.StringType),
    new StructField("Category", DataTypes.StringType),
    new StructField("Price", DataTypes.createDecimalType(10,2)));

    public Row[] mockData() {
        return new Row[]{
                Row.create(101, "Laptop","Electronics", 1200.00),
                Row.create(102, "Smartphone", "Electronics", 800.00),
                Row.create(103, "Headphones", "Electronics", 100.00),
                Row.create(104, "T-shirt", "Clothing", 20.00),
                Row.create(105, "Jeans", "Clothing", 50.00)
        };
    }



    @Test
    public void shouldWriteTheExcelFile()
    {
        session = mock(Session.class);
        DataFrame dataFrameMock = mock(DataFrame.class);
        when(session.sql(any())).thenReturn(dataFrameMock);
        when(dataFrameMock.collect()).thenReturn(mockData());
        when(dataFrameMock.schema()).thenReturn(schema);
        FileOperation fileOperationMock = mock(FileOperation.class);
        when(session.file()).thenReturn(fileOperationMock);

        String query = "SELECT * FROM PUBLIC.PRODUCT";
        String fileOutput = "@MYSTAGE/output.xlsx";
        String result = new ExcelWriter().call(session,query,fileOutput);
        // Assert that the method call was successful
        assertEquals("Done", result);
        verify(session).file();
        verify(fileOperationMock).uploadStream(eq(fileOutput), any(), eq(false)); 
    }
}
