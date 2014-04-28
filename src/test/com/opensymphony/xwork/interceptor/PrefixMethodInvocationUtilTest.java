/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor;

import java.lang.reflect.Method;

import org.easymock.MockControl;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;

import junit.framework.TestCase;

/**
 * Test case for PrefixMethodInovcationUtil.
 * 
 * @author tm_jee
 * @version $Date: 2006-07-04 02:11:57 +0900 (火, 04 7 2006) $ $Id: PrefixMethodInvocationUtilTest.java 1054 2006-07-03 17:11:57Z tmjee $
 */
public class PrefixMethodInvocationUtilTest extends TestCase {

	// === capitalizeMethodName ===
	public void testCapitalizeMethodName() throws Exception {
		assertEquals("SomeMethod", 
				PrefixMethodInvocationUtil.capitalizeMethodName("someMethod"));
		assertEquals("AnotherMethod", 
				PrefixMethodInvocationUtil.capitalizeMethodName("anotherMethod"));
	}
	
	// === getPrefixMethod ===
	public void testGetPrefixMethod1() throws Exception {
		Object action = new PrefixMethodInvocationUtilTest.Action1();
		Method m = PrefixMethodInvocationUtil.getPrefixedMethod(
				new String[] { "prepare", "prepareDo" }, "save", action);
		assertNotNull(m);
		assertEquals(m.getName(), "prepareSave");
	}
	
	public void testGetPrefixMethod2() throws Exception {
		Object action = new PrefixMethodInvocationUtilTest.Action1();
		Method m = PrefixMethodInvocationUtil.getPrefixedMethod(
				new String[] { "prepare", "prepareDo" }, "submit", action);
		assertNotNull(m);
		assertEquals(m.getName(), "prepareSubmit");
	}
	
	public void testGetPrefixMethod3() throws Exception {
		Object action = new PrefixMethodInvocationUtilTest.Action1();
		Method m = PrefixMethodInvocationUtil.getPrefixedMethod(
				new String[] { "prepare", "prepareDo" }, "cancel", action);
		assertNotNull(m);
		assertEquals(m.getName(), "prepareDoCancel");
	}
	
	public void testGetPrefixMethod4() throws Exception {
		Object action = new PrefixMethodInvocationUtilTest.Action1();
		Method m = PrefixMethodInvocationUtil.getPrefixedMethod(
				new String[] { "prepare", "prepareDo" }, "noSuchMethod", action);
		assertNull(m);
	}
	
	public void testGetPrefixMethod5() throws Exception {
		Object action = new PrefixMethodInvocationUtilTest.Action1();
		Method m = PrefixMethodInvocationUtil.getPrefixedMethod(
				new String[] { "noSuchPrefix", "noSuchPrefixDo" }, "save", action);
		assertNull(m);
	}
	
	
	// === invokePrefixMethod === 
	public void testInvokePrefixMethod1() throws Exception {
		PrefixMethodInvocationUtilTest.Action1 action = new PrefixMethodInvocationUtilTest.Action1();
		
		// ActionProxy
		MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
		ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();		
		mockActionProxy.getMethod();
		controlActionProxy.setReturnValue("save");
		
		
		// ActionInvocation
		MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
		ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
		mockActionInvocation.getAction();
		controlActionInvocation.setReturnValue(action);
		mockActionInvocation.getProxy();
		controlActionInvocation.setReturnValue(mockActionProxy);
		
		controlActionProxy.replay();
		controlActionInvocation.replay();
		
		
		PrefixMethodInvocationUtil.invokePrefixMethod(
				mockActionInvocation, 
				new String[] { "prepare", "prepareDo" });
		
		controlActionProxy.verify();
		controlActionInvocation.verify();
		
		assertTrue(action.prepareSaveInvoked);
		assertFalse(action.prepareDoSaveInvoked);
		assertFalse(action.prepareSubmitInvoked);
		assertFalse(action.prepareDoCancelInvoked);
	}
	
	public void testInvokePrefixMethod2() throws Exception {
		PrefixMethodInvocationUtilTest.Action1 action = new PrefixMethodInvocationUtilTest.Action1();
		
		// ActionProxy
		MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
		ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();		
		mockActionProxy.getMethod();
		controlActionProxy.setReturnValue("submit");
		
		
		// ActionInvocation
		MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
		ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
		mockActionInvocation.getAction();
		controlActionInvocation.setReturnValue(action);
		mockActionInvocation.getProxy();
		controlActionInvocation.setReturnValue(mockActionProxy);
		
		controlActionProxy.replay();
		controlActionInvocation.replay();
		
		
		PrefixMethodInvocationUtil.invokePrefixMethod(
				mockActionInvocation, 
				new String[] { "prepare", "prepareDo" });
		
		controlActionProxy.verify();
		controlActionInvocation.verify();
		
		assertFalse(action.prepareSaveInvoked);
		assertFalse(action.prepareDoSaveInvoked);
		assertTrue(action.prepareSubmitInvoked);
		assertFalse(action.prepareDoCancelInvoked);
	}
	
