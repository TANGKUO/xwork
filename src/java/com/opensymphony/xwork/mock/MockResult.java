/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.mock;

import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.ActionInvocation;

/**
 * Mock for a {@link Result}.
 *
 * @author Mike
 * @author Rainer Hermanns
 * @version $Date: 2007-03-25 19:59:53 +0900 (日, 25 3 2007) $ $Id: MockResult.java 1392 2007-03-25 10:59:53Z rainerh $
 */
public class MockResult implements Result {

	private static final long serialVersionUID = 5371782753949314951L;
    public static final Object DEFAULT_PARAM = "location";

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MockResult)) {
            return false;
        }

        return true;
    }

    public void execute(ActionInvocation invocation) throws Exception {
        System.out.println("MockResult.execute");
    }

    public int hashCode() {
        return 10;
    }
}
