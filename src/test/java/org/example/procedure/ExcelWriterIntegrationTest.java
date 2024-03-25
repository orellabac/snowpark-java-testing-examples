package org.example.procedure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.example.Register;
import org.example.util.SessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.snowflake.snowpark_java.Session;

public class ExcelWriterIntegrationTest {
    Register register;
    Session session;

    @Before
    public void initialize() {  
        this.session = SessionUtils.from_snowsql().getOrCreate();
        this.session.sql("CREATE OR REPLACE DATABASE mytestdb_clone CLONE unittesting").collect();
        this.session.sql("USE DATABASE mytestdb_clone").collect();
        this.session.sql("CREATE STAGE PUBLIC.MYSTAGE").collect();
        this.register = new Register(session);
        register.EXCELWRITER();
    }

    @After
    public void cleanup() {
        if (this.session!=null) {
            this.session.sql("DROP DATABASE mytestdb_clone").collect();
        }
    }



    @Test
    public void shouldWriteTheExcelFile()
    {
        session.sql("CALL EXCELWRITER('SELECT * FROM PUBLIC.PRODUCT','@MYSTAGE/output.xlsx')").show();
        var files = session.sql("ls @MYSTAGE  PATTERN = '.*output.xlsx'").first();
        if (!files.isPresent())
            fail("File was not found");
        String expected = "mystage/output.xlsx";
        String actual   = files.get().getString(0);
        assertEquals(expected,actual);
    }
}
