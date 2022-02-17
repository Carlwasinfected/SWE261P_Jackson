package com.fasterxml.jackson.core.json;

import java.io.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

// For [core#549], ability to use alternate quote characters
public class CustomQuoteCharTest
    extends com.fasterxml.jackson.core.BaseTest
{
    final JsonFactory JSON_F = streamFactoryBuilder()
            .quoteChar('\'')
            .build();

    // Only ASCII range supported as of 2.10
    public void testInvalidQuote() throws Exception
    {
        try {
            streamFactoryBuilder()
                .quoteChar('\u00A0');
            fail("Should not allow quote character outside ASCII range");
        } catch (IllegalArgumentException e) {
            verifyException(e, "Can only use Unicode characters up to 0x7F");
        }
    }

    @Test
    public void testIsEnabledByDefault() {
        for (JsonFactory.Feature f : JsonFactory.Feature.values()) {
            assertEquals(f.enabledByDefault(), true);
        }
    }

    @Test
    public void testMask() {
        int mask = 1;
        for (JsonFactory.Feature f : JsonFactory.Feature.values()) {
            assertEquals(mask, f.getMask());
            mask <<= 1;
        }
    }

    @Test
    public void testFlagsEnabled() {
        JsonFactory.Feature initialFeature = JsonFactory.Feature.values()[0];
        for (int i = 0; i < 16; i++) {
            if (i % 2 == 1)
                assertEquals(initialFeature.enabledIn(i), true);
            else assertEquals(initialFeature.enabledIn(i), false);
        }
        int bit_mask = 1 << 1;
        for (int j = 1; j < JsonFactory.Feature.values().length; j++) {
            boolean mask_result = false;
            for (int i = 0; i < 16; i++) {
                assertEquals(mask_result, JsonFactory.Feature.values()[j].enabledIn(i));
                if ((i + 1) % bit_mask == 0)
                    mask_result = !mask_result;
            }
            bit_mask <<= 1;
        }
    }

    @Test
    public void testCollectDefaults() {
        int flag = 1, bit_mask = 0;
        int arr_length = JsonFactory.Feature.values().length;
        for (int i = 0; i < arr_length; i++) {
            bit_mask = flag | bit_mask;
            flag <<= 1;
        }
        assertEquals(bit_mask, JsonFactory.Feature.collectDefaults());
    }
    
    public void testBasicAposWithCharBased() throws Exception
    {
        StringWriter w;
        JsonGenerator g;

        // with Object
        w = new StringWriter();
        g = createGenerator(JSON_F, w);
        _writeObject(g, "question", "answer");
        g.close();
        assertEquals("{'question':'answer'}", w.toString());

        // with Array
        w = new StringWriter();
        g = createGenerator(JSON_F, w);
        _writeArray(g, "hello world");
        g.close();
        assertEquals("['hello world']", w.toString());
    }

    public void testBasicAposWithByteBased() throws Exception
    {
        ByteArrayOutputStream out;
        JsonGenerator g;

        // with Object
        out = new ByteArrayOutputStream();
        g = createGenerator(JSON_F, out);
        _writeObject(g, "question", "answer");
        g.close();
        assertEquals("{'question':'answer'}", out.toString("UTF-8"));

        // with Array
        out = new ByteArrayOutputStream();
        g = createGenerator(JSON_F, out);
        _writeArray(g, "hello world");
        g.close();
        assertEquals("['hello world']", out.toString("UTF-8"));
    }

    public void testAposQuotingWithCharBased() throws Exception
    {
        StringWriter w;
        JsonGenerator g;

        // with Object
        w = new StringWriter();
        g = createGenerator(JSON_F, w);
        _writeObject(g, "key", "It's \"fun\"");
        g.close();
        // should escape apostrophes but not quotes?
        assertEquals("{'key':'It\\u0027s \\\"fun\\\"'}", w.toString());

        // with Array
        w = new StringWriter();
        g = createGenerator(JSON_F, w);
        _writeArray(g, "It's a sin");
        g.close();
        assertEquals("['It\\u0027s a sin']", w.toString());
    }

    public void testAposQuotingWithByteBased() throws Exception
    {
        ByteArrayOutputStream out;
        JsonGenerator g;

        // with Object
        out = new ByteArrayOutputStream();
        g = createGenerator(JSON_F, out);
        _writeObject(g, "key", "It's \"fun\"");
        g.close();
        // should escape apostrophes but not quotes?
        assertEquals("{'key':'It\\u0027s \\\"fun\\\"'}", out.toString("UTF-8"));

        // with Array
        out = new ByteArrayOutputStream();
        g = createGenerator(JSON_F, out);
        _writeArray(g, "It's a sin");
        g.close();
        assertEquals("['It\\u0027s a sin']", out.toString("UTF-8"));
    }
    
    private void _writeObject(JsonGenerator g, String key, String value) throws Exception {
        g.writeStartObject();
        g.writeStringField(key, value);
        g.writeEndObject();
    }

    private void _writeArray(JsonGenerator g, String value) throws Exception {
        g.writeStartArray();
        g.writeString(value);
        g.writeEndArray();
    }
}
