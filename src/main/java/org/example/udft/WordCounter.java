package org.example.udft;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.snowflake.snowpark_java.types.*;
import com.snowflake.snowpark_java.udtf.*;
import com.snowflake.snowpark_java.Row;

public class WordCounter implements JavaUDTF1<String> {

    // Keeps partition-wide counts for each word seen.
    private final Map<String,Integer> wordCounts;

    // Each partition will have a new instance of WordCount
    // constructed, and we are allowed to maintain state over the
    // partition. In this case, we're going to start the partition with
    // an empty map, and keep running counts as we go.
    public WordCounter() {
        wordCounts = new HashMap<>();
    }

    // The process method is called on each record. In this case, we'll
    // split up the line and add the words to our partition-wide count.
    // This method could return per-row values if we wished, but in our
    // case, we'll just return an empty Stream because our results are
    // really for the partition as a whole.
    public Stream<Row> process(String text) {
        // Update the counts with the words in this line.
        for (String word : text.toLowerCase().split("[.,!\"\\s]+")) {
            // If we don't have an entry for the word, set the count to 1.
            // Otherwise, increment the count.
            wordCounts.compute(word, (key, value) -> (value == null) ? 1 : value + 1);
        }

        // We're waiting until the end of the partition to return per-
        // word counts and return nothing here.
        return Stream.empty();
    }

    // The endPartition routine is called at the end of the partition.
    // In our case, this will return the total counts across all lines
    // in the input.
    public Stream<Row> endPartition() {
        // Stream back the word counts for the whole partition. Calling
        // stream() on the keySet enables us to iterate lazily over the
        // contents of the map.
        return wordCounts.keySet().stream().map(word -> Row.create(word, wordCounts.get(word)));
    }

    @Override
    public StructType outputSchema() {
        return StructType.create(
            new StructField("word", DataTypes.StringType),
            new StructField("count", DataTypes.IntegerType));
    }
}
