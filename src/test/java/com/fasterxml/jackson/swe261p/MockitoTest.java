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

import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MockitoTest {

    @Mock
    StringWriter writer;

    @Before
    public void setup() {
        writer = mock(StringWriter.class);
        initMocks(this);
    }

    @Test
    public void mockitoTest() throws Exception {
        assertNotNull(writer);
        when(writer.toString()).thenReturn("1/2/3");
        StringWriter w = new StringWriter();
        WriterBasedJsonGenerator g = (WriterBasedJsonGenerator) new JsonFactory().createGenerator(w);
        g.writeNumber(1);
        g.writeNumber(2);
        g.writeNumber(3);
        g.close();
        assertEquals(g.getContext(), w.toString());
    }

}
