
package com.warrior.core.utils;

import java.util.Dictionary;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * The Class AbstractConfigService.
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractConfigService {

    /**
     * reads the string property from configuration.
     *
     * @param properties
     *            the properties
     * @param propertyName
     *            the property name
     * @return the string property
     */
    protected String getStringProperty(final Dictionary properties,
            final String propertyName) {

        String value = (String) properties.get(propertyName);
        if (value == null) {
            value = StringUtils.EMPTY;

        }
        return value;
    }

    /**
     * reads the int property from configuration.
     *
     * @param properties
     *            the properties
     * @param propertyName
     *            the property name
     * @return the int property
     */
    protected int getIntProperty(final Dictionary properties,
            final String propertyName) {

        int value = -1;
        final Object obj = properties.get(propertyName);
        if (obj instanceof Integer) {
            value = (Integer) obj;
        } else if (obj instanceof Long) {
            final long longValue = (Long) obj;
            value = (int) longValue;
        } else if (obj instanceof String) {
            value = NumberUtils.toInt(this.getStringProperty(properties,
                    propertyName));
        }

        return value;
    }

    /**
     * reads the double property from configuration.
     *
     * @param properties
     *            the properties
     * @param propertyName
     *            the property name
     * @return the double property
     */
    protected double getdoubleProperty(final Dictionary properties,
            final String propertyName) {

        double value = -1;
        final Object obj = properties.get(propertyName);
        if (obj instanceof Double) {
            value = (Double) obj;
        } else if (obj instanceof String) {
            value = NumberUtils.toDouble(this.getStringProperty(properties,
                    propertyName));
        }

        return value;
    }

    /**
     * reads the boolean property from configuration.
     *
     * @param properties
     *            the properties
     * @param propertyName
     *            the property name
     * @return the boolean property
     */
    protected boolean getbooleanProperty(final Dictionary properties,
            final String propertyName) {

        boolean value = false;
        final Object obj = properties.get(propertyName);
        if (obj instanceof Boolean) {
            value = (Boolean) obj;
        } else if (obj instanceof String) {
            if ("1".equals(obj) || "0".equals(obj)) {
                value = BooleanUtils.toBoolean(Integer.parseInt((String) obj));
            } else {
                value = BooleanUtils.toBoolean((String) obj);
            }
        }
        return value;
    }

    /**
     * reads the string array property from configuration.
     *
     * @param properties
     *            the properties
     * @param propertyName
     *            the property name
     * @return the string property
     */
    protected String[] getStringArrayProperty(final Dictionary properties,
            final String propertyName) {
    	String[] value;
    	if (properties.get(propertyName) instanceof String) {
    		value = new String[1];
    		value[0] = (String) properties.get(propertyName);
    	} else if (properties.get(propertyName) instanceof String[]) {
    		value = (String[]) properties.get(propertyName);
    	} else {
    		value = new String[0];
    	}
        
        if (value == null) {
            value = new String[0];
        }
        return value;
    }

}
