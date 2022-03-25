# SWE261P Final Report

> *Team Members: [Can Wang](mailto:canw7@uci.edu), Kaiqin Chen, Tianren Tan*
> 

# Part I: ****Introduction, Set Up and Partitioning****

## 1. Intro and Overview

### 1.1 Intro to Jackson

`Jackson` is a popular Java library that provides low-level streaming parsers and generators for converting real-world data among different data formats, such as `XML`, `JSON`, `ProtoBuf`, `CSV`, etc.  Compared with other build-in data parsing and converting libraries, such as `org.json`, Jackson has much better performance when facing different usage scenarios, for example, concurrency read and write.[[1]](https://github.com/FasterXML/jackson)

The GitHub repository link of our team is [https://github.com/Carlwasinfected/SWE261P_Jackson](https://github.com/Carlwasinfected/SWE261P_Jackson). Our team members are *Can Wang*, *Kaiqin* Chen, and *Tianren Tan*. Since it is a private repository, we have already sent the invitation to Prof. Jones and TAs as collaborators.

### 1.2 General Overview

In order to gain a general overview of the project, we have done some researches on a few important information, which come as follows.

First, we used the command below to see the number of lines of source code(Java files in our case), the result is `77800`.

![Fig 1-1-1 LOC of the project](SWE261P%20Fi%2041846/Untitled.png)

Fig 1-1-1 LOC of the project

Second, we used the command below to see the amount of source code files(Java files in our case), the result is `283`.

![Fig 1-1-2 Source code files amount](SWE261P%20Fi%2041846/Untitled%201.png)

Fig 1-1-2 Source code files amount

Third, we knew the languages used from the GitHub repository page, which appears as a nearly pure Java project.

![Fig 1-1-3 Languages ](SWE261P%20Fi%2041846/Untitled%202.png)

Fig 1-1-3 Languages 

## 2.Setup & Build

### 2.1 Fork & Clone to the local machine

The first thing we did is to fork the project into our own repository. The original repository was at [https://github.com/FasterXML/jackson-core](https://github.com/FasterXML/jackson-core), which was shown below.

![Fig 1-2-1 Fork button](SWE261P%20Fi%2041846/Untitled%203.png)

Fig 1-2-1 Fork button

We used the button as pointed by the red arrow to fork the project, then typed the command below to clone the project by Git into the local disk.

`git clone [https://github.com/Carlwasinfected/SWE261P_Jackson.git](https://github.com/Carlwasinfected/SWE261P_Jackson.git)`

### 2.2 Build

After the operations above, we would like to open the project in an IDE and try to build the project. After discussion, our team decided to use `IntelliJ IDEA` provided by JetBrain as IDE.

The project used `Maven` as the building tool. After opening the project in IntelliJ IDEA, Maven will scan the configuration and related indexes automatically. Also, we can edit the `pom.xml` file under the project root to change the default configuration, and then use the build window shown below to sync and rebuild the project.

![Fig 1-2-2 Build result](SWE261P%20Fi%2041846/Untitled%204.png)

Fig 1-2-2 Build result

### 2.3 Notes

The project was originally using Junit 4 as the testing tool due to some historical reasons. To migrate the toolkit to Junit 5 but maintain the old codes runnable, we changed some configurations at pom.xml so that it is both available for the testing files using JUnit 4 and JUnit5.

The main changes are shown below, you can also find them at pom.xml from line 14 to line 41.

![Fig 1-2-3 Dependencies](SWE261P%20Fi%2041846/Untitled%205.png)

Fig 1-2-3 Dependencies

## 3. Study of Existing Test Cases

In this course, we specifically focus on the `Jackson-core` library. The testing framework for `Jackson-core` is `JUnit`. `JUnit` demonstrates various types of testing cases, and in our project, we extracted the following patterns:

### 3.1 Fixture

In order to produce comprehensive results, tests sometimes need to be run on fixed backgrounds of a known set of existing objects. It describes a fixed state of numerous objects provided as the baseline for running tests. An obvious advantage for fixtures is the overall environment is well-known and we are able to produce repeatable results.

A common way of implementing fixtures is through the annotation ”@Before”, which is usually corresponding to “@After”. In Jackson, the existing test cases use constructors instead of annotations for initialization.

![Fig 1-3-1 Test case](SWE261P%20Fi%2041846/Untitled%206.png)

Fig 1-3-1 Test case

### 3.2 Test Runners

Another feature of JUnit we discovered was test running. JUnit framework involves the specified class as a test runner instead of using the default runner if a JUnit class contains the annotation “@RunWith”. According to JUnit documentation, @RunWith only contains one element ‘value’ as shown, which must extend the abstract Runner class. Runner classes are responsible for running JUnit tests, typically using reflection.

![Fig 1-3-2 Runners](SWE261P%20Fi%2041846/Untitled%207.png)

Fig 1-3-2 Runners

As we can see, the TrailingCommasTest class is annotated with @RunWith, with the value of Parameterized .class, indicating that running test cases in this class is under the responsibility of the class Parameterized.

![Fig 1-3-3 `@RunWith`](SWE261P%20Fi%2041846/Untitled%208.png)

Fig 1-3-3 `@RunWith`

### 3.3 Examples of Existing Test Cases

A brief scope of the whole project:

![Fig 1-3-4 Overview](SWE261P%20Fi%2041846/Untitled%209.png)

Fig 1-3-4 Overview

Existing test cases are classified into different packages based on purpose and functionality.

Here is an example test function from JsonLocationTest.java

```java
public void testBasicToString() throws Exception
    {
        // no location; presumed to be Binary due to defaulting
        assertEquals("[Source: UNKNOWN; line: 3, column: 2]",
                new JsonLocation(null, 10L, 10L, 3, 2).toString());

        // Short String
        assertEquals("[Source: (String)\"string-source\"; line: 1, column: 2]",
                new JsonLocation(_sourceRef("string-source"), 10L, 10L, 1, 2).toString());

        // Short char[]
        assertEquals("[Source: (char[])\"chars-source\"; line: 1, column: 2]",
                new JsonLocation(_sourceRef("chars-source".toCharArray()), 10L, 10L, 1, 2).toString());

        // Short byte[]
        assertEquals("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]",
                new JsonLocation(_sourceRef("bytes-source".getBytes("UTF-8")),
                        10L, 10L, 1, 2).toString());

        // InputStream
        assertEquals("[Source: (ByteArrayInputStream); line: 1, column: 2]",
                new JsonLocation(_sourceRef(new ByteArrayInputStream(new byte[0])),
                        10L, 10L, 1, 2).toString());

        // Class<?> that specifies source type
        assertEquals("[Source: (InputStream); line: 1, column: 2]",
                new JsonLocation(_rawSourceRef(true, InputStream.class), 10L, 10L, 1, 2).toString());

        // misc other
        Foobar srcRef = new Foobar();
        assertEquals("[Source: ("+srcRef.getClass().getName()+"); line: 1, column: 2]",
                new JsonLocation(_rawSourceRef(true, srcRef), 10L, 10L, 1, 2).toString());
    }
```

The JsonLocation class is an object that encapsulates location information used for reporting parsing errors as well as current location within input streams. This function tests if the generated JSON string is equivalent to the desired output format.

## 4. How to run test cases

### 4.1 Dependency

In order to run existing test cases, we introduce JUnit into the project modules. There are different ways to add the JUnit dependency. You could either choose to use Maven or Gradle or stick with plain-old JAR files. The existing project includes JUnit through importing jar files as module dependencies.

![Fig 1-3-5 Dependency](SWE261P%20Fi%2041846/Untitled%2010.png)

Fig 1-3-5 Dependency

### 4.2 Running Tests

The latest JUnit tests are usually annotation-based, eg. @Test, @Before, @After, @Ignores, and more. A portion of existing test cases in Jackson uses an alternative but rather old-classed way, which is through extending the abstract TestCase. This is because it was initially built to be compatible with older versions. We prefer @Test annotations more since it is more explicit and much easier to support in various tools, exhibiting flexibility and providing convenience.

Beneath are two examples, the first using extending and the second using annotation.

![Fig 1-3-6 Test case I ](SWE261P%20Fi%2041846/Untitled%2011.png)

Fig 1-3-6 Test case I 

![Fig 1-3-7 Test case II](SWE261P%20Fi%2041846/Untitled%2012.png)

Fig 1-3-7 Test case II

The above images were both shot in IntelliJ IDEA. We can see that on the left-hand side there is a small green arrow indicating “Run Test”. By pressing this button we can choose to run this specific test only without worrying about other testing methods in the whole class.

![Fig 1-3-8 Test case result](SWE261P%20Fi%2041846/Untitled%2013.png)

Fig 1-3-8 Test case result

After running the test, we can see on the console “Tests passed: X of X test - XXXms”, indicating how many tests passed out of the total.

An alternative way of running JUnit tests would be right-clicking on the class file, selecting “Run XXXXX” (or “Run” in this case). Then the IDE will perform all runnable tests in the file.

![Fig 1-3-9 Run option](SWE261P%20Fi%2041846/Untitled%2014.png)

Fig 1-3-9 Run option

The third way is through run configuration. On the top right of the IDE click the current configuration, select “Edit Configuration”.

![Fig 1-3-10 Configurations](SWE261P%20Fi%2041846/Untitled%2015.png)

Fig 1-3-10 Configurations

Inside the popup window, we could select “Method” or “Class” in the left dropbox to either run a specific method or run the whole class. In the middle dropbox, we can specify which class we wish to test. If “Method” was selected, we can then decide which exact method to run.

![Fig 1-3-11 Configurations](SWE261P%20Fi%2041846/Untitled%2016.png)

Fig 1-3-11 Configurations

On the other hand, if we select “Class”, then the result will be the same as the second approach, meaning the whole test class will be executed.

![Fig 1-3-12 Result](SWE261P%20Fi%2041846/Untitled%2017.png)

Fig 1-3-12 Result

## 5. Partitioning

### 5.1 Intro to Systematic functional testing and partition testing

Systematic functional testing is a black-box testing technique. The idea of this method is to select inputs that are especially valuable and has great potential to cause the program to fail often or not at all.

Partition testing is also a black-box technique. It can be applied at any level of testing and is often a good technique to use first. The idea is to divide input space into classes and then separately execute the program.

### 5.2 What we have learned

For systematic functional testing, we can find bugs and remove them more effectively.

For partition testing, we can sample each class. With this method, we can easily reveal the fault.

### 5.3 Jackson Core: Streaming Partitioning Test case

In the project, we split features into 4 new types, to separate JSON-specific features from general "format-agnostic" features.

### 5.4 Our partitions and boundaries

In this project, we select `createParserUsingReader` as our partition feature.

In the test case, we try to figure out whether Jackson Core can process the correct JSON file as well as an incorrect JSON file using `asserEquals` function.

For boundaries, we use the length of JSON tag as ranges, we need to test super large JSON tag as input.

### 5.5 Write new test cases

```java
public class SWE261P extends BaseTest {
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
```

## Reference

[1]. [https://github.com/FasterXML/jackson](https://github.com/FasterXML/jackson)

---

# Part II: Functional Testing and Finite State Machines

## 1. Introduction to Finite State Machines

A **finite-state machine** (**FSM**) is a mathematical model of computation based on a hypothetical machine made of one or more states and had a very limited memory. The state will change based on inputs, providing the resulting output for implemented changes and these changes can be called as transitions.

A simple mechanism for a state machine will be a turnstile, see the picture below.

There are two possible states which are Locked and Unlocked. And there are two inputs that might change the state which are Coin and Push. This photo is kind of like directed graph in data structure view. Each node is represents a state. Each edge is represents a transition.

![Untitled](SWE261P%20Fi%2041846/Untitled%2018.png)

If FSM were represented mathematically, it would look something like this.

$$
\begin{aligned}
FSM &= \{Q,\ E,\ q_0,\ F,\ \delta\} \\
Q &= Set\ of\ all\ states \\
E &= Inputs \\
q_0 &= Start/initial\ state \\
F &= Set\ of \ final\ states \\
\delta &= Transition\ function\ from\ \{Q,E\}\ to\ Q
\end{aligned}
$$

## 2. How Finite Models Can be Useful for Testing

With finite models when we try to test our project, we first can draw a state diagram or a state-transition table. These tools can be a great help when we want to check the completeness and correctness of our program. The state-transition table below is based on the turnstile example given above.

| Current State | Input | Next State | Output |
| --- | --- | --- | --- |
| Locked | coin | Unlocked | Unlocks the turnstile so that the customer can push through. |
| Locked | push | Locked | None |
| Unlocked | coin | Unlocked | None |
| Unlocked | push | Locked | When the customer has pushed through, locks the turnstile. |
1. Help analyze what states the program has
2. Help analyze what input might be
3. Help analyze what start/initial state is and what final states are
4. Help analyze what state transitions the program has
5. Help improve test code coverage when we know all the branches
6. Help locate bugs in which transition or branch

## 3. The Feature We Chose

In this project, `Jackson-core`, the usage is to parse, convert and manipulate tons of data elements, such as `XML`, `JSON`, etc. In the functional model below, we extracted mainly two features from the framework, which come as follows:

- Convert `JSON` raw string to stream
- parse the stream and manipulate the related elements, such as `JSONArray`, `JSONObject`, etc.

## 4. Description of our functional model and diagram

The process of our model is as follows.

1. Given the input raw string, building a `JSON` stream.
2. By using the APIs from  `Jackson`, get current token
3. Convert the element of current token to the value with correct data type correspondingly.
4. Iteration of 3 & 4, until reach the end of the stream.

 

![Diagram of *Our Functional Model’s Work Flow*](SWE261P%20Fi%2041846/Untitled%2019.png)

Diagram of *Our Functional Model’s Work Flow*

## 5. Write Test Cases

The test cases are stored in the directory : 

`/src/test/java/com/fasterxml/jackson/swe261p/PartII_FiniteStateMachine.java`

The tests within are written below:

```java
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

	//System support utils
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
```

Explaining the code:

1. The first test is `testIsClosed`. This one is responsible for checking whether a JSON Parser stream is correctly closed. 
    
    The basic input is an array of 6 numbers, correspondingly from 1 to 6. We perform 8 tests on the data. For the first round `(i & 1) == 0`, we create the parser with an UTF-8 stream, and default for others.
    
    We then split the conditions into two major parts: whether to close `JsonParser`  in advance. For the first 4 tests we close it before it completes the iteration through all the elements, and check if the stream is closed as expected. For the other half, we traverse through the entire JSON object, then check if it correctly reaches the end of the array and closes automatically.
    

```java
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
```

1. Our second test is `testNextValue`, which checks if the parser is extracting the correct data from an array or object.
    
    We start with basic arrays.
    

```java
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
```

Then we move on to objects. Objects have an additional attribute `currentName` for each element, so we need to check if the key and value are in the correct position.

```java
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
```

Finally we test a mixed combination of arrays and objects.

```java
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
```

1. The final test is `testNextValueNested`,  which performs iteration on complex objects with several inner objects or elements.
    
    We focus on the token positions, such as `START_OBJECT`, `START_ARRAY`,`END_OBJECT`, `END_ARRAY` and also the attribute names and properties, using `assertToken` and `assertEquals`.
    

```java
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
```

## Reference

1. ****[What is a Finite State Machine?](https://medium.com/@mlbors/what-is-a-finite-state-machine-6d8dec727e2c)****
2. ****[Finite state machines](https://isaaccomputerscience.org/concepts/dsa_toc_fsm?examBoard=all&stage=all)****
3. **[Finite-state machine[Wikipedia]](https://en.wikipedia.org/wiki/Finite-state_machine)**
4. **[Finite State Machine (Finite Automata)[Youtube]](https://www.youtube.com/watch?v=Qa6csfkK7_I)**
5. ****[Model Based Testing Tutorial: What is, Tools & Example](https://www.guru99.com/model-based-testing-tutorial.html)****

---

# Part III: Structural (White Box) Testing

## 1. Intro to Structural Testing

### 1.1 What is Structural Testing

Structural testing, also known as glass box testing or white box testing is an approach where the tests are derived from the knowledge of the software's structure or an internal implementation. 

The other names of structural testing include clear box testing, open box testing, logic-driven testing, or path-driven testing. [[1]](https://www.notion.so/SWE261P-Part-III-Structural-White-Box-Testing-5651d2d773d3484f8e3483e9fc96cd27) 

### 1.2 Pros

- Forces test developer to reason carefully about implementation
- Find errors hidden in branches
- Spots the Dead Code or other issues with respect to best programming practices.
- Helps you find defects as early as possible.

### 1.3 Cons

- Expensive for some small applications have to spend both time and money to perform white box testing.
- Every possibility that a few lines of code are missed accidentally. [[2]](https://www.notion.so/SWE261P-Part-III-Structural-White-Box-Testing-5651d2d773d3484f8e3483e9fc96cd27)
- In-depth knowledge about the programming language is necessary to perform white box testing.

## 2. Coverage Tool Usage & Analysis

### 2.1 How to use `JaCoCo` in `Intellij IDEA`

In our project, we used `JaCoCo` as a code coverage tool. `JaCoCo` is a Java-based code coverage detector library and it is easy to configure and use. Also, `JaCoCo` has the feature to produce the coverage testing report as an HTML file in the browser or in the built-in window of `IntelliJ IDEA`, and it will clearly show you the coverage of classes, methods, lines, branches of the selected packages or classes.  [[3]](https://www.notion.so/SWE261P-Part-III-Structural-White-Box-Testing-5651d2d773d3484f8e3483e9fc96cd27)

Since it is a `Maven` project, we need to add some imports at `pom.xml` to use `JaCoCo`.  Starting at `line 105` and ending at `line 123`,  you can see the configuration of `JaCoCo`. To use `JaCoCo` to generate the testing report, we also edit the default run configuration in the `Intellij IDEA`.  The code coverage configuration we used is shown below. 

![2-1 Configuration Window](SWE261P%20Fi%2041846/Untitled%2020.png)

2-1 Configuration Window

After completing the configuration correctly, we can simply click `run with coverage`. A result example is shown below. This is an HTML-format report generated by `JaCoCo`. It indicates that in the package `com.fasterxml.jackson.core`, the line coverage is `87.5%`, the class coverage is `96.5%`, the method coverage is `83.5%`, and the block coverage is `67.3%`.

![2-2 Original Coverage Report](SWE261P%20Fi%2041846/Untitled%2021.png)

2-2 Original Coverage Report

### 2.2 Analysis of Original Coverage

We would like to focus on `com.fasterxml.jackson.core.json`, the original coverage result is shown below in the table.

| Measurements | Hit | Total | Coverage |
| --- | --- | --- | --- |
| Method | 733 | 1022 | 71% |
| Line | 8110 | 13182 | 61% |
| Branch | 2626 | 6131 | 42% |

More importantly, `JaCoCo` has the feature to show the coverage overview of each source code file in the IDE project structure view window. The highlighting of the coverage condition is shown below.

![2-3 Coverage of `core.json`](SWE261P%20Fi%2041846/Untitled%2022.png)

2-3 Coverage of `core.json`

Take `DupDetector` as an example, after running the test cases with `JaCoCo`, we look into the source code, then it is easy to see the coverage of it as shown below.  For the function below, the lines with a red bar, starting at `line97` and ending at `line 109`, are never reached via the original test suites.  The line with a brown bar indicates that the statement is only reached just once.

![2-4 Example of Uncovered Code](SWE261P%20Fi%2041846/Untitled%2023.png)

2-4 Example of Uncovered Code

By analyzing some parts of the codes, as shown above, we found that it is crucial to improve the coverage of `DupDetector`, whose method coverage is `62%`, line coverage is `48%`; `JsonReadContext`, whose method coverage is `73%`, line coverage is `87%`; `JsonWriteContext`, whose method coverage is `38%`, line coverage is `51%`. 

## 3. New Test Cases & Coverage Improvement

### 3.1 Overview of Our Improvement

| Function | Method Before | Method After | Line Before | Line After |
| --- | --- | --- | --- | --- |
| DupDetector | 62% | 100% | 48% | 100% |
| JsonReadContext | 73% | 100% | 87% | 100% |
| JsonWriteContext | 38% | 71% | 51% | 83% |

### 3.2 New Test Cases

### 3.2.1 Improvement of `DupDetector`

The new test cases are at `com/fasterxml/jackson/core/json/TestDupDetector.java`.  Here I created four functions as follows, improving the method coverage from `62%` to `100%`, the line coverage from `48%` to `100%`. 

- The function tests `findLocation()` when the return object is not `null`.

```java
@Test
    public void testFindLocation() throws IOException {
        String carInfo = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
        String EXPECTED_LOCATION_RESULT = "[Source: (String)\"{ \"brand\" : \"Mercedes\", \"doors\" : 5 }\";" +
                " line: 1, column: 1]";
        JsonParser jsonParser = FACTORY.createParser(carInfo);
        DupDetector dupDetector = DupDetector.rootDetector(jsonParser);
        Assert.assertEquals(EXPECTED_LOCATION_RESULT, dupDetector.findLocation().toString());
    }
```

- The function tests `findLocation()` when the `DupDetector` has no source, which will return `null`.

```java
@Test
    public void testFindLocationIsNull() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        Assert.assertNull(dupDetector.findLocation());
    }
```

- The function tests `child()` which will return a fork of current `DupDetector` Object.

```java
@Test
    public void testChild() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        Assert.assertEquals(dupDetector.findLocation(), dupDetector.child().findLocation());
    }
```

- The function tests `isDup()` and improves it by covering all five logic branches.

```java
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
```

### 3.3.2 Improvement of  `JsonReadContext`

The new test cases are at `com/fasterxml/jackson/core/json/JsonReadContextTest.java`.  Here I created three functions as follows, improving the method coverage from `73%` to `100%`, the line coverage from `87%` to `100%`. 

- The function tests `setCurrentValue()` and `getCurrentValue()` .

```java
@Test
	public void testSetAndGetCurrentValue() throws Exception
    {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        jsonReadContext.setCurrentValue("abc");
        assertEquals("abc", jsonReadContext.getCurrentValue());
        jsonReadContext.setCurrentValue(null);
        assertNull(jsonReadContext.getCurrentValue());
    }
```

- The function tests `withDupDetector()` and `getDupDetector()` . In order to test these methods, we first create a `JsonReadContext` object and a `DupDetector` object and call `withDupDetector()` to revise the `JsonReadContext` object. Then we call `getDupDetector()` to check if the `DupDetector` in `JsonReadContext` is equal.

```java
@Test
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
```

- The function tests `getStartLocation()` which will return the start location of file and here we just `assertEquals` that we get `JsonLocation` class.

```java
@Test
	public void testGetStartLocation()
    {
        JsonReadContext jsonReadContext = JsonReadContext.createRootContext(0, 0, (DupDetector) null);
        assertEquals(jsonReadContext.getStartLocation("TestUnicode.java").getClass(), JsonLocation.class);
    }
```

### 3.3.3 Improvement of  `JsonWriteContext`

The new test cases are at `com/fasterxml/jackson/core/json/JsonWriteContextTest.java`.  Here I created three functions as follows, improving the method coverage from `38%` to `71%`, the line coverage from `51%` to `83%`. 

- Initially we define a constructor for fixed object testing.

```java
DupDetector dupDetector;
JsonWriteContext jsonWriteContext;
public JsonWriteContextTest() {
    dupDetector = DupDetector.rootDetector((JsonGenerator) null);
    jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
}
```

- The first two tests check if `JsonWriteContext` is correctly reset to specified type with and without a value. By calling `inRoot(), inObject(), inArray()` we can determine if the context is a type of `TYPE_ROOT`, `TYPE_OBJECT`, or `TYPE_ARRAY`.

```java
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
```

- The next function tests inputting an object as current value. The context resets to `TYPE_OBJECT` , which can be tested by calling `inObject()`.

```java
@Test
public void testCreateChildObject() {
    Object childrenObj = new Object();
    jsonWriteContext = jsonWriteContext.createChildObjectContext(childrenObj);
    assertEquals(childrenObj, jsonWriteContext._currentValue);
    assertTrue(jsonWriteContext.inObject());
}
```

- Correspondingly, the fourth test tests inputting an array as current value. The context respectively resets to `TYPE_ARRAY`, which can be tested by calling `inArray()`.

```java
public void testCreateChildArray() {
    Object childrenObj = new Object();
    jsonWriteContext = jsonWriteContext.createChildArrayContext(childrenObj);
assertEquals(childrenObj, jsonWriteContext._currentValue);
assertTrue(jsonWriteContext.inArray());
}
```

- The final test checks if the function `withDupDetector` correctly assigns the passed argument as the new `DupDetector`

```java
@Test
public void testWithDups() {
    jsonWriteContext.withDupDetector(dupDetector);
    assertEquals(dupDetector, jsonWriteContext.getDupDetector());
}
```

## Reference

[1]:  [https://www.tutorialspoint.com/software_testing_dictionary/structural_testing.htm](https://www.tutorialspoint.com/software_testing_dictionary/structural_testing.htm)

[2]: [https://www.geeksforgeeks.org/structural-software-testing/](https://www.geeksforgeeks.org/structural-software-testing/)

[3]: [https://www.baeldung.com/jacoco](https://www.baeldung.com/jacoco)

---

# Part IV ****Continuous Integration****

## 1. Intro to Continuous Integration

What is continuous integration? Continuous integration (CI) is the merging of copies of all developers' changes onto the same thread multiple times a day. Each merge automatically triggers a code build and test sequence, which we want to keep under ten minutes. [[1]](https://www.notion.so/SWE261P-Part-IV-Continuous-Integration-68787cfcc8c242f7b6597cd94f7ea560)

A common workflow for Continuous Integration is like this: [[2]](https://www.notion.so/SWE261P-Part-IV-Continuous-Integration-68787cfcc8c242f7b6597cd94f7ea560)

1. ****Run tests locally****

Before committing to the mainline, the developer needs to ensure that all unit tests pass in the local environment.

1. ****Compile code in CI****

Compile the code periodically in the server or after each commit and generate a report to the developer.

1. ****Run tests in CI****

In addition to running unit and integration tests, the system also runs measurement and configuration performance, additional static analysis, and extracting and formatting documents from source code, which can help with manual QA.

1. **Deploy an artifact from CI**

In the so-called CI/CD pipeline, the relationship between CI and continuous delivery or continuous deployment is like a cycle.

This was done through CI/CD pipeline in preparation for release to production. After that, continuous delivery still requires all code changes to be deployed to test and production environments for continuous integration.

![Fig 1-1 *Continuous Integration Process[[3]](https://www.notion.so/SWE261P-Part-IV-Continuous-Integration-68787cfcc8c242f7b6597cd94f7ea560)*](SWE261P%20Fi%2041846/Untitled%2024.png)

Fig 1-1 *Continuous Integration Process[[3]](https://www.notion.so/SWE261P-Part-IV-Continuous-Integration-68787cfcc8c242f7b6597cd94f7ea560)*

## 2. Why Continuous Integration

In the past, developers worked mostly on their own and didn't merge their changes into the main branch halfway through. This makes it extremely difficult and time-consuming to merge code changes, resulting in a lot of code that must be discarded due to the long accumulation of errors. This situation makes it more difficult to deliver updates to customers quickly.[[3]](https://www.notion.so/SWE261P-Part-IV-Continuous-Integration-68787cfcc8c242f7b6597cd94f7ea560) 

So why do we choose continuous integration? Because continuous integration allows bugs to be found and resolved more quickly, thanks to the fact that each commit is separate, we can quickly determine which commit the bug corresponds to. In addition, continuous integration can reduce the number of bugs and increase productivity because it frees developers from manual tasks. Finally, the frequency of continuous integration allows us to deliver updates to application users faster and more frequently.

## 3. How To Use `TravisCI`As Project’s CI Toolkit

In our case, we choose `TravisCI` as a CI toolkit. According to the official doc, the flow chart of `TravisCI` is shown below. In this following subsection, we will introduce the related configuration of our case.

![Fig 3-1 Flow Chart of TravisCI](SWE261P%20Fi%2041846/Untitled%2025.png)

Fig 3-1 Flow Chart of TravisCI

### 3.1 Create a Building Script File `.travis.yml`

In order to use `TravisCI`, firstly we need to create an account and choose the plan on the website. Our plan is shown as the image below.

![Fig 3-2 Our TravisCI Plan](SWE261P%20Fi%2041846/Untitled%2026.png)

Fig 3-2 Our TravisCI Plan

Secondly, we need to choose and sync a project on the dashboard. Here we choose the previous project, `Jackson-Core` to build CI.  After doing this, the dashboard is shown below.

![Fig 3-3 The Dashboard After Uploading](SWE261P%20Fi%2041846/Untitled%2027.png)

Fig 3-3 The Dashboard After Uploading

Thirdly, here comes the most exciting part. We will create and add a config file named `.travis.yml` to the root path of the project and it will tell `TravisCI` how to complete the CI pass by using the configuration from developers. Our configuration file is [here](https://github.com/Carlwasinfected/jackson-core/blob/2.14/.travis.yml). It indicates that `TravisCI` needs to build and test the project based on two different JDK versions. 

```yaml
language: java

jdk:
  - openjdk11
  - openjdk10
```

### 3.2 Commit and Verify

After creating the `.travis.yml` file and committing the change to GitHub, `TravisCI` will try to build and test the project as the given configuration shown above.

![Fig 3-4 Trigger Building Process](SWE261P%20Fi%2041846/Untitled%2028.png)

Fig 3-4 Trigger Building Process

During the process, we can look into the work log by simply clicking the selected building instance. For example, the image below shows that `build 7.1` is running test cases, and luckily, all the test cases have been passed so far.

![Fig 3-5 One Example of our working log](SWE261P%20Fi%2041846/Untitled%2029.png)

Fig 3-5 One Example of our working log

After running and passing the CI process, it will show that this build is successful and also, some details of this build, such as which JDK was used, which OS was supported, etc., which is shown as the image below. In our case, the CI process will take approximately four minutes. 

![Fig 3-6 Result of Build Passing](SWE261P%20Fi%2041846/Untitled%2030.png)

Fig 3-6 Result of Build Passing

## Reference

[1]. [https://semaphoreci.com/continuous-integration](https://semaphoreci.com/continuous-integration)

[2]. [https://en.wikipedia.org/wiki/Continuous_integration](https://en.wikipedia.org/wiki/Continuous_integration)

[3]. [https://aws.amazon.com/devops/continuous-integration](https://aws.amazon.com/devops/continuous-integration/)

---

# Part V: Testable Design

## 1.  Testable Design

### 1.1 Intro to Testable Design

The core principle about testable design is to make code quicker and easier to test, eg. simplifying the process of instantiating a class, or simulating various scenarios that may occur. In short words, testable designs make writing tests less burdensome.

### 1.2 Constraints of Testable Design

Below discusses some key points to maintain a testable architecture. 

- **Avoid logic in constructors**
    - Avoiding test-critical code when constructing an object is very important since a subclass constructor inevitably calls upon the super constructor. One solution to this would be applying a container framework such as Spring IOC.
- **Caution while using “new”**
    - Despite the fact that “new” is a common way to hardcode when testing, we need to think it through before doing so. If an object is something that might vary throughout tests, we should consider passing the object as a parameter instead of a ‘new-ing’ one.
- **Avoid “static”, “private”**
    - Static methods relate to the father class instead of a particular object. We shouldn’t make a method static unless we have analyzed thoroughly that it will be stubbed out of the test.
    - Private methods are very difficult to test. If we need to test a private method, we should refactor the code so the method is encapsulated in an inner object, providing access to which from a public method.
- **Beware of singleton**
    - The initial idea of singleton was to guarantee that a class only has one existing instance while providing an overall accessing point to it. This, however, has an obvious setback: when testing our only way to acquire an instance is through reflection, which is something we definitely don’t want to use.

### 1.3 Example of Testable Design

In the package `com/fasterxml/jackson/core/io`, we selected the class `BigDecimalParser` as example. Here, we find a private method `adjustScale()` . The link is [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/main/java/com/fasterxml/jackson/core/io/BigDecimalParser.java).

```java
private int adjustScale(int scale, long exp) {
        long adjScale = scale - exp;
        if (adjScale > Integer.MAX_VALUE || adjScale < Integer.MIN_VALUE) {
            throw new NumberFormatException(
                    "Scale out of range: " + adjScale + " while adjusting scale " + scale + " to exponent " + exp);
        }

        return (int) adjScale;
    }
```

As we discussed previously, private methods should be encapsulated with public access when tested. Therefore, we create an inner class called `AdjustScale` inside of `BigDecimalParser` 

```java
public class AdjustScale {
        private int adjustScale(int scale, long exp) {
            long adjScale = scale - exp;
            if (adjScale > Integer.MAX_VALUE || adjScale < Integer.MIN_VALUE) {
                throw new NumberFormatException(
                        "Scale out of range: " + adjScale + " while adjusting scale " + scale + " to exponent " + exp);
            }

            return (int) adjScale;
        }

        public int _adjustScale(int scale, long exp) {
            return this.adjustScale(scale, exp);
        }
    }
```

Our test file is located under `test/java/com.fasterxml.jackson/swe261p` . Here we test the general cases and then we perform two failing cases with overflow and underflow. The test file is available [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/test/java/com/fasterxml/jackson/swe261p/BigDecimalScalingTest.java).

```java
public class BigDecimalScalingTest {

    BigDecimalParser parser = new BigDecimalParser();
    BigDecimalParser.AdjustScale as = parser.new AdjustScale();
    long exp_min = Integer.MIN_VALUE, exp_max = Integer.MAX_VALUE;

    @Test
    public void testAdjustScale() {
        //general case
        assertEquals(Integer.MAX_VALUE, as._adjustScale(-1, exp_min));
        assertEquals(Integer.MIN_VALUE, as._adjustScale(-1, exp_max));
    }

    @Test(expected = NumberFormatException.class)
    public void testAdjustScaleUnderflow() {
        as._adjustScale(-2, exp_max);
    }

    @Test(expected = NumberFormatException.class)
    public void testAdjustScaleOverflow() {
        as._adjustScale(0, exp_min);
    }
}
```

## 2. Mocking

### 2.1 Intro to mocking

Mocking means creating a fake version of an external or internal service that can stand-in for the real one, helping your tests run more quickly and more reliably [[1]](https://circleci.com/blog/how-to-test-software-part-i-mocking-stubbing-and-contract-testing/).  The essentials of mocking are to define whether a tested method/object can perform correctly while interacting with others.

### 2.2 Why mocking?

- Mock testing significantly improves test efficiency, since we won’t need to rely on external services. Through mocking, we can reduce the testing time from seconds to milliseconds.
- Mocking prevents us from running duplicated tests while avoiding the fact that some data we query in runtime may differ from time.
- Mock testing doesn’t require third-party databases or APIs, ensuring that the test units can run independently.
- Mocking provides a jump start to developers since we can simulate objects, which enables developers to begin performing tests at relatively earlier periods.

### 2.3 Example of Mocking

Below is a snippet from `com/fasterxml/jackson/core/JsonFactory.java` . The source code is available [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/main/java/com/fasterxml/jackson/core/JsonFactory.java).

```java
@Override
    public JsonParser createParser(File f) throws IOException, JsonParseException {
        // true, since we create InputStream from File
        IOContext ctxt = _createContext(_createContentReference(f), true);
        InputStream in = new FileInputStream(f);
        return _createParser(_decorate(in, ctxt), ctxt);
    }

@Override
    public JsonParser createParser(String content) throws IOException, JsonParseException {
        final int strLen = content.length();
        // Actually, let's use this for medium-sized content, up to 64kB chunk (32kb char)
        if ((_inputDecorator != null) || (strLen > 0x8000) || !canUseCharArrays()) {
            // easier to just wrap in a Reader than extend InputDecorator; or, if content
            // is too long for us to copy it over
            return createParser(new StringReader(content));
        }
        IOContext ctxt = _createContext(_createContentReference(content), true);
        char[] buf = ctxt.allocTokenBuffer(strLen);
        content.getChars(0, strLen, buf, 0);
        return _createParser(buf, 0, strLen, ctxt, true);
    }
```

These two methods create a `JsonParser` object based on the argument passed in.  We will run two mock tests, the first one for testing whether a method was actually called, and the second is to simulate an exception occurring. The original test file is available [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/test/java/com/fasterxml/jackson/swe261p/MockitoTest.java).

```java
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
```

For the first test`mockitoTest`, we create a mock object file as well as a mock `JsonFactory`, and try to call `createParser` on both objects. By calling `verify` we can determine whether the two objects are collaborating as expected. The second test simulates an exception while creating a parser. Sometimes exceptions are difficult to simulate with actual objects, so we use `when` and `thenThrow` to simulate the process. In this way, we can perform tests or simulations on situations that are difficult to reproduce.

## Reference

[1] [https://circleci.com/blog/how-to-test-software-part-i-mocking-stubbing-and-contract-testing/](https://circleci.com/blog/how-to-test-software-part-i-mocking-stubbing-and-contract-testing/)

---

# Part VI: ****Static Analyzers****

## 1. Intro to Static Analysis

The concept static analysis, or static program analysis, is the process of analysis of software’s source code without executing the application. This is the opposite of dynamic analysis, which is performed when programs are executed. 

Static analysis is often used to detect various types of problems including [1]

- Vulnerabilities in security
- Performance issues
- Non-compliance with standards
- Usage of outdated programming constructs

We can see that static analysis is a human-based approach, usually performed by software developers and testers. Common types of static analysis testing include

- Software inspection
- Technical reviews
- Structured walkthroughs

## 2. Why Static Analysis

Below is a very simple code snippet:

```java
x = x / (x - y);
```

This operation may lead to a number divided by zero, which further leads to a runtime exception. Using static analysis, this problem can be easily avoided. 

There are several benefits of static analysis:

- Find bugs earlier
    - With static analysis, we can detect potential failures in a relatively early stage. We should not solely depend on dynamic testing, as it reveals issues at a very late stage, which would cost greater time and effort to fix.
- Low cost
    - Unlike dynamic analyzing, static analyzing is much more flexible and free from test cases or configurations. Nothing needs to be executed or validated.
- High efficiency
    - Static analysis can reveal the exact locations of bugs. Even if errors were determined by dynamic testing, testers still need to dig deeper into the code to diagnose which part is causing malfunction. On the other hand, improvement of code after static analysis leads to clean and quick dynamic tests.

## 3. Tools for Static Analyzing

The two static analyzing tools we selected are `CheckStyle` and `SpotBugs`. We inspect all `*.java` source code within the scope of `src/main/java/com/fasterxml/jackson/core`.

### 3.1 CheckStyle

To use CheckStyle, we first install the plugin in the setting window, as the image shows below.

![Fig 3-1 Plugin window](SWE261P%20Fi%2041846/Untitled%2031.png)

Fig 3-1 Plugin window

After that, we edit configurations in the preference window. There are two default configuration files to use, Sun Checks and Google Checks, like the image shown below.

![Fig 3-2 Configuration window](SWE261P%20Fi%2041846/Untitled%2032.png)

Fig 3-2 Configuration window

The image below shows the result when using Sun Checks.

![Fig 3-4 Result of using Sun Checks](SWE261P%20Fi%2041846/Untitled%2033.png)

Fig 3-4 Result of using Sun Checks

The image below shows the result when using Sun Checks.

![Fig 3-5 Result of using Google Checks](SWE261P%20Fi%2041846/Untitled%2034.png)

Fig 3-5 Result of using Google Checks

***Analysis***

After scanning the project and inspecting the code, there are some classic and frequent errors.

The most frequent one is `private method decleared final`. This `final` is redundant here since a `private` method is not visible outside and cannot be overridden itself. One of the examples in our result is below.

```java
private final static void _close(Closeable c) {  // `final` here is redundant
        try {
            c.close();
        } catch (IOException e) { }
    }
```

The second one I found is `redundant type cast`.  One of the examples is below, indicating that there is actually no need to convert `char` to `int`.

```java
public int decodeBase64Char(char c)
    {
        int ch = (int) c; // type cast here is redundant
        return (ch <= 127) ? _asciiToBase64[ch] : BASE64_VALUE_INVALID;
    }
```

The third one I would like to mention is `unnecessary interface modifier`. `Checkstyle` will report any redundant modifiers on interfaces and interface components and provide advice to developers to remove them. One of the code examples is below.

```java
...
    /**
     * Id used to represent {@link JsonToken#START_OBJECT}
     */
    public final static int ID_START_OBJECT = 1; // redundant modifiers

    /**
     * Id used to represent {@link JsonToken#END_OBJECT}
     */
    public final static int ID_END_OBJECT = 2; // redundant modifiers

...
```

### 3.2 SpotBugs

`IntelliJ SpotBugs` plugin provides static byte code analysis to look for bugs in Java code from within `IntelliJ IDEA`.  

![Fig 3-6 Plugin window](SWE261P%20Fi%2041846/Untitled%2035.png)

Fig 3-6 Plugin window

![Fig 3-6 Configuration window](SWE261P%20Fi%2041846/Untitled%2036.png)

Fig 3-6 Configuration window

![Fig 3-7 Result window](SWE261P%20Fi%2041846/Untitled%2037.png)

Fig 3-7 Result window

**Analysis**

The first error in **correctness** is `Value of ? from previous case is overwritten here due to switch statement fall through`.  We can see that this part of the code tries to use a switch function of `i` to determine the value of t. However, in the case ‘.’ t is given the value but not break, so if i is ‘9’, then t will be overwritten. The source code file is [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/main/java/com/fasterxml/jackson/core/json/UTF8DataInputJsonParser.java).

```java
JsonToken t;
        switch (i) {
        case '-':
            t = _parseNegNumber();
            break;
        case '.': // as per [core#611]
            t = _parseFloatThatStartsWithPeriod();
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            t = _parsePosNumber(i);
            break;
        case 'f':
            _matchToken("false", 1);
             t = JsonToken.VALUE_FALSE;
            break;
        case 'n':
            _matchToken("null", 1);
            t = JsonToken.VALUE_NULL;
            break;
        case 't':
            _matchToken("true", 1);
            t = JsonToken.VALUE_TRUE;
            break;
        case '[':
            t = JsonToken.START_ARRAY;
            break;
        case '{':
            t = JsonToken.START_OBJECT;
            break;

        default:
            t = _handleUnexpectedValue(i);
        }
```

![Fig 3-8 First error](SWE261P%20Fi%2041846/Untitled%2038.png)

Fig 3-8 First error

The second error in Dodgy code is `Class doesn't override equals in superclass`. Because the class is extended from `ConcurrentHashMap` and programmers add some fields to it. So the superclass’s `equals` function should be rewritten. But as long as we don’t use `equals`, this practice is also acceptable. The source code file is [here](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/main/java/com/fasterxml/jackson/core/util/InternCache.java).

```java
public final class InternCache
    extends ConcurrentHashMap<String,String> // since 2.3
{
    private static final long serialVersionUID = 1L;

    /**
     * Size to use is somewhat arbitrary, so let's choose something that's
     * neither too small (low hit ratio) nor too large (waste of memory).
     *<p>
     * One consideration is possible attack via colliding {@link String#hashCode};
     * because of this, limit to reasonably low setting.
     */
    private final static int MAX_ENTRIES = 180;

    public final static InternCache instance = new InternCache();

    /**
     * As minor optimization let's try to avoid "flush storms",
     * cases where multiple threads might try to concurrently
     * flush the map.
     */
    private final Object lock = new Object();
}
```

![Fig 3-9 Second error](SWE261P%20Fi%2041846/Untitled%2039.png)

Fig 3-9 Second error

### 3.3 Contrast

The information provided by the two tools is similar in some ways, for example, both can be grouped by class.

For `CheckStyle`, Every error message is put under its original file name. And for those errors identified by `CheckStyle` are more likely to be the coding style error instead of bugs.

![Fig 3-10 Style of `CheckStyles`](SWE261P%20Fi%2041846/Untitled%2040.png)

Fig 3-10 Style of `CheckStyles`

For `SpotBugs`, it is more neat and clear with description and code. Every error can be categorized into `Bug Category` or `Class` or `Package` or `Bug Rank`. You can use the button below to change what kind of Group you want. For these four categories, we think `SpotBugs` is more about humans, engineering, and problem-solving.

![Fig 3-11 `SpotBugs` Result I](SWE261P%20Fi%2041846/Untitled%2041.png)

Fig 3-11 `SpotBugs` Result I

![Fig 3-12 `SpotBugs` Result II](SWE261P%20Fi%2041846/Untitled%2042.png)

Fig 3-12 `SpotBugs` Result II

![Fig 3-13 `SpotBugs` Result III](SWE261P%20Fi%2041846/Untitled%2043.png)

Fig 3-13 `SpotBugs` Result III

![Fig 3-14 `SpotBugs` Result IV](SWE261P%20Fi%2041846/Untitled%2044.png)

Fig 3-14 `SpotBugs` Result IV

We can see that `CheckStyle` found 23, 373 items, and `SpotBugs` only found 169 items. The number of bugs they found varied widely. One thing we found in `CheckStyle` is that it gives us the power to write our own checks, which might be useful when we want to implement ourselves’s check styles.

### 3.4 Identify Same File with Two Tools

We used two tools in the same file [./com/fasterxml/jackson/core/util/InternCache.java](https://github.com/Carlwasinfected/SWE261P_Jackson/blob/2.14/src/main/java/com/fasterxml/jackson/core/util/InternCache.java)

These pictures below are the results of `CheckStyle` and `SpotBugs` in order.

![Fig 3-14 The result when using `CheckStyle`](SWE261P%20Fi%2041846/Untitled%2045.png)

Fig 3-14 The result when using `CheckStyle`

![Fig 3-15 The result when using `SpotBugs`](SWE261P%20Fi%2041846/Untitled%2046.png)

Fig 3-15 The result when using `SpotBugs`

We can see that for the same file they spot completely different bugs. These two tools might be fundamentally different in finding bugs, also the purpose of finding bugs.

## Reference

---

[1] [https://www.securecodewarrior.com/blog/what-is-static-analysis#:~:text=Static Analysis is the automated,Performance issues](https://www.securecodewarrior.com/blog/what-is-static-analysis#:~:text=Static%20Analysis%20is%20the%20automated,Performance%20issues).

[2]. [https://spotbugs.readthedocs.io/en/stable/](https://spotbugs.readthedocs.io/en/stable/)

[3]. [https://github.com/JetBrains/spotbugs-intellij-plugin](https://github.com/JetBrains/spotbugs-intellij-plugin)
