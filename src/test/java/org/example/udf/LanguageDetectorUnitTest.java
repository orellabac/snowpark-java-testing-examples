package org.example.udf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LanguageDetectorUnitTest {

    static String english_phrase = "Actions speak louder than words.";
    static String spanish_phrase = "Vive la vida al máximo";
    static String french_phrase  = "Joie de vivre";
    static String german_phrase  = "Alles hat ein Ende, nur die Wurst hat zwei";
    static String chinese_phrase = "千里之行，始于足下";
    @Test
    public void shouldProperlyDetectLanguage() {
        
        assertEquals("ENGLISH" ,new LanguageDetectorUDF().call(english_phrase));
        assertEquals("SPANISH" ,new LanguageDetectorUDF().call(spanish_phrase));
        assertEquals("FRENCH"  ,new LanguageDetectorUDF().call(french_phrase));
        assertEquals("GERMAN"  ,new LanguageDetectorUDF().call(german_phrase));
        assertEquals("CHINESE" ,new LanguageDetectorUDF().call(chinese_phrase));

    }

}
