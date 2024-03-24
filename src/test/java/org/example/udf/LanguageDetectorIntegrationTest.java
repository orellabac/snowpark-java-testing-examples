package org.example.udf;

import static org.junit.Assert.assertEquals;

import org.example.Register;
import org.example.util.SessionUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import com.snowflake.snowpark_java.Session;


public class LanguageDetectorIntegrationTest {
    Register register;
    Session session;

    @Before
    public void initialize() {  
        this.session = SessionUtils.from_snowsql().getOrCreate();
        this.session.sql("CREATE OR REPLACE DATABASE mytestdb_clone CLONE unittesting").collect();
        this.session.sql("USE DATABASE mytestdb_clone").collect();
        this.session.sql("CREATE STAGE PUBLIC.MYSTAGE").collect();
        this.register = new Register(session);
        register.LANGUAGEDETECTOR();
    }

    @After
    public void cleanup() {
        if (this.session!=null) {
            this.session.sql("DROP DATABASE mytestdb_clone").collect();
        }
    }

    @Test
    public void shouldreturnENGLISH()
    {
        String actual = session.sql("SELECT LANGUAGEDETECTOR('Actions speak louder than words.')").first().get().getString(0);
        String expected = "ENGLISH";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldreturnSPANISH()
    {
        String actual = session.sql("SELECT LANGUAGEDETECTOR('Vive la vida al máximo')").first().get().getString(0);
        String expected = "SPANISH";
        assertEquals(expected, actual);
    }
}
