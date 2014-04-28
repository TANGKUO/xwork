/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor;

import com.mockobjects.dynamic.Mock;
import com.opensymphony.xwork.*;
import com.opensymphony.xwork.util.OgnlValueStack;
import junit.framework.TestCase;

import java.util.Date;
import java.util.HashMap;


/**
 * Unit test for {@link ChainingInterceptor}.
 *
 * @author Jason Carreira
 * @author tmjee
 * @version $Date: 2007-11-30 23:32:25 +0900 (金, 30 11 2007) $ $Id: ChainingInterceptorTest.java 1695 2007-11-30 14:32:25Z tm_jee $
 */
public class ChainingInterceptorTest extends TestCase {

    ActionInvocation invocation;
    ChainingInterceptor interceptor;
    Mock mockInvocation;
    OgnlValueStack stack;


    public void testActionErrorsCanBeAddedAfterChain() throws Exception {
        SimpleAction action1 = new SimpleAction();
        SimpleAction action2 = new SimpleAction();
        action1.addActionError("foo");
        mockInvocation.matchAndReturn("getAction", action2);
        stack.push(action1);
        stack.push(action2);
        interceptor.intercept(invocation);
        assertEquals(action1.getActionErrors(), action2.getActionErrors());
        action2.addActionError("bar");
        assertEquals(1, action1.getActionErrors().size());
        assertEquals(2, action2.getActionErrors().size());
        assertTrue(action2.getActionErrors().contains("bar"));
    }

    public void testPropertiesChained() throws Exception {
        TestBean bean = new TestBean();
        TestBeanAction action = new TestBeanAction();
        mockInvocation.matchAndReturn("getAction", action);
        bean.setBirth(new Date());
        bean.setName("foo");
        bean.setCount(1);
        stack.push(bean);
        stack.push(action);
        interceptor.intercept(invocation);
        assertEquals(bean.getBirth(), action.getBirth());
        assertEquals(bean.getName(), action.getName());
        assertEquals(bean.getCount(), action.getCount());
    }

    public void testExcludesPropertiesChained() throws Exception {
        TestBean bean = new TestBean();
        TestBeanAction action = new TestBeanAction();
        mockInvocation.matchAndReturn("getAction", action);
        bean.setBirth(new Date());
        bean.setName("foo");
        bean.setCount(1);
        stack.push(bean);
        stack.push(action);

        // WW-1475
        String excludes = "count";
        interceptor.setExcludes(excludes);
        interceptor.intercept(invocation);
        assertEquals(bean.getBirth(), action.getBirth());
        assertEquals(bean.getName(), action.getName());
        assertEquals(0, action.getCount());
        assertEquals(1, interceptor.getExcludes().size());
        assertTrue(interceptor.getExcludes().contains("count"));
        assertFalse(interceptor.getExcludes().contains("xxxx"));
    }

    public void testTwoExcludesPropertiesChained() throws Exception {
        TestBean bean = new TestBean();
        TestBeanAction action = new TestBeanAction();
        mockInvocation.matchAndReturn("getAction", action);
        bean.setBirth(new Date());
        bean.setName("foo");
        bean.setCount(1);
        stack.push(bean);
        stack.push(action);

        // WW-1475
        String excludes = "name,count";
        interceptor.setExcludes(excludes);
        interceptor.intercept(invocation);
        assertEquals(bean.getBirth(), action.getBirth());
        assertEquals(null, action.getName());
        assertEquals(0, action.getCount());
        assertEquals(2, interceptor.getExcludes().size());
        assertTrue(interceptor.getExcludes().contains("name"));
        assertTrue(interceptor.getExcludes().contains("count"));
        assertFalse(interceptor.getExcludes().contains("xxxx"));
    }

    public void testNullCompoundRootElementAllowsProcessToContinue() throws Exception {
        // we should not get NPE, but instead get a warning logged.
        stack.push(null);
        stack.push(null);
        stack.push(null);
        interceptor.intercept(invocation);
    }


    protected void setUp() throws Exception {
        super.setUp();
        stack = new OgnlValueStack();
        mockInvocation = new Mock(ActionInvocation.class);
        mockInvocation.expectAndReturn("getStack", stack);
        mockInvocation.expectAndReturn("invoke", Action.SUCCESS);
        mockInvocation.expectAndReturn("getInvocationContext", new ActionContext(new HashMap()));
        invocation = (ActionInvocation) mockInvocation.proxy();
        interceptor = new ChainingInterceptor();
    }


    private class TestBeanAction extends TestBean implements Action {
        public String execute() throws Exception {
            return SUCCESS;
        }
    }
}
