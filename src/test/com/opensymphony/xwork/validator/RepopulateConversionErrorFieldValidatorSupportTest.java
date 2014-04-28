/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.validator;

import java.util.Map;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.mock.MockActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.validator.validators.RepopulateConversionErrorFieldValidatorSupport;
import com.opensymphony.xwork.validator.DelegatingValidatorContext;

import junit.framework.TestCase;

/**
 * Test RepopulateConversionErrorFieldValidatorSupport.
 * 
 * @author tm_jee
 * @version $Date: 2006-09-09 02:02:53 +0900 (土, 09 9 2006) $ $Id: RepopulateConversionErrorFieldValidatorSupportTest.java 1117 2006-09-08 17:02:53Z tm_jee $
 */
public class RepopulateConversionErrorFieldValidatorSupportTest extends TestCase {

	
	InternalRepopulateConversionErrorFieldValidatorSupport validator1;
	InternalRepopulateConversionErrorFieldValidatorSupport validator2;
	ActionSupport action;
	
	public void testUseFullFieldName() throws Exception {
		validator2.setRepopulateField("true");
		validator2.validate(action);
		
		ActionContext.getContext().getActionInvocation().invoke();
		Object valueFromStack1 = ActionContext.getContext().getValueStack().findValue("someFieldName", String.class);
		Object valueFromStack2 = ActionContext.getContext().getValueStack().findValue("xxxsomeFieldName", String.class);
		
		assertNull(valueFromStack1);
		assertEquals(valueFromStack2, "some value");
	}
	
	public void testGetterSetterGetsCalledApropriately1() throws Exception {
		
		validator1.setRepopulateField("true");
		validator1.validate(action);

		
		ActionContext.getContext().getActionInvocation().invoke();
		
		Object valueFromStack = ActionContext.getContext().getValueStack().findValue("someFieldName", String.class);
		
		assertEquals(valueFromStack, "some value");
	}
	
	
	public void testGetterSetterGetsCalledApropriately2() throws Exception {
		
		validator1.setRepopulateField("false");
		validator1.validate(action);

		
		ActionContext.getContext().getActionInvocation().invoke();
		
		Object valueFromStack = ActionContext.getContext().getValueStack().findValue("someFieldName", String.class);
		
		assertEquals(valueFromStack, null);
	}
	
	
	protected void setUp() throws Exception {
		OgnlValueStack stack = new OgnlValueStack();
		MockActionInvocation invocation = new MockActionInvocation();
		invocation.setStack(stack);
		ActionContext.getContext().setValueStack(stack);
		ActionContext.getContext().setActionInvocation(invocation);
		
		String[] conversionErrorValue = new String[] { "some value" };
		Map conversionErrors = ActionContext.getContext().getConversionErrors();
		conversionErrors.put("someFieldName", conversionErrorValue);
		
		action = new ActionSupport();
		validator1 = 
			new InternalRepopulateConversionErrorFieldValidatorSupport();
		validator1.setFieldName("someFieldName");
		validator1.setValidatorContext(new DelegatingValidatorContext(action));
		conversionErrors.put("xxxsomeFieldName", conversionErrorValue);
		
		validator2 = 
			new InternalRepopulateConversionErrorFieldValidatorSupport();
		validator2.setFieldName("someFieldName");
		validator2.setValidatorContext(new DelegatingValidatorContext(action) {
			public String getFullFieldName(String fieldName) {
				return "xxx"+fieldName;
			}
		});
	}
	
	protected void tearDown() throws Exception {
		validator1 = null;
		action = null;
	}
	
	
	// === inner class ============
	
	class InternalRepopulateConversionErrorFieldValidatorSupport extends RepopulateConversionErrorFieldValidatorSupport {
		public boolean doValidateGetsCalled = false;
		
		protected void doValidate(Object object) throws ValidationException {
			doValidateGetsCalled = true;
		}
	}
}
