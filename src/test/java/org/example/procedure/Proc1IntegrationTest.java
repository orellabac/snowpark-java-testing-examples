package org.example.procedure;

import static org.junit.Assert.assertEquals;

import org.example.Register;
import org.example.util.SessionUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.snowflake.snowpark_java.Session;


public class Proc1IntegrationTest {
    Register register;
    Session session;

    @Before
    public void initialize() {  
        this.session = SessionUtils.from_snowsql().getOrCreate();
        this.session.sql("CREATE OR REPLACE DATABASE mytestdb_clone CLONE unittesting").collect();
        this.session.sql("USE DATABASE mytestdb_clone").collect();
        this.session.sql("CREATE STAGE PUBLIC.MYSTAGE").collect();
        this.register = new Register(session);
        register.PROC1();
    }

    @After
    public void cleanup() {
        if (this.session!=null) {
            this.session.sql("DROP DATABASE mytestdb_clone").collect();
        }
    }



    @Test
    public void shouldreturn7()
    {
        session.sql("CALL PROC1()").show();
        Long expected = 7L;
        Long actual = session.table("PUBLIC.PRODUCT").count();
        assertEquals(expected, actual);
    }
}
