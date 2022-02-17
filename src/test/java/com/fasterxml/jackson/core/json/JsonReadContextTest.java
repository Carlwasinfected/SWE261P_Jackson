package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.ContentReference;

import java.io.StringWriter;

/**
 * Unit tests for class {@link JsonReadContext}.
 */
public class JsonReadContextTest extends BaseTest
{
    public void testSetAndGetCurrentValue() throws Exception
    {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        jsonReadContext.setCurrentValue("abc");
        assertEquals("abc", jsonReadContext.getCurrentValue());
        jsonReadContext.setCurrentValue(null);
        assertNull(jsonReadContext.getCurrentValue());
    }
    public void testWithAndGetDupDetector() throws Exception
    {
        JsonFactory JSON_F = new JsonFactory();
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        JsonGenerator g = JSON_F.createGenerator(new StringWriter());
        g.writeNumber(100);
        g.close();
        DupDetector dupDetector = DupDetector.rootDetector(g);
        jsonReadContext.withDupDetector(dupDetector);
        assertEquals(jsonReadContext.getDupDetector(),dupDetector);
    }

    public void testGetStartLocation()
    {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        assertEquals(jsonReadContext.getStartLocation("TestUnicode.java").getClass(), JsonLocation.class);
    }

  public void testSetCurrentNameTwiceWithSameNameRaisesJsonParseException() throws Exception
  {
      DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
      JsonReadContext jsonReadContext = new JsonReadContext((JsonReadContext) null, dupDetector, 2441, 2441, 2441);
      jsonReadContext.setCurrentName("dupField");
      try {
          jsonReadContext.setCurrentName("dupField");
          fail("Should not pass");
      } catch (JsonParseException e) {
          verifyException(e, "Duplicate field 'dupField'");
      }
  }

  public void testSetCurrentName() throws Exception
  {
      JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
      jsonReadContext.setCurrentName("abc");
      assertEquals("abc", jsonReadContext.getCurrentName());
      jsonReadContext.setCurrentName(null);
      assertNull(jsonReadContext.getCurrentName());
  }

  public void testReset()
  {
      DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
      JsonReadContext jsonReadContext = JsonReadContext.createRootContext(dupDetector);
      final ContentReference bogusSrc = ContentReference.unknown();

      assertTrue(jsonReadContext.inRoot());
      assertEquals("root", jsonReadContext.typeDesc());
      assertEquals(1, jsonReadContext.startLocation(bogusSrc).getLineNr());
      assertEquals(0, jsonReadContext.startLocation(bogusSrc).getColumnNr());

      jsonReadContext.reset(200, 500, 200);

      assertFalse(jsonReadContext.inRoot());
      assertEquals("?", jsonReadContext.typeDesc());
      assertEquals(500, jsonReadContext.startLocation(bogusSrc).getLineNr());
      assertEquals(200, jsonReadContext.startLocation(bogusSrc).getColumnNr());
  }

}