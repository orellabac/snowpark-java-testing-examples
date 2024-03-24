package org.example.udft;

import static org.junit.Assert.assertEquals;

import org.example.Register;
import org.example.util.SessionUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Session;


public class WordCounterIntegrationTest {
    Register register;
    Session session;

    @Before
    public void initialize() {  
        this.session = SessionUtils.from_snowsql().getOrCreate();
        this.session.sql("CREATE OR REPLACE DATABASE mytestdb_clone CLONE unittesting").collect();
        this.session.sql("USE DATABASE mytestdb_clone").collect();
        this.session.sql("CREATE STAGE PUBLIC.MYSTAGE").collect();
        this.register = new Register(session);
        register.WORDCOUNTER();
    }

    @After
    public void cleanup() {
        if (this.session!=null) {
            this.session.sql("DROP DATABASE mytestdb_clone").collect();
        }
    }

    @Test
    public void shouldReturn10Rows()
    {
        DataFrame df  = session.sql("SELECT * FROM TABLE(WORDCOUNTER('One Two Three Four Five Six Seven Eight Nine Ten'))");
        Long actual   = df.count();
        Long expected = 10L;
        assertEquals(expected, actual);
    }

}
