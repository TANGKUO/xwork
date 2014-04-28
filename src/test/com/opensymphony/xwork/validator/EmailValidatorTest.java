/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.validator;

import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.validator.validators.EmailValidator;

import junit.framework.TestCase;

/**
 * Test case for Email Validator
 * 
 * 
 * @author tm_jee
 * @version $Date: 2006-04-01 21:45:14 +0900 (土, 01 4 2006) $ $Id: EmailValidatorTest.java 969 2006-04-01 12:45:14Z tmjee $
 */
public class EmailValidatorTest extends TestCase {
	
	public void testEmailValidity() throws Exception {
		assertTrue(verifyEmailValidity("tmjee@yahoo.com"));
		assertTrue(verifyEmailValidity("tm_jee@yahoo.co"));
		assertTrue(verifyEmailValidity("tm.jee@yahoo.co.uk"));
		assertTrue(verifyEmailValidity("tm.jee@yahoo.co.biz"));
		assertTrue(verifyEmailValidity("tm_jee@yahoo.com"));
		assertTrue(verifyEmailValidity("tm_jee@yahoo.net"));
		assertTrue(verifyEmailValidity(" user@subname1.subname2.subname3.domainname.co.uk "));
		
		assertFalse(verifyEmailValidity("tm_jee#marry@yahoo.co.uk"));
		assertFalse(verifyEmailValidity("tm_jee@ yahoo.co.uk"));
		assertFalse(verifyEmailValidity("tm_jee  @yahoo.co.uk"));
		assertFalse(verifyEmailValidity("tm_j ee  @yah oo.co.uk"));
		assertFalse(verifyEmailValidity("tm_jee  @yah oo.co.uk"));
		assertFalse(verifyEmailValidity("tm_jee @ yahoo.com"));
		assertFalse(verifyEmailValidity(" user@subname1.subname2.subname3.domainn#ame.co.uk "));
	}
	
	protected boolean verifyEmailValidity(final String email) throws Exception {
		ActionSupport action = new ActionSupport() {
			public String getMyEmail() {
				return email;
			}
		};
		
		EmailValidator validator = new EmailValidator();
		validator.setValidatorContext(new DelegatingValidatorContext(action));
		validator.setFieldName("myEmail");
		validator.setDefaultMessage("invalid email");
		validator.validate(action);
		
		return (action.getFieldErrors().size() == 0);
	}
}
