/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.util;

import com.opensymphony.xwork.XWorkTestCase;

import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit test of {@link TextParseUtil}.
 *
 * @author plightbo
 * @author tm_jee
 * 
 * @version $Date: 2007-07-16 23:46:59 +0900 (月, 16 7 2007) $ $Id: TextParseUtilTest.java 1545 2007-07-16 14:46:59Z mrdon $
 */
public class TextParseUtilTest extends XWorkTestCase {
	
	
	public void testTranslateVariablesWithEvaluator() throws Exception {
		OgnlValueStack stack = new OgnlValueStack();
		stack.push(new Object() {
			public String getMyVariable() {
				return "My Variable ";
			}
		});
		
		TextParseUtil.ParsedValueEvaluator evaluator = new TextParseUtil.ParsedValueEvaluator() {
			public Object evaluate(Object parsedValue) {
				return parsedValue.toString()+"Something";
			}
		};
		
		String result = TextParseUtil.translateVariables("Hello ${myVariable}", stack, evaluator);
		
		assertEquals(result, "Hello My Variable Something");
	}

    public void testTranslateVariables() {
        OgnlValueStack stack = new OgnlValueStack();

        Object s = TextParseUtil.translateVariables("foo: ${{1, 2, 3}}, bar: ${1}", stack);
        assertEquals("foo: [1, 2, 3], bar: 1", s);

        s = TextParseUtil.translateVariables("foo: ${#{1 : 2, 3 : 4}}, bar: ${1}", stack);
        assertEquals("foo: {1=2, 3=4}, bar: 1", s);

        s = TextParseUtil.translateVariables("foo: 1}", stack);
        assertEquals("foo: 1}", s);

        s = TextParseUtil.translateVariables("foo: {1}", stack);
        assertEquals("foo: {1}", s);

        s = TextParseUtil.translateVariables("foo: ${1", stack);
        assertEquals("foo: ${1", s);

        s =  TextParseUtil.translateVariables('$', "${{1, 2, 3}}", stack, Object.class);
        assertNotNull(s);
        assertTrue("List not returned when parsing a 'pure' list", s instanceof List);
        assertEquals(((List)s).size(), 3);
        
        s = TextParseUtil.translateVariables('$', "${#{'key1':'value1','key2':'value2','key3':'value3'}}", stack, Object.class);
        assertNotNull(s);
        assertTrue("Map not returned when parsing a 'pure' map", s instanceof Map);
        assertEquals(((Map)s).size(), 3);

        s =  TextParseUtil.translateVariables('$', "${1} two ${3}", stack, Object.class);
        assertEquals("1 two 3", s);

        s = TextParseUtil.translateVariables('$', "count must be between ${123} and ${456}, current value is ${98765}.", stack, Object.class);
        assertEquals("count must be between 123 and 456, current value is 98765.", s);
    }

    public void testCommaDelimitedStringToSet() {
        assertEquals(0, TextParseUtil.commaDelimitedStringToSet("").size());
        assertEquals(new HashSet(Arrays.asList(new String[] { "foo", "bar", "tee" })),
                TextParseUtil.commaDelimitedStringToSet(" foo, bar,tee"));
    }

    public void testTranslateVariablesOpenChar() {
        // just a quick test to see if the open char works
        // most test are done the methods above
        OgnlValueStack stack = new OgnlValueStack();

        Object s = TextParseUtil.translateVariables('$', "foo: ${{1, 2, 3}}, bar: ${1}", stack);
        assertEquals("foo: [1, 2, 3], bar: 1", s);

        Object s2 = TextParseUtil.translateVariables('#', "foo: #{{1, 2, 3}}, bar: #{1}", stack);
        assertEquals("foo: [1, 2, 3], bar: 1", s2);
    }

    public void testTranslateNoVariables() {
        OgnlValueStack stack = new OgnlValueStack();

        Object s = TextParseUtil.translateVariables('$', "foo: ${}", stack);
        assertEquals("foo: ", s);
    }
    
    public void testTranslateVariablesNoRecursive() {
        OgnlValueStack stack = new OgnlValueStack();
        stack.push(new HashMap() {{ put("foo", "${1+1}"); }});

        Object s = TextParseUtil.translateVariables('$', "foo: ${foo}", stack, String.class, null, 1);
        assertEquals("foo: ${1+1}", s);
    }
    
    public void testTranslateVariablesRecursive() {
        OgnlValueStack stack = new OgnlValueStack();
        stack.push(new HashMap() {{ put("foo", "${1+1}"); }});

        Object s = TextParseUtil.translateVariables('$', "foo: ${foo}", stack, String.class, null, 2);
        assertEquals("foo: 2", s);
    }

}
