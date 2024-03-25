package org.example.procedure;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.DataFrameWriter;
import com.snowflake.snowpark_java.Session;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class Proc1UnitTest {
    @Mock
    Session session;

    @Test
    public void shouldCallAnAppendOnProducts()
    {
        session = mock(Session.class);
        // Mocking DataFrame
        DataFrame dataFrameMock = mock(DataFrame.class);
        DataFrameWriter dataFrameWriterMock = mock(DataFrameWriter.class);
        // Mocking session.createDataFrame() to return the mocked DataFrame
        when(session.createDataFrame(any(), any())).thenReturn(dataFrameMock);
        // Mocking DataFrameWriter.write() to return the mocked DataFrameWriter
        when(dataFrameMock.write()).thenReturn(dataFrameWriterMock);
        // Mocking DataFrameWriter.mode() to return the mocked DataFrameWriter
        when(dataFrameWriterMock.mode("append")).thenReturn(dataFrameWriterMock);
        // Calling the PROC
        String result = new Proc1().call(session);
        // Assert that the method call was successful
        assertEquals("Done", result);
        // Verify that saveAsTable method is called with a particular string parameter
        verify(dataFrameWriterMock).saveAsTable(eq("PUBLIC.PRODUCT"));
    }
}
