/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork;

import java.util.HashMap;


/**
 * Simple Test ActionContext's ThreadLocal
 * 
 * @author tm_jee
 * @version $Date: 2007-02-09 08:36:05 +0900 (金, 09 2 2007) $ $Id: ActionContextThreadLocalTest.java 1331 2007-02-08 23:36:05Z tm_jee $
 */
public class ActionContextThreadLocalTest extends XWorkTestCase {

	
	public void testGetContext() throws Exception {
		assertNotNull(ActionContext.getContext());
	}
	
	public void testSetContext() throws Exception {
		ActionContext context = new ActionContext(new HashMap());
		ActionContext.setContext(context);
		assertEquals(context, ActionContext.getContext());
	}
}
