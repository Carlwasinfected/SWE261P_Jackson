package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import org.junit.Test;

public class JsonWriteContextTest extends com.fasterxml.jackson.core.BaseTest {

    DupDetector dupDetector;
    JsonWriteContext jsonWriteContext;
    public JsonWriteContextTest() {
        dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
    }

    @Test
    public void testCreateChildArray() {
        Object childrenObj = new Object();
        jsonWriteContext = jsonWriteContext.createChildArrayContext(childrenObj);
        assertEquals(childrenObj, jsonWriteContext._currentValue);
        assertTrue(jsonWriteContext.inArray());
    }

    @Test
    public void testCreateChildObject() {
        Object childrenObj = new Object();
        jsonWriteContext = jsonWriteContext.createChildObjectContext(childrenObj);
        assertEquals(childrenObj, jsonWriteContext._currentValue);
        assertTrue(jsonWriteContext.inObject());
    }

    @Test
    public void testWithDups() {
        jsonWriteContext.withDupDetector(dupDetector);
        assertEquals(dupDetector, jsonWriteContext.getDupDetector());
    }

    @Test
    public void testReset() {
        _testReset();
        _testResetWithValue(new Object());
    }

    private void _testReset() {
        jsonWriteContext.reset(JsonStreamContext.TYPE_ROOT);
        assertTrue(jsonWriteContext.inRoot());
        jsonWriteContext.reset(JsonStreamContext.TYPE_OBJECT);
        assertTrue(jsonWriteContext.inObject());
        jsonWriteContext.reset(JsonStreamContext.TYPE_ARRAY);
        assertTrue(jsonWriteContext.inArray());
    }

    private void _testResetWithValue(Object value) {
        jsonWriteContext.reset(JsonStreamContext.TYPE_ROOT, value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
        jsonWriteContext.reset(JsonStreamContext.TYPE_OBJECT, value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
        jsonWriteContext.reset(JsonStreamContext.TYPE_ARRAY, value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }
}
