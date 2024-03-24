package org.example.util;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import com.snowflake.snowpark_java.Session;
import com.snowflake.snowpark_java.SessionBuilder;

public class SessionUtils {

    private static Map<String, String> read_snowsql_config(String sectionName) throws IOException {
        String homeDir = System.getProperty("user.home");
        String filePath = homeDir + "/.snowsql/config";
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentSection = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                } else if (currentSection != null && currentSection.equals(sectionName) && line.contains("=")) {
                    String[] parts = line.split("=");
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    properties.put(key, value);
                }
            }
        }

        return properties;
    }

    public static SessionBuilder from_snowsql() {
        try {
            SessionBuilder builder = Session.builder();
            Map<String,String> configs = read_snowsql_config("connections");
            String accountname = configs.getOrDefault("accountname","missing");
            String url =  "https://" + accountname + ".snowflakecomputing.com";
            builder.config("URL", url);
            builder.config("USER",configs.get("username"));
            builder.config("PASSWORD",configs.get("password"));
            builder.config("ROLE",configs.get("rolename"));
            builder.config("DB",configs.get("dbname"));
            builder.config("SCHEMA",configs.get("schemaname"));
            builder.config("WAREHOUSE",configs.get("warehousename"));
            builder.configs(configs);
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }

}
