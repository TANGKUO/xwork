/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork;

import com.opensymphony.xwork.config.ConfigurationManager;

import java.util.HashMap;


/**
 * @author $Author: tm_jee $
 * @version $Revision: 1331 $
 */
public class ActionInvocationTest extends XWorkTestCase {

    public void testCommandInvocation() throws Exception {
        ActionProxy baseActionProxy = ActionProxyFactory.getFactory().createActionProxy("baz", "commandTest", null);
        assertEquals("success", baseActionProxy.execute());

        ActionProxy commandActionProxy = ActionProxyFactory.getFactory().createActionProxy("baz", "myCommand", null);
        assertEquals(SimpleAction.COMMAND_RETURN_CODE, commandActionProxy.execute());
    }

    public void testSimple() {
        HashMap params = new HashMap();
        params.put("blah", "this is blah");

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, params);

        try {
            ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", "Foo", extraContext);
            proxy.execute();
            assertEquals("this is blah", proxy.getInvocation().getStack().findValue("[1].blah"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    protected void setUp() throws Exception {
        super.setUp();

        // ensure we're using the default configuration, not simple config
        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.getConfiguration().reload();
    }
}
