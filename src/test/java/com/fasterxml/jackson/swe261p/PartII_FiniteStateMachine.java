package com.fasterxml.jackson.swe261p;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class PartII_FiniteStateMachine extends BaseTest {

    @SuppressWarnings("resource")
    public void testIsClosed() throws IOException
    {
        for (int i = 0; i < 8; ++i) {
            String JSON = "[ 1, 2, 3, 4, 5, 6 ]";
            boolean stream = ((i & 1) == 0);//i == 0
            JsonParser jp = stream ?
                    createParserUsingStream(JSON, "UTF-8")
                    : createParserUsingReader(JSON);
            boolean partial = i < 4;

            assertFalse(jp.isClosed());
            assertToken(JsonToken.START_ARRAY, jp.nextToken());//{

            assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//1
            assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//2
            assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//3
            assertFalse(jp.isClosed());

            if (partial) {
                //for first half test cases
                jp.close();
                assertTrue(jp.isClosed());
            } else {
                assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//4
                assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//5
                assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextToken());//6
                assertToken(JsonToken.END_ARRAY, jp.nextToken());//}
                assertNull(jp.nextToken());
                assertTrue(jp.isClosed());
            }
        }
    }


    /**
     * Test Next Value Basic test, using and not using stream
     * @throws IOException
     */
    public void testNextValue() throws IOException {
        testNextValueBasic(true);
        testNextValueBasic(false);
    }

    public void testNextValueNested() throws IOException {
        _testNextValueNested(true);
        _testNextValueNested(false);
    }

    private void testNextValueBasic(boolean useStream) throws IOException
    {
        // first array, no change to default
        JsonParser jp = _getParser("[ 1, 2, 3, 4, 5, 6, 7, 20 ]", useStream);
        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        for (int i = 1; i <= 7; ++i) {
            assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
            assertEquals(i, jp.getIntValue());
        }
        assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
        assertEquals(20, jp.getIntValue());
        assertToken(JsonToken.END_ARRAY, jp.nextValue());
        assertNull(jp.nextValue());
        jp.close();

        // then Object, is different
        jp = _getParser("{ \"3\" :3, \"4\": 4, \"5\" : 5, \"6\" : 6, \"7\" : 7 }", useStream);
        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        for (int i = 3; i <= 7; ++i) {
            assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
            assertEquals(String.valueOf(i), jp.getCurrentName());
            assertEquals(i, jp.getIntValue());
        }
        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        assertNull(jp.nextValue());
        jp.close();

        // and then mixed...
        jp = _getParser("[ true, [ ], { \"a\" : 3 }, false, [{\"b\" : 10}, {\"c\" : 20}], 15 ]", useStream);

        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        assertToken(JsonToken.VALUE_TRUE, jp.nextValue());
        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        assertToken(JsonToken.END_ARRAY, jp.nextValue());

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
        assertEquals("a", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());

        assertEquals(JsonToken.VALUE_FALSE, jp.nextValue());

        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
        assertEquals("b", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());
        assertEquals("c", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        assertToken(JsonToken.END_ARRAY, jp.nextValue());

        assertToken(JsonToken.VALUE_NUMBER_INT, jp.nextValue());

        assertToken(JsonToken.END_ARRAY, jp.nextValue());

        assertNull(jp.nextValue());
        jp.close();
    }

    // [JACKSON-395]
    private void  _testNextValueNested(boolean useStream) throws IOException
    {
        // first array, no change to default
        JsonParser jp;

        // then object with sub-objects...
        jp = _getParser("{\"a\": { \"b\" : true, \"c\": false }, \"d\": { \"x\": [ {\"e\": true}, {\"f\": false} ], \"g\": true}}", useStream);

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertNull(jp.getCurrentName());
        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertEquals("a", jp.getCurrentName());
        assertToken(JsonToken.VALUE_TRUE, jp.nextValue());
        assertEquals("b", jp.getCurrentName());
        assertToken(JsonToken.VALUE_FALSE, jp.nextValue());
        assertEquals("c", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        // ideally we should match closing marker with field, too:
        assertEquals("a", jp.getCurrentName());

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertEquals("d", jp.getCurrentName());

        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        assertEquals("x", jp.getCurrentName());

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertToken(JsonToken.VALUE_TRUE, jp.nextValue());
        assertEquals("e", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertToken(JsonToken.VALUE_FALSE, jp.nextValue());
        assertEquals("f", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());

        assertToken(JsonToken.END_ARRAY, jp.nextValue());

        assertToken(JsonToken.VALUE_TRUE, jp.nextValue());
        assertEquals("g", jp.getCurrentName());

        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        assertNull(jp.getCurrentName());
        assertNull(jp.nextValue());
        jp.close();

        // and arrays
        jp = _getParser("{\"a\": [ false, true ] }", useStream);

        assertToken(JsonToken.START_OBJECT, jp.nextValue());
        assertNull(jp.getCurrentName());
        assertToken(JsonToken.START_ARRAY, jp.nextValue());
        assertEquals("a", jp.getCurrentName());

        assertToken(JsonToken.VALUE_FALSE, jp.nextValue());
        assertNull(jp.getCurrentName());

        assertToken(JsonToken.VALUE_TRUE, jp.nextValue());
        assertNull(jp.getCurrentName());

        assertToken(JsonToken.END_ARRAY, jp.nextValue());
        // ideally we should match closing marker with field, too:
        assertEquals("a", jp.getCurrentName());
        assertToken(JsonToken.END_OBJECT, jp.nextValue());
        assertNull(jp.getCurrentName());
        assertNull(jp.nextValue());
        jp.close();
    }

    private JsonParser _getParser(String doc, boolean useStream)
            throws IOException
    {
        JsonFactory jf = new JsonFactory();
        if (useStream) {
            return jf.createParser(doc.getBytes("UTF-8"));
        }
        return jf.createParser(new StringReader(doc));
    }
}
