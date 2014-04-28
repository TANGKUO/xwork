/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.util;

import java.io.Serializable;


/**
 * @author <a href="mailto:plightbo@cisco.com">Pat Lightbody</a>
 * @author $Author: plightbo $
 * @version $Revision: 558 $
 */
public class Dog implements Serializable {

    public static final String SCIENTIFIC_NAME = "Canine";


    Cat hates;
    String name;
    int[] childAges;
    boolean male;
    int age;


    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setChildAges(int[] childAges) {
        this.childAges = childAges;
    }

    public int[] getChildAges() {
        return childAges;
    }

    public void setException(String blah) throws Exception {
        throw new Exception("This is expected");
    }

    public String getException() throws Exception {
        throw new Exception("This is expected");
    }

    public void setHates(Cat hates) {
        this.hates = hates;
    }

    public Cat getHates() {
        return hates;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isMale() {
        return male;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int computeDogYears() {
        return age * 7;
    }

    public int multiplyAge(int by) {
        return age * by;
    }

    /**
     * @return null
     */
    public Integer nullMethod() {
        return null;
    }

    /**
     * a method which is safe to call with a null argument
     *
     * @param arg the Boolean to return
     * @return arg, if it is not null, or Boolean.TRUE if arg is null
     */
    public Boolean nullSafeMethod(Boolean arg) {
        return (arg == null) ? Boolean.TRUE : arg;
    }
}
