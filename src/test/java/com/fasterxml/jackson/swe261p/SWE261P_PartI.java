package com.fasterxml.jackson.swe261p;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public class SWE261P_PartI extends BaseTest {
    public void testKeywords() throws Exception
    {
        final String DOC = "{\n"
                +"\"key1\" : null,\n"
                +"\"key4\" : [ false, null, true ]\n"
                +"}"
                ;
        final String DOC2 = "{\n"
                +"\"key1\" : null,\n"
                +"\"key4\" : [ false, null, true ]\n";

        JsonParser p = createParserUsingReader(JSON_FACTORY, DOC);
        JsonParser p2 = createParserUsingReader(JSON_FACTORY, DOC2);
        findTag(p, true);
        findArrayTag(p, true);
        parseWrongTag(p2, true);
        handleSuperLargeTag();
        p.close();
    }
    //Test whether Jackson could get the tag:
    private void findTag(JsonParser p, boolean checkColumn) throws Exception
    {
        JsonStreamContext ctxt = p.getParsingContext();
        assertEquals("/", ctxt.toString());
        assertToken(JsonToken.START_OBJECT, p.nextToken());
        assertToken(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals(p.getCurrentName(), "key1");
    }
    //Test whether Jackson could get the array tag:
    private void findArrayTag(JsonParser p, boolean checkColumn) throws Exception
    {
        JsonStreamContext ctxt = p.getParsingContext();
        assertToken(JsonToken.VALUE_NULL, p.nextToken());

        assertToken(JsonToken.FIELD_NAME, p.nextToken());
        verifyFieldName(p, "key4");
        assertToken(JsonToken.START_ARRAY, p.nextToken());
        ctxt = p.getParsingContext();
        assertTrue(ctxt.inArray());
        assertNull(ctxt.getCurrentName());
        assertEquals("key4", ctxt.getParent().getCurrentName());

        assertToken(JsonToken.VALUE_FALSE, p.nextToken());
        assertEquals("[0]", ctxt.toString());

        assertToken(JsonToken.VALUE_NULL, p.nextToken());
        assertToken(JsonToken.VALUE_TRUE, p.nextToken());
        assertToken(JsonToken.END_ARRAY, p.nextToken());
    }
    //Test whether Jackson could parse the wrong format JSON:
    private void parseWrongTag(JsonParser p, boolean checkColumn) throws Exception
    {
        JsonStreamContext ctxt = p.getParsingContext();
        assertEquals("/", ctxt.toString());
        assertToken(JsonToken.START_OBJECT, p.nextToken());
        assertToken(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals(p.getCurrentName(), "key1");
        assertToken(JsonToken.VALUE_NULL, p.nextToken());
    }
    // Test whether Jackson could handle really large tag name:
    public void handleSuperLargeTag() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append("LargeTagName");
        } while (sb.length() < 1000000);
        String tag = sb.toString();
        String DOC = "{\n"
                +"\"" + tag + "\": null,\n"
                +"}"
                ;
        JsonParser p = createParserUsingReader(JSON_FACTORY, DOC);
        JsonStreamContext ctxt = p.getParsingContext();
        assertToken(JsonToken.START_OBJECT, p.nextToken());
        assertToken(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals(p.getCurrentName(), sb.toString());
        assertToken(JsonToken.VALUE_NULL, p.nextToken());
    }
}