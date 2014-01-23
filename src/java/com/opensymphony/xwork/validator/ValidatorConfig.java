/*
 * Copyright (c) 2002-2007 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.validator;

import java.util.Map;
import com.opensymphony.xwork.util.location.Located;

/**
 * Holds the necessary information for configuring an instance of a Validator.
 * 
 * 
 * @author James House
 * @author Rainer Hermanns
 * @author tmjee
 * @version $Date: 2007-11-26 18:02:22 +0900 (月, 26 11 2007) $ $Id: ValidatorConfig.java 1687 2007-11-26 09:02:22Z tm_jee $
 */
public class ValidatorConfig extends Located {

    private String type;
    private Map params;
    private String defaultMessage;
    private String messageKey;
    private boolean shortCircuit;
    private String[] messageParams;
    
    public ValidatorConfig() {
    }
    
    /**
     * @param validatorType
     * @param params
     */
    public ValidatorConfig(String validatorType, Map params) {
        this.type = validatorType;
        this.params = params;
    }
    
    /**
     * @return Returns the defaultMessage for the validator.
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }
    
    /**
     * @param defaultMessage The defaultMessage to set on the validator.
     */
    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
    
    /**
     * @return Returns the messageKey for the validator.
     */
    public String getMessageKey() {
        return messageKey;
    }
    
    /**
     * @param messageKey The messageKey to set on the validator.
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }


    /**
     * @param messageParams The i18n message parameters/arguments to be used.
     */
    public void setMessageParams(String[] messageParams) {
        this.messageParams = messageParams;
    }

    /**
     * @return The i18n message parameters/arguments to be used.
     */
    public String[] getMessageParams() {
        return messageParams;
    }
    
    /**
     * @return Returns wether the shortCircuit flag should be set on the 
     * validator.
     */
    public boolean isShortCircuit() {
        return shortCircuit;
    }
    
    /**
     * @param shortCircuit Whether the validator's shortCircuit flag should 
     * be set.
     */
    public void setShortCircuit(boolean shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    /**
     * @return Returns the configured params to set on the validator. 
     */
    public Map getParams() {
        return params;
    }
    
    /**
     * @param params The configured params to set on the validator.
     */
    public void setParams(Map params) {
        this.params = params;
    }
    
    /**
     * @return Returns the type of validator to configure.
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param validatorType The type of validator to configure.
     */
    public void setType(String validatorType) {
        this.type = validatorType;
    }
}
