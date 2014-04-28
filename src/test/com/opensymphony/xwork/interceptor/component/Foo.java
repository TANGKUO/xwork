/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.interceptor.component;


/**
 * @author $Author: plightbo $
 * @version $Revision: 558 $
 */
public class Foo implements BarAware {

    Bar bar;


    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Bar getBar() {
        return bar;
    }
}
