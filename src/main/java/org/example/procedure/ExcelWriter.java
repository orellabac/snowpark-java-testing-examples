package org.example.procedure;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.util.SessionUtils;

import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Session;
import com.snowflake.snowpark_java.sproc.JavaSProc2;

import java.io.*;

public class ExcelWriter implements JavaSProc2<String,String,String> {

    public String call(Session session, String query, String filePath) {
        DataFrame df = session.sql(query);
        com.snowflake.snowpark_java.Row[] rows = df.collect();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            String[] headers = df.schema().names();
            // Write headers
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }            

            // Write data to cells
            int rowNum = 1;
            for (var rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (Object cellData : rowData.toList()) {
                    Cell cell = row.createCell(colNum++);
                    if (cellData instanceof String) {
                        cell.setCellValue((String) cellData);
                    } else if (cellData instanceof Integer) {
                        cell.setCellValue((Integer) cellData);
                    } else if (cellData instanceof Double) {
                        cell.setCellValue((Double) cellData);
                    } else if (cellData instanceof Boolean) {
                        cell.setCellValue((Boolean) cellData);
                    } else {
                        cell.setCellValue(cellData.toString());
                    }
                }
            }

             // Write workbook to ByteArrayOutputStream
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             workbook.write(outputStream);
             // Convert ByteArrayOutputStream to InputStream
             session.file().uploadStream(filePath,new ByteArrayInputStream(outputStream.toByteArray()),false);
             return "Done";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    /**
     * Main entrypoint. Runs the stored procedure locally for development.
     * @param args
     */
    public static void main(String[] args) {
        Session session = SessionUtils.from_snowsql().getOrCreate();
        String filePath = "@mystage/output.xlsx";
        String res = new ExcelWriter().call(session, "SELECT * FROM UNITTESTING.PUBLIC.EMPLOYEE", filePath);
        System.out.println(res);
    }
}
