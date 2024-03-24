package org.example;

import org.example.procedure.ExcelWriter;
import org.example.procedure.Proc1;
import org.example.udf.LanguageDetectorUDF;
import org.example.udft.WordCounter;
import org.example.util.SessionUtils;

import com.snowflake.snowpark_java.Session;
import com.snowflake.snowpark_java.types.DataType;
import com.snowflake.snowpark_java.types.DataTypes;

public class Register {

    private Session session;
    public Register(Session session) {
        this.session = session;
    }

    public void PROC1() {
        // Drops procedure if exists
        session.sql("DROP PROCEDURE IF EXISTS PROC1()").collect();
        session.sproc().registerPermanent("PROC1",new Proc1(), DataTypes.StringType,"@MYSTAGE",true);
    }
    
    public void EXCELWRITER() {
        session.sql("DROP PROCEDURE IF EXISTS EXCELWRITER(STRING, STRING)").collect();
        session.addDependency("libs/poi.jar");
        session.addDependency("libs/poi-ooxml-lite.jar");
        session.addDependency("libs/poi-ooxml.jar");
        session.addDependency("libs/xmlbeans.jar");
        session.addDependency("libs/commons-collections4.jar");
        session.addDependency("libs/commons-compress.jar");
        session.addDependency("libs/commons-io.jar");
        session.addDependency("libs/commons-math3.jar");
        session.addDependency("libs/curvesapi.jar");
        session.addDependency("libs/log4j-api.jar");
        session.sproc().registerPermanent("EXCELWRITER",new ExcelWriter(),new DataType[]{DataTypes.StringType,DataTypes.StringType}, DataTypes.StringType,"@MYSTAGE",true);
        session.removeDependency("libs/poi.jar");
        session.removeDependency("libs/poi-ooxml-lite.jar");
        session.removeDependency("libs/poi-ooxml.jar");
        session.removeDependency("libs/xmlbeans.jar");
        session.removeDependency("libs/commons-collections4.jar");
        session.removeDependency("libs/commons-compress.jar");
        session.removeDependency("libs/commons-io.jar");
        session.removeDependency("libs/commons-math3.jar");
        session.removeDependency("libs/curvesapi.jar");
        session.removeDependency("libs/log4j-api.jar");

    }

    public void LANGUAGEDETECTOR() {
        session.sql("DROP FUNCTION IF EXISTS LANGUAGEDETECTOR(STRING)").collect();
        session.addDependency("libs/lingua.jar");
        session.addDependency("libs/kotlin-stdlib.jar");
        session.addDependency("libs/kotlin-stdlib-common.jar");
        session.addDependency("libs/annotations.jar");
        session.addDependency("libs/moshi.jar");
        session.addDependency("libs/okio.jar");
        session.addDependency("libs/kotlin-stdlib-jdk8.jar");
        session.addDependency("libs/kotlin-stdlib-jdk7.jar");
        session.addDependency("libs/moshi-kotlin.jar");
        session.addDependency("libs/kotlin-reflect.jar");
        session.addDependency("libs/fastutil.jar");
        session.udf().registerPermanent("LANGUAGEDETECTOR",new LanguageDetectorUDF(), DataTypes.StringType, DataTypes.StringType,"@MYSTAGE");
        session.removeDependency("libs/lingua.jar");
        session.removeDependency("libs/kotlin-stdlib.jar");
        session.removeDependency("libs/kotlin-stdlib-common.jar");
        session.removeDependency("libs/annotations.jar");
        session.removeDependency("libs/moshi.jar");
        session.removeDependency("libs/okio.jar");
        session.removeDependency("libs/kotlin-stdlib-jdk8.jar");
        session.removeDependency("libs/kotlin-stdlib-jdk7.jar");
        session.removeDependency("libs/moshi-kotlin.jar");
        session.removeDependency("libs/kotlin-reflect.jar");
        session.removeDependency("libs/fastutil.jar");
    }

    public void WORDCOUNTER() {
        session.sql("DROP FUNCTION IF EXISTS WORDCOUNTER(STRING)").collect();
        session.udtf().registerPermanent("WORDCOUNTER",new WordCounter(), "@MYSTAGE");
    }
    public static void main(String[] args) {
        Session session = SessionUtils.from_snowsql().getOrCreate();
        Register register = new Register(session);
        register.PROC1(); 
        register.EXCELWRITER();
        register.LANGUAGEDETECTOR();
        register.WORDCOUNTER();       
    }    
}
