/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor;

import com.opensymphony.xwork.*;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.providers.MockConfigurationProvider;
import com.opensymphony.xwork.mock.MockActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;

import java.util.HashMap;
import java.util.Map;


/**
 * Unit test for {@link ParametersInterceptor}.
 *
 * @author Jason Carreira
 */
public class ParametersInterceptorTest extends XWorkTestCase {

    public void testParameterNameAware() {
        ParametersInterceptor pi = new ParametersInterceptor();
        final Map actual = new HashMap();
        OgnlValueStack stack = new OgnlValueStack() {
            public void setValue(String expr, Object value) {
                actual.put(expr, value);
            }
        };
        final Map expected = new HashMap() {
            {
                put("fooKey", "fooValue");
                put("barKey", "barValue");
            }
        };
        Object a = new ParameterNameAware() {
            public boolean acceptableParameterName(String parameterName) {
                return expected.containsKey(parameterName);
            }
        };
        Map parameters = new HashMap() {
            {
                put("fooKey", "fooValue");
                put("barKey", "barValue");
                put("error", "error");
            }
        };
        pi.setParameters(a, stack, parameters);
        assertEquals(expected, actual);
    }

    public void testDoesNotAllowMethodInvocations() throws Exception {
        Map params = new HashMap();
        params.put("@java.lang.System@exit(1).dummy", "dumb value");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.MODEL_DRIVEN_PARAM_TEST, extraContext);
        assertEquals(Action.SUCCESS, proxy.execute());

        ModelDrivenAction action = (ModelDrivenAction) proxy.getAction();
        TestBean model = (TestBean) action.getModel();

        String property = System.getProperty("webwork.security.test");
        assertNull(property);
    }

    public void testModelDrivenParameters() throws Exception {
        Map params = new HashMap();
        final String fooVal = "com.opensymphony.xwork.interceptor.ParametersInterceptorTest.foo";
        params.put("foo", fooVal);

        final String nameVal = "com.opensymphony.xwork.interceptor.ParametersInterceptorTest.name";
        params.put("name", nameVal);
        params.put("count", "15");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.MODEL_DRIVEN_PARAM_TEST, extraContext);
        assertEquals(Action.SUCCESS, proxy.execute());

        ModelDrivenAction action = (ModelDrivenAction) proxy.getAction();
        TestBean model = (TestBean) action.getModel();
        assertEquals(nameVal, model.getName());
        assertEquals(15, model.getCount());
        assertEquals(fooVal, action.getFoo());
    }

    public void testParametersDoesNotAffectSession() throws Exception {
        Map params = new HashMap();
        params.put("blah", "This is blah");
        params.put("#session.foo", "Foo");
        params.put("\u0023session[\'user\']", "0wn3d");
        params.put("\u0023session.user2", "0wn3d");
        params.put("('\u0023'%20%2b%20'session[\'user3\']')(unused)", "0wn3d");
        params.put("('\\u0023' + 'session[\\'user4\\']')(unused)", "0wn3d");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.PARAM_INTERCEPTOR_ACTION_NAME, extraContext);
        OgnlValueStack stack = proxy.getInvocation().getStack();
        HashMap session = new HashMap();
        stack.getContext().put("session", session);
        proxy.execute();
        assertEquals("This is blah", ((SimpleAction) proxy.getAction()).getBlah());
        assertNull(session.get("foo"));
        assertNull(session.get("user"));
        assertNull(session.get("user2"));
        assertNull(session.get("user3"));
        assertNull(session.get("user4"));
    }

    public void testParameters() throws Exception {
        Map params = new HashMap();
        params.put("blah", "This is blah");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);
        extraContext.put(ActionContext.DEV_MODE, Boolean.FALSE);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.PARAM_INTERCEPTOR_ACTION_NAME, extraContext);
        proxy.execute();
        assertEquals("This is blah", ((SimpleAction) proxy.getAction()).getBlah());
    }

    public void testNonexistentParametersGetLoggedInDevMode() throws Exception {
        Map params = new HashMap();
        params.put("not_a_property", "There is no action property named like this");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);
        extraContext.put(ActionContext.DEV_MODE, Boolean.TRUE);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.PARAM_INTERCEPTOR_ACTION_NAME, extraContext);

        proxy.execute();
        
        final String actionMessage = ""+((SimpleAction) proxy.getAction()).getActionMessages().toArray()[0];
        assertTrue(actionMessage.indexOf("No object in the CompoundRoot has a publicly accessible property named 'not_a_property' (no setter could be found).") > -1);
    }

    public void testNonexistentParametersAreIgnoredInProductionMode() throws Exception {
        Map params = new HashMap();
        params.put("not_a_property", "There is no action property named like this");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);
        extraContext.put(ActionContext.DEV_MODE, Boolean.FALSE);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.PARAM_INTERCEPTOR_ACTION_NAME, extraContext);
        proxy.execute();
        assertTrue(((SimpleAction) proxy.getAction()).getActionMessages().isEmpty());
    }

    public void testNoParametersAction() throws Exception {
        ParametersInterceptor interceptor = new ParametersInterceptor();
        interceptor.init();

        MockActionInvocation mai = new MockActionInvocation();
        Action action = new NoParametersAction();
        mai.setAction(action);

        interceptor.intercept(mai);
        interceptor.destroy();
    }

    public void testArrayClassPollutionBlockedByPattern() throws Exception{

        final String valid = "validKey";
        final String pollution1 = "model.class.classLoader.jarPath";
        final String pollution2 = "model['class']['classLoader']['jarPath']";
        final String pollution3 = "model[\"class\"]['classLoader']['jarPath']";
        final String pollution4 = "class.classLoader.jarPath";
        final String pollution5 = "class['classLoader']['jarPath']";
        final String pollution6 = "class[\"classLoader\"]['jarPath']";

        Map params = new HashMap() {
            {
                put(valid, "validValue");
                put(pollution1,"bad");
                put(pollution2,"bad");
                put(pollution3,"bad");
                put(pollution4,"bad");
                put(pollution5,"bad");
                put(pollution6,"bad");
            }
        };

        final Map acceptable = new HashMap();

        ParametersInterceptor pi = new ParametersInterceptor() {

            protected boolean acceptableName(String name) {

                boolean result = super.acceptableName(name);
                acceptable.put(name, Boolean.valueOf(result));
                return result;
            }
        };

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);

        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.PARAM_INTERCEPTOR_ACTION_NAME, extraContext);
        OgnlValueStack stack = proxy.getInvocation().getStack();
        pi.setParameters(proxy,stack,params);


        assertTrue(((Boolean) acceptable.get(valid)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution1)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution2)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution3)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution4)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution5)).booleanValue());
        assertFalse(((Boolean) acceptable.get(pollution6)).booleanValue());

    }

    public void testExcludedPatternsGetInitialized() throws Exception {
        ParametersInterceptor parametersInterceptor = new ParametersInterceptor();
        assertEquals(ExcludedPatterns.EXCLUDED_PATTERNS.length, parametersInterceptor.excludeParams.size());
    }

    private class NoParametersAction implements Action, NoParameters {

        public String execute() throws Exception {
            return SUCCESS;
        }
    }

    protected void setUp() throws Exception {
    	super.setUp();
        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.addConfigurationProvider(new MockConfigurationProvider());
        ConfigurationManager.getConfiguration().reload();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        //  clear out configuration
        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.destroyConfiguration();
    }
}