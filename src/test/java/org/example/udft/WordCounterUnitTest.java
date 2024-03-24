package org.example.udft;

import org.junit.Before;
import org.junit.Test;

import com.snowflake.snowpark_java.Row;
import com.zavtech.morpheus.frame.DataFrame;

//import joinery.DataFrame;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.udf.LanguageDetectorUDF;

public class WordCounterUnitTest {

    DataFrame<String,String> df;

    @Before
    public void initialize() throws IOException {
        /*var df = DataFrame.readCsv("src/sample.csv");

        // Group by the 'Date' column
        var groups = df.groupBy(1);

        // Display the groups
        for (Object group : groups) {
            System.out.println(group);
        }*/
        this.df=DataFrame.read().csv("src/sample.csv");
       

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldXXX() {
        List<Row> allResults = new ArrayList<Row>();
        Map<String,List<Row>> allResultsByPartition = new HashMap<String,List<Row>>();
        df.rows().groupBy("author").forEach(0, (groupKey, group) -> {
            String key = groupKey.item(0);
            WordCounter counter = new WordCounter();
            List<Row>   partitionResults = new ArrayList<Row>();
            group.rows().select(row -> {
                final String title = row.getValue("title");
                final String author = row.getValue("author");
                final String firstLine = row.getValue("first_line");
                // Call your method that returns a Stream of Row
                Stream<Row> rowStream = counter.process(firstLine);
                // Collect all the results into a list
                List<Row> rowList = rowStream.collect(Collectors.toList());
                partitionResults.addAll(rowList);
                return true;
            });

            Stream<Row> rowStream = counter.endPartition();
            List<Row> rowList = rowStream.collect(Collectors.toList());
            partitionResults.addAll(rowList);
            allResultsByPartition.put(key, partitionResults);
            allResults.addAll(partitionResults);
        });
        
        
        assertEquals(allResultsByPartition.get("James Joyce").size(), 42);
        assertEquals(allResultsByPartition.get("Thomas Pynchon").size(), 18);
        assertEquals(allResultsByPartition.get("Herman Melville").size(), 41);
        assertEquals(allResults.size(),101);
    }
   

}
