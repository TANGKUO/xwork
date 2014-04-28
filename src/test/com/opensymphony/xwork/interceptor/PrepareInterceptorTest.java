/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
*/
package com.opensymphony.xwork.interceptor;

import org.easymock.MockControl;

import com.mockobjects.dynamic.Mock;
import com.opensymphony.xwork.mock.MockActionInvocation;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.Preparable;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.SimpleFooAction;
import junit.framework.TestCase;

/**
 * Unit test for PrepareInterceptor.
 *
 * @author Claus Ibsen
 * @author tm_jee
 */
public class PrepareInterceptorTest extends TestCase {

    private Mock mock;
    private PrepareInterceptor interceptor;

    public void testPrepareCalled() throws Exception {
        MockActionInvocation mai = new MockActionInvocation();
        mai.setAction(mock.proxy());
        mock.expect("prepare");

        interceptor.before(mai);
        interceptor.after(mai, Action.SUCCESS); // to have higher code coverage
    }

    public void testNoPrepareCalled() throws Exception {
        MockActionInvocation mai = new MockActionInvocation();
        mai.setAction(new SimpleFooAction());

        interceptor.before(mai);
        interceptor.after(mai, Action.SUCCESS); // to have higher code coverage
    }
    
    public void testPrefixInvocation1() throws Exception {
    	
    	MockControl controlAction = MockControl.createControl(ActionInterface.class);
    	ActionInterface mockAction = (ActionInterface) controlAction.getMock();
    	mockAction.prepareSubmit();
    	controlAction.setVoidCallable(1);
    	mockAction.prepare();
    	controlAction.setVoidCallable(1);
    	
    	MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
    	ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();
    	mockActionProxy.getMethod();
    	controlActionProxy.setDefaultReturnValue("submit");
    	
    	
    	MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
    	ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
    	mockActionInvocation.getAction();
    	controlActionInvocation.setDefaultReturnValue(mockAction);
    	mockActionInvocation.invoke();
    	controlActionInvocation.setDefaultReturnValue("okok");
    	mockActionInvocation.getProxy();
    	controlActionInvocation.setDefaultReturnValue(mockActionProxy);
    	
    	
    	controlAction.replay();
    	controlActionProxy.replay();
    	controlActionInvocation.replay();
    	
    	PrepareInterceptor interceptor = new PrepareInterceptor();
    	String result = interceptor.intercept(mockActionInvocation);
    	
    	assertEquals("okok", result);
    	
    	controlAction.verify();
    	controlActionProxy.verify();
    	controlActionInvocation.verify();
    }
    
    public void testPrefixInvocation2() throws Exception {
    	
    	MockControl controlAction = MockControl.createControl(ActionInterface.class);
    	ActionInterface mockAction = (ActionInterface) controlAction.getMock();
    	mockAction.prepare();
    	controlAction.setVoidCallable(1);
    	
    	MockControl controlActionProxy = MockControl.createControl(ActionProxy.class);
    	ActionProxy mockActionProxy = (ActionProxy) controlActionProxy.getMock();
    	mockActionProxy.getMethod();
    	controlActionProxy.setDefaultReturnValue("save");
    	
    	
    	MockControl controlActionInvocation = MockControl.createControl(ActionInvocation.class);
    	ActionInvocation mockActionInvocation = (ActionInvocation) controlActionInvocation.getMock();
    	mockActionInvocation.getAction();
    	controlActionInvocation.setDefaultReturnValue(mockAction);
    	mockActionInvocation.invoke();
    	controlActionInvocation.setDefaultReturnValue("okok");
    	mockActionInvocation.getProxy();
    	controlActionInvocation.setDefaultReturnValue(mockActionProxy);
    	
    	
    	controlAction.replay();
    	controlActionProxy.replay();
    	controlActionInvocation.replay();
    	
    	PrepareInterceptor interceptor = new PrepareInterceptor();
    	String result = interceptor.intercept(mockActionInvocation);
    	
    	assertEquals("okok", result);
    	
    	controlAction.verify();
    	controlActionProxy.verify();
    	controlActionInvocation.verify();
    }
    

    protected void setUp() throws Exception {
        mock = new Mock(Preparable.class);
        interceptor = new PrepareInterceptor();
    }

    protected void tearDown() throws Exception {
        mock.verify();
    }

    
    /**
     * Simple interface to test prefix action invocation 
     * eg. prepareSubmit(), prepareSave() etc.
     * 
     * @author tm_jee
     * @version $Date: 2006-07-04 02:11:57 +0900 (火, 04 7 2006) $ $Id: PrepareInterceptorTest.java 1054 2006-07-03 17:11:57Z tmjee $
     */
    public interface ActionInterface extends Action, Preparable {
    	void prepareSubmit();
    }
}
