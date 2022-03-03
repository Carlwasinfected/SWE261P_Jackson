package com.fasterxml.jackson.swe261p;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MockitoTest {

    @Mock
    File file;
    DataInput input;
    JsonFactory mockFactory;

    @Before
    public void setup() {
        file = mock(File.class);
        input = mock(DataInput.class);
        mockFactory = mock(JsonFactory.class);
    }

    @Test
    public void mockitoTest() throws Exception {
        assertNotNull(file);
        mockFactory.createParser(file);
        verify(mockFactory).createParser(file);
    }

    @Test(expected = IOException.class)
    public void mockitoExceptionTest() throws IOException {
        assertNotNull(input);
        JsonFactory realFactory = new JsonFactory();
        when(realFactory.createParser(input)).thenThrow(new IOException());
        realFactory.createParser(input);
    }
}