	public void testInvokePrefixMethod3() throws Exception {
		PrefixMethodInvocationUtilTest.Action1 action = new PrefixMethodInvocationUtilTest.Action1();
		
		// ActionProxy
		MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
		ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();		
		mockActionProxy.getMethod();
		controlActionProxy.setReturnValue("cancel");
		
		
		// ActionInvocation
		MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
		ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
		mockActionInvocation.getAction();
		controlActionInvocation.setReturnValue(action);
		mockActionInvocation.getProxy();
		controlActionInvocation.setReturnValue(mockActionProxy);
		
		controlActionProxy.replay();
		controlActionInvocation.replay();
		
		
		PrefixMethodInvocationUtil.invokePrefixMethod(
				mockActionInvocation, 
				new String[] { "prepare", "prepareDo" });
		
		controlActionProxy.verify();
		controlActionInvocation.verify();
		
		assertFalse(action.prepareSaveInvoked);
		assertFalse(action.prepareDoSaveInvoked);
		assertFalse(action.prepareSubmitInvoked);
		assertTrue(action.prepareDoCancelInvoked);
	}
		
	public void testInvokePrefixMethod4() throws Exception {
		PrefixMethodInvocationUtilTest.Action1 action = new PrefixMethodInvocationUtilTest.Action1();
		
		// ActionProxy
		MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
		ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();		
		mockActionProxy.getMethod();
		controlActionProxy.setReturnValue("noSuchMethod");
		
		
		// ActionInvocation
		MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
		ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
		mockActionInvocation.getAction();
		controlActionInvocation.setReturnValue(action);
		mockActionInvocation.getProxy();
		controlActionInvocation.setReturnValue(mockActionProxy);
		
		controlActionProxy.replay();
		controlActionInvocation.replay();
		
		
		PrefixMethodInvocationUtil.invokePrefixMethod(
				mockActionInvocation, 
				new String[] { "prepare", "prepareDo" });
		
		controlActionProxy.verify();
		controlActionInvocation.verify();
		
		assertFalse(action.prepareSaveInvoked);
		assertFalse(action.prepareDoSaveInvoked);
		assertFalse(action.prepareSubmitInvoked);
		assertFalse(action.prepareDoCancelInvoked);
	}
		
	public void testInvokePrefixMethod5() throws Exception {
		PrefixMethodInvocationUtilTest.Action1 action = new PrefixMethodInvocationUtilTest.Action1();
		
		// ActionProxy
		MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
		ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();		
		mockActionProxy.getMethod();
		controlActionProxy.setReturnValue("save");
		
		
		// ActionInvocation
		MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
		ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
		mockActionInvocation.getAction();
		controlActionInvocation.setReturnValue(action);
		mockActionInvocation.getProxy();
		controlActionInvocation.setReturnValue(mockActionProxy);
		
		controlActionProxy.replay();
		controlActionInvocation.replay();
		
		
		PrefixMethodInvocationUtil.invokePrefixMethod(
				mockActionInvocation, 
				new String[] { "noSuchPrefix", "noSuchPrefixDo" });
		
		controlActionProxy.verify();
		controlActionInvocation.verify();
		
		assertFalse(action.prepareSaveInvoked);
		assertFalse(action.prepareDoSaveInvoked);
		assertFalse(action.prepareSubmitInvoked);
		assertFalse(action.prepareDoCancelInvoked);
	}
	
	
	
	
	/**
	 * Just a simple object for testing method invocation on its methods.
	 * 
	 * @author tm_jee
	 * @version $Date: 2006-07-04 02:11:57 +0900 (火, 04 7 2006) $ $Id: PrefixMethodInvocationUtilTest.java 1054 2006-07-03 17:11:57Z tmjee $
	 */
	public static class Action1 {
		
		boolean prepareSaveInvoked = false;
		boolean prepareDoSaveInvoked = false;
		boolean prepareSubmitInvoked = false;
		boolean prepareDoCancelInvoked = false;
		
		
		// save
		public void prepareSave() {
			prepareSaveInvoked = true;
		}
		public void prepareDoSave() {
			prepareDoSaveInvoked = true;
		}
		
		// submit
		public void prepareSubmit() {
			prepareSubmitInvoked = true;
		}
		
		// cancel
		public void prepareDoCancel() {
			prepareDoCancelInvoked = true;
		}
	}
}
