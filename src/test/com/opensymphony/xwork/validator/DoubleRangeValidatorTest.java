package com.opensymphony.xwork.validator;

import junit.framework.TestCase;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import com.opensymphony.xwork.*;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.validator.validators.DoubleRangeFieldValidator;
import com.opensymphony.xwork.config.providers.MockConfigurationProvider;
import com.opensymphony.xwork.config.ConfigurationManager;

/**
 * Unit test for {@link DoubleRangeFieldValidator}.
 *
 * @author <a href="mailto:hermanns@aixcept.de">Rainer Hermanns</a>
 * @author Claus Ibsen
 * @version $Id: DoubleRangeValidatorTest.java 871 2006-03-04 11:39:13Z rainerh $
 */
public class DoubleRangeValidatorTest extends TestCase {

    public void testRangeValidationWithError() throws Exception {
        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", MockConfigurationProvider.VALIDATION_ACTION_NAME, null);
        proxy.execute();
        assertTrue(((ValidationAware) proxy.getAction()).hasFieldErrors());

        Map errors = ((ValidationAware) proxy.getAction()).getFieldErrors();
        Iterator it = errors.entrySet().iterator();

        List errorMessages = (List) errors.get("percentage");
        assertNotNull("Expected double range validation error message.", errorMessages);
        assertEquals(1, errorMessages.size());

        String errorMessage = (String) errorMessages.get(0);
        assertNotNull("Expecting: percentage must be between 0.1 and 10.1, current value is 100.0123.", errorMessage);
        assertEquals("percentage must be between 0.1 and 10.1, current value is 100.0123.", errorMessage);
    }

    public void testRangeValidationNoError() throws Exception {
        ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy("", "percentage", null);
        proxy.execute();
        assertTrue(((ValidationAware) proxy.getAction()).hasFieldErrors());

        Map errors = ((ValidationAware) proxy.getAction()).getFieldErrors();
        Iterator it = errors.entrySet().iterator();

        List errorMessages = (List) errors.get("percentage");
        assertNull("Expected no double range validation error message.", errorMessages);
    }

    public void testRangeNoExclusiveAndNoValueInStack() throws Exception {
        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setFieldName("hello");
        val.validate("world");
    }

    public void testRangeSimpleDoubleValueInStack() throws Exception {
        MyTestProduct prod = new MyTestProduct();
        prod.setName("coca cola");
        prod.setPrice(5.99);

        OgnlValueStack stack = new OgnlValueStack();
        stack.push(prod);
        ActionContext.getContext().setValueStack(stack);

        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setMinInclusive("0");
        val.setMaxInclusive("10");
        val.setFieldName("price");
        val.validate(prod);
    }

    public void testRangeRealDoubleValueInStack() throws Exception {
        MyTestProduct prod = new MyTestProduct();
        prod.setName("coca cola");
        prod.setPrice(5.99);
        prod.setVolume(new Double(12.34));

        OgnlValueStack stack = new OgnlValueStack();
        stack.push(prod);
        ActionContext.getContext().setValueStack(stack);

        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setMinInclusive("0");
        val.setMaxInclusive("30");
        val.setFieldName("volume");
        val.validate(prod);
    }

    public void testRangeNotADoubleObjectValueInStack() throws Exception {
        MyTestProduct prod = new MyTestProduct();
        prod.setName("coca cola");

        OgnlValueStack stack = new OgnlValueStack();
        stack.push(prod);
        ActionContext.getContext().setValueStack(stack);

        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setMinInclusive("0");
        val.setMaxInclusive("10");
        val.setFieldName("name");

        DelegatingValidatorContext context = new DelegatingValidatorContext(new ValidationAwareSupport());
        val.setValidatorContext(context);

        val.validate(prod);

        assertEquals("0", val.getMinInclusive());
        assertEquals("10", val.getMaxInclusive());
    }

    public void testEdgeOfMaxRange() throws Exception {
        MyTestProduct prod = new MyTestProduct();
        prod.setName("coca cola");
        prod.setPrice(9.95);

        OgnlValueStack stack = new OgnlValueStack();
        stack.push(prod);
        ActionContext.getContext().setValueStack(stack);

        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setFieldName("price");

        DelegatingValidatorContext context = new DelegatingValidatorContext(new ValidationAwareSupport());
        val.setValidatorContext(context);

        val.setMaxInclusive("9.95");
        val.validate(prod); // should pass
        assertTrue(! context.hasErrors());
        assertEquals("9.95", val.getMaxInclusive());

        val.setMaxExclusive("9.95");
        val.validate(prod); // should not pass
        assertTrue(context.hasErrors());
        assertEquals("9.95", val.getMaxExclusive());
    }

    public void testEdgeOfMinRange() throws Exception {
        MyTestProduct prod = new MyTestProduct();
        prod.setName("coca cola");
        prod.setPrice(9.95);

        OgnlValueStack stack = new OgnlValueStack();
        stack.push(prod);
        ActionContext.getContext().setValueStack(stack);

        DoubleRangeFieldValidator val = new DoubleRangeFieldValidator();
        val.setFieldName("price");

        DelegatingValidatorContext context = new DelegatingValidatorContext(new ValidationAwareSupport());
        val.setValidatorContext(context);

        val.setMinInclusive("9.95");
        val.validate(prod); // should pass
        assertTrue(! context.hasErrors());

        val.setMinExclusive("9.95");
        val.validate(prod); // should not pass
        assertTrue(context.hasErrors());
    }

    protected void setUp() throws Exception {
        ConfigurationManager.clearConfigurationProviders();
        ConfigurationManager.addConfigurationProvider(new MockConfigurationProvider());
        ConfigurationManager.getConfiguration().reload();
    }

    private class MyTestProduct {
        private double price;
        private Double volume;
        private String name;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }
    }

}
