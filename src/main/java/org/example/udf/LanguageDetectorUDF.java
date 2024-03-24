package org.example.udf;


import com.github.pemistahl.lingua.api.*;
import com.snowflake.snowpark_java.udf.JavaUDF1;


public class LanguageDetectorUDF implements JavaUDF1<String,String> {
    static LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages().build();

    @Override
    public String call(String text) {
        return detector.detectLanguageOf(text).toString();
    }
}
