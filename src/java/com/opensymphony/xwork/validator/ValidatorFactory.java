/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.xwork.validator;

import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.XworkException;
import com.opensymphony.xwork.util.ResourceScanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * ValidatorFactory is responsible for
 * <ul>
 *  <li>reading in validators (eg. what validators are registired) from a
 *      config file eg. default.xml that comes with XWork / validators.xml
 *      / *-validators.xml that lies within the classpath</li>
 *  <li>looking up a validator</li>
 *  <li>registering a validator</li>
 *  <li>obtaining a validator based on
 *      {@link com.opensymphony.xwork.validator.ValidatorConfig}</li>
 * </ul>
 *
 * <p>
 * <!-- START SNIPPET: javadoc -->
 * Validation rules are handled by validators, which must be registered with 
 * the ValidatorFactory (using the registerValidator method). The simplest way to do so is to add a file name 
 * validators.xml in the root of the classpath (/WEB-INF/classes) that declares 
 * all the validators you intend to use.
 *
 * As of XWork 1.2.4 / WebWork 2.2.7 and above, validator definition are loaded in the following order, where
 * validators loaded latter could override those loaded previously if there have the same name :-
 * <ul>
 *  <li>default validators found in com/opensymphony/xwork/validator/validators/default.xml (always loaded)</li>
 *  <li>all validators.xml that lies in the root of the classpath (user defined)</li>
 *  <li>all *-validators.xml that lies in the root of the classpath (user defined)</li>
 * </ul>
 * Validators definition defined in *-validators.xml will override those defined in validators.xml if they
 * have similar names. This pattern applies to default.xml as well.
 *
 * WebWork / XWork will always have the validator definition defined in
 * com/opensymphony/xwork/validator/validators/default.xml (which comes with xwork jar file) loaded.
 * <!-- END SNIPPET: javadoc -->
 * </p>
 *
 * 
 * <p>
 * <b>INFORMATION</b>
 * <!-- START SNIPPET: information -->
 * Normally, to have custom validators, we could have them defined in validators.xml that resides in
 * the classpath. However since, WebWork 2.2.7 / XWork 1.2.4 and above, we could have *-validators.xml
 * that lies in the root of the classpath and WebWork / XWork will pick it up. This allows us to have
 * various validator definition that lies in a jar file itself (at the root of course).
 *
 * To override a validator definition defined by XWork, we could just define a a custom validator mean
 * to replace it with the same name and have them in either validators.xml or *-validators.xml that lies
 * in the root of the classpath (could be in the root of a jar file as well).
 *
 * See ValidatorFactory static block for details.
 * <!-- END SNIPPET: information -->
 * </p>
 * 
 * <p><b>Note:</b> 
 * <!-- START SNIPPET: turningOnValidators -->
 * The default validationWorkflowStack already includes this.<br/>
 * All that is required to enable validation for an Action is to put the 
 * ValidationInterceptor in the interceptor refs of the action (see xwork.xml) like so:
 * <!-- END SNIPPET: turningOnValidators -->
 * </p>
 * 
 * <pre>
 * <!-- START SNIPPET: exTurnOnValidators -->
 *     &lt;interceptor name="validator" class="com.opensymphony.xwork.validator.ValidationInterceptor"/&gt;
 * <!-- END SNIPPET: exTurnOnValidators -->
 * </pre>
 * 
 * <p><b>Field Validators</b>
 * <!-- START SNIPPET: fieldValidators -->
 * Field validators, as the name indicate, act on single fields accessible through an action. 
 * A validator, in contrast, is more generic and can do validations in the full action context, 
 * involving more than one field (or even no field at all) in validation rule.
 * Most validations can be defined on per field basis. This should be preferred over 
 * non-field validation whereever possible, as field validator messages are bound to the 
 * related field and will be presented next to the corresponding input element in the 
 * respecting view.
 * <!-- END SNIPPET: fieldValidators -->
 * </p>
 * 
 * <p><b>Non Field Validators</b>
 * <!-- START SNIPPET: nonFieldValidators -->
 * Non-field validators only add action level messages. Non-field validators 
 * are mostly domain specific and therefore offer custom implementations. 
 * The most important standard non-field validator provided by XWork/WebWork 
 * is ExpressionValidator.
 * <!-- END SNIPPET: nonFieldValidators -->
 * </p>
 * 
 * <p><b>NOTE:</b>
 * <!-- START SNIPPET: validatorsNote -->
 * Non-field validators takes precedence over field validators 
 * regardless of the order they are defined in *-validation.xml. If a non-field 
 * validator is short-circuited, it will causes its non-field validator to not 
 * being executed. See validation framework documentation for more info.
 * <!-- END SNIPPET: validatorsNote -->
 * </p>
 * 
 * <p><b>VALIDATION RULES:</b>
 * <!-- START SNIPPET: validationRules1 -->
 * Validation rules can be specified:
 * <ol>
 *  <li> Per Action class: in a file named ActionName-validation.xml</li>
 *  <li> Per Action alias: in a file named ActionName-alias-validation.xml</li>
 *  <li> Inheritance hierarchy and interfaces implemented by Action class: 
 *  WebWork searches up the inheritance tree of the action to find default 
 *  validations for parent classes of the Action and interfaces implemented</li>
 * </ol>
 * Here is an example for SimpleAction-validation.xml:
 * <!-- END SNIPPET: validationRules1 -->
 * <p>
 * 
 * <pre>
 * <!-- START SNIPPET: exValidationRules1 -->
 * &lt;!DOCTYPE validators PUBLIC "-//OpenSymphony Group//XWork Validator 1.0.2//EN"
 *        "http://www.opensymphony.com/xwork/xwork-validator-1.0.2.dtd"&gt;
 * &lt;validators&gt;
 *   &lt;field name="bar"&gt;
 *       &lt;field-validator type="required"&gt;
 *           &lt;message&gt;You must enter a value for bar.&lt;/message&gt;
 *       &lt;/field-validator&gt;
 *       &lt;field-validator type="int"&gt;
 *           &lt;param name="min">6&lt;/param&gt;
 *           &lt;param name="max"&gt;10&lt;/param&gt;
 *           &lt;message&gt;bar must be between ${min} and ${max}, current value is ${bar}.&lt;/message&gt;
 *       &lt;/field-validator&gt;
 *   &lt;/field&gt;
 *   &lt;field name="bar2"&gt;
 *       &lt;field-validator type="regex"&gt;
 *           &lt;param name="regex"&gt;[0-9],[0-9]&lt;/param&gt;
 *           &lt;message&gt;The value of bar2 must be in the format "x, y", where x and y are between 0 and 9&lt;/message&gt;
 *      &lt;/field-validator&gt;
 *   &lt;/field&gt;
 *   &lt;field name="date"&gt;
 *       &lt;field-validator type="date"&gt;
 *           &lt;param name="min"&gt;12/22/2002&lt;/param&gt;
 *           &lt;param name="max"&gt;12/25/2002&lt;/param&gt;
 *           &lt;message&gt;The date must be between 12-22-2002 and 12-25-2002.&lt;/message&gt;
 *       &lt;/field-validator&gt;
 *   &lt;/field&gt;
 *   &lt;field name="foo"&gt;
 *       &lt;field-validator type="int"&gt;
 *           &lt;param name="min"&gt;0&lt;/param&gt;
 *           &lt;param name="max"&gt;100&lt;/param&gt;
 *           &lt;message key="foo.range"&gt;Could not find foo.range!&lt;/message&gt;
 *       &lt;/field-validator&gt;
 *   &lt;/field&gt;
 *   &lt;validator type="expression"&gt;
 *       &lt;param name="expression"&gt;foo lt bar &lt;/param&gt;
 *       &lt;message&gt;Foo must be greater than Bar. Foo = ${foo}, Bar = ${bar}.&lt;/message&gt;
 *   &lt;/validator&gt;
 * &lt;/validators&gt;
 * <!-- END SNIPPET: exValidationRules1 -->
 * </pre>
 * 
 * 
 * <p>
 * <!-- START SNIPPET: validationRules2 -->
 * Here we can see the configuration of validators for the SimpleAction class. 
 * Validators (and field-validators) must have a type attribute, which refers 
 * to a name of an Validator registered with the ValidatorFactory as above. 
 * Validator elements may also have &lt;param&gt; elements with name and value attributes 
 * to set arbitrary parameters into the Validator instance. See below for discussion 
 * of the message element.
 * <!-- END SNIPPET: validationRules2 -->
 * </p>
 * 
 * 
 * 
 * <!-- START SNIPPET: validationRules3 -->
 * <p>Each Validator or Field-Validator element must define one message element inside 
 * the validator element body. The message element has 1 attributes, key which is not 
 * required. The body of the message tag is taken as the default message which should 
 * be added to the Action if the validator fails.Key gives a message key to look up 
 * in the Action's ResourceBundles using getText() from LocaleAware if the Action 
 * implements that interface (as ActionSupport does). This provides for Localized 
 * messages based on the Locale of the user making the request (or whatever Locale 
 * you've set into the LocaleAware Action). After either retrieving the message from 
 * the ResourceBundle using the Key value, or using the Default message, the current 
 * Validator is pushed onto the ValueStack, then the message is parsed for \$\{...\} 
 * sections which are replaced with the evaluated value of the string between the 
 * \$\{ and \}. This allows you to parameterize your messages with values from the 
 * Validator, the Action, or both.</p>
 * 
 *
 * <p>If the validator fails, the validator is pushed onto the ValueStack and the 
 * message - either the default or the locale-specific one if the key attribute is
 * defined (and such a message exists) - is parsed for ${...} sections which are 
 * replaced with the evaluated value of the string between the ${ and }. This 
 * allows you to parameterize your messages with values from the validator, the 
 * Action, or both. </p>
 * 
 * <p><b>NOTE:</b>Since validation rules are in an XML file, you must make sure 
 * you escape special characters. For example, notice that in the expression 
 * validator rule above we use "&gt;" instead of ">". Consult a resource on XML 
 * for the full list of characters that must be escaped. The most commonly used 
 * characters that must be escaped are: & (use &amp;), > (user &gt;), and < (use &lt;).</p>
 *  
 * <p>Here is an example of a parameterized message:</p>
 * <p>This will pull the min and max parameters from the IntRangeFieldValidator and 
 * the value of bar from the Action.</p>
 * <!-- END SNIPPET: validationRules3 -->
 * 
 * <pre>
 * <!-- START SNIPPET: exValidationRules3 -->
 *    bar must be between ${min} and ${max}, current value is ${bar}.
 * <!-- END SNIPPET: exValidationRules3 -->
 * </pre>
 * 
 * @author Jason Carreira
 * @author James House
 * @author tmjee
 * @version $Date: 2007-11-29 18:39:08 +0900 (木, 29 11 2007) $ $Id: ValidatorFactory.java 1692 2007-11-29 09:39:08Z tm_jee $
 */
public class ValidatorFactory {

    private static Map validators = new HashMap();
    private static Log LOG = LogFactory.getLog(ValidatorFactory.class);

    static {
        try {
            parseValidators();
        }
        catch(Exception e) {
            throw new XworkException(e);
        }
    }


    private ValidatorFactory() {
    }


    /**
     * Removed all registered validators.
     */
    public static void reset() {
        validators.clear();
    }

    /**
     * Get a Validator that matches the given configuration.
     *
     * @param cfg  the configurator.
     * @return  the validator.
     */
    public static Validator getValidator(ValidatorConfig cfg) {

        String className = lookupRegisteredValidatorType(cfg.getType());

        Validator validator;

        try {
            // instantiate the validator, and set configured parameters
            //todo - can this use the ThreadLocal?
            validator = ObjectFactory.getObjectFactory().buildValidator(className, cfg.getParams(), null); // ActionContext.getContext().getContextMap());
        } catch (Exception e) {
            final String msg = "There was a problem creating a Validator of type " + className + " : caused by " + e.getMessage();
            throw new XworkException(msg, e, cfg);
        }

        // set other configured properties
        validator.setMessageKey(cfg.getMessageKey());
        validator.setMessageParameters(cfg.getMessageParams());
        validator.setDefaultMessage(cfg.getDefaultMessage());
        if (validator instanceof ShortCircuitableValidator) {
            ((ShortCircuitableValidator) validator).setShortCircuit(cfg.isShortCircuit());
        }

        return validator;
    }

    /**
     * Registers the given validator to the existing map of validators.
     * This will <b>add</b> to the existing list.
     *
     * @param name    name of validator to add.
     * @param className   the FQ classname of the validator.
     */
    public static void registerValidator(String name, String className) {
        if (validators.containsKey(name)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validator with name ["+name+"] with class ["+validators.get(name)+"], replacing it with ["+className+"]");
            }
        }
        else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Registering validator of class " + className + " with name " + name);
            }
        }
        validators.put(name, className);
    }

    /**
     * Lookup to get the FQ classname of the given validator name.
     *
     * @param name   name of validator to lookup.
     * @return  the found FQ classname
     * @throws IllegalArgumentException is thrown if the name is not found.
     */
    public static String lookupRegisteredValidatorType(String name) {
        // lookup the validator class mapped to the type name
        String className = (String) validators.get(name);

        if (className == null) {
            throw new IllegalArgumentException("There is no validator class mapped to the name " + name);
        }

        return className;
    }

    /**
     * Parse all the validators definition, using the following precedence:-
     * <ul>
     *  <li>Defaults from com/opensymphony/xwork/validator/validators/default.xml</li>
     *  <li>validators.xml that lies in the root of the classpath</li>
     *  <li>*-validators.xml that lies in the root of the classpath</li>
     * </ul>
     */
    public static void parseValidators() throws IOException, URISyntaxException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Loading validator definitions.");
        }

        // Try loading com/opensymphony/xwork/validator/validators/default.xml
        {
            String resourceName = "com/opensymphony/xwork/validator/validators/default.xml";
            loadValidators(ClassLoaderUtil.getResourceAsStream(resourceName, ValidatorFactory.class), resourceName);
        }


        // Try loading from validators.xml
        {
            String resourceName = "validators.xml";
            loadValidators(ClassLoaderUtil.getResourceAsStream(resourceName, ValidatorFactory.class), resourceName);
        }


        // Try loading from *-validators.xml
        {
            ResourceScanner resourceScanner = new ResourceScanner(
                    new String[] { "" }, ValidatorFactory.class
            );
            List validatorDefs = resourceScanner.scanForResources(new ResourceScanner.Filter() {
                public boolean accept(URL resource) {
                    // eg. file:/C:/j2sdk1.5.12/jre/lib/javaws.jar!/COPYRIGHT
                    if (resource.getFile().endsWith("-validators.xml")) {
                        return true;
                    }
                    return false;
                }
            });
            for (Iterator i = validatorDefs.iterator(); i.hasNext(); ) {
                URL validatorDefUrl = (URL) i.next();
                loadValidators(validatorDefUrl.openStream(),
                        validatorDefUrl.getFile().substring(validatorDefUrl.getFile().lastIndexOf("/")+1));
            }
        }
    }


    private static void loadValidators(InputStream is, String resourceName) {
         if (is != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading validators definition from "+resourceName);
            }
            try {
                ValidatorFileParser.parseValidatorDefinitions(is, resourceName);
            }
            finally {
                try { is.close(); } catch(IOException e) { };
            }
         }
    }
}
