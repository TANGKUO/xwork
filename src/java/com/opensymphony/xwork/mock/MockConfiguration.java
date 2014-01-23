/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.mock;

import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.config.RuntimeConfiguration;
import com.opensymphony.xwork.config.entities.PackageConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Mock for an {@link Configuration}.
 *
 * @author Mike
 * @author tmjee
 * @version $Date: 2007-11-25 23:06:04 +0900 (日, 25 11 2007) $ $Id: MockConfiguration.java 1682 2007-11-25 14:06:04Z tm_jee $
 */
public class MockConfiguration implements Configuration {

    private Map packages = new LinkedHashMap();
    private Map parameters = new LinkedHashMap();

    public Map getParameters() {
        return parameters;
    }

    public String getParameter(String name) {
        return (String) parameters.get(name);
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    public PackageConfig getPackageConfig(String name) {
        return (PackageConfig) packages.get(name);
    }

    public Set getPackageConfigNames() {
        return packages.keySet();
    }

    public Map getPackageConfigs() {
        return packages;
    }

    public RuntimeConfiguration getRuntimeConfiguration() {
        throw new UnsupportedOperationException();
    }

    public void addPackageConfig(String name, PackageConfig packageContext) {
        packages.put(name, packageContext);
    }

    public void buildRuntimeConfiguration() {
        throw new UnsupportedOperationException();
    }

    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public void rebuildRuntimeConfiguration() {
        throw new UnsupportedOperationException();
    }

    public void reload() throws ConfigurationException {
        throw new UnsupportedOperationException();
    }

    public void removePackageConfig(String name) {
    }
}
