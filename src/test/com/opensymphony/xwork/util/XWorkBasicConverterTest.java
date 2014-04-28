/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.util;

import java.util.Date;
import java.util.HashMap;

import com.opensymphony.xwork.XworkException;

import junit.framework.TestCase;

/**
 * Test case for XWorkBasicConverter
 * 
 * @author tm_jee
 * @version $Date: 2006-05-11 01:55:54 +0900 (木, 11 5 2006) $ $Id: XWorkBasicConverterTest.java 1010 2006-05-10 16:55:54Z tmjee $
 */
public class XWorkBasicConverterTest extends TestCase {

	public void test() {
		// TODO: test for every possible conversion
		// take into account of empty string
		// primitive -> conversion error when empty string is passed
		// object -> return null when empty string is passed
	}
	
	
	// TODO: more test will come soon !!!
	
	// TEST DATE CONVERSION:-
	public void testDateConversionWithEmptyValue() {
		XWorkBasicConverter basicConverter = new XWorkBasicConverter();
		Object convertedObject = basicConverter.convertValue(new HashMap(), null, null, null, "", Date.class);
		// we must not get XWorkException as that will caused a conversion error
		assertNull(convertedObject); 
	}
	
	public void testDateConversionWithInvalidValue() {
		XWorkBasicConverter basicConverter = new XWorkBasicConverter();
		try {
			Object convertedObject = basicConverter.convertValue(new HashMap(), null, null, null, "asdsd", Date.class);
		}
		catch(XworkException e) {
			// we MUST get this exception as this is a conversion error
			assertTrue(true);
			return;
		}
		fail("XWorkException expected - conversion error occurred");
	}
}
