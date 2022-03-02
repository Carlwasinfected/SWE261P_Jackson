package com.fasterxml.jackson.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

public class TestDupDetector {

    JsonFactory FACTORY = new JsonFactory();

    @Test
    public void testFindLocation() throws IOException {
        String carInfo = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        String EXPECTED_LOCATION_RESULT = "[Source: (String)\"{ \"brand\" : \"Mercedes\", \"doors\" : 5 }\";" +
                " line: 1, column: 1]";
        JsonParser jsonParser = FACTORY.createParser(carInfo);
        DupDetector dupDetector = DupDetector.rootDetector(jsonParser);
        Assert.assertEquals(EXPECTED_LOCATION_RESULT, dupDetector.findLocation().toString());
    }

    @Test
    public void testFindLocationIsNull() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        Assert.assertNull(dupDetector.findLocation());
    }

    @Test
    public void testChild() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        Assert.assertEquals(dupDetector.findLocation(), dupDetector.child().findLocation());
    }

    @Test
    public void testIsDup() throws IOException {
        String carInfo = "{ \"brand\" : \"Mercedes\", \"doors\" : 5, \"price\": 40000, \"model\": \"C300\"}";
        DupDetector dupDetector = DupDetector.rootDetector(FACTORY.createParser(carInfo));
        Assert.assertFalse(dupDetector.isDup("brand"));
        Assert.assertTrue(dupDetector.isDup("brand"));

        Assert.assertFalse(dupDetector.isDup("doors"));
        Assert.assertTrue(dupDetector.isDup("doors"));

        Assert.assertFalse(dupDetector.isDup("price"));
        Assert.assertFalse(dupDetector.isDup("model"));

    }


}
