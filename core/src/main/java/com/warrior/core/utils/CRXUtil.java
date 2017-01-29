package com.warrior.core.utils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Utility class for crx operations.
 */
public final class CRXUtil {

    /**
     * private constructor for denying direct instantiation.
     */
    private CRXUtil() {
    }

    /**
     * Gets the service reference either from SlingBindings or from
     * BundleContext.
     * 
     * @param <T>
     *            the generic type
     * @param serviceClass
     *            the class whose reference has to be returned.
     * @param request
     *            as SlingHttpServletRequest.
     * @return T the service reference
     */
    @SuppressWarnings("unchecked")
    public static <T> T getServiceReference(final Class<T> serviceClass,
            final SlingHttpServletRequest request) {

        final SlingBindings bindings = (SlingBindings) request
                .getAttribute(SlingBindings.class.getName());
        /**
         * In case SlingBindings is not available in request attribute map. Find
         * it in the BundleContext. If this request is not originated from
         * script i.e jsp then bindings are not set in request. Other possible
         * way to get SlingBindings initialized is to set the SlingBindings in
         * the request in the jsp itself, while making a call to the Servlet.
         */
        T serviceRef;

        if (bindings == null) {
            /**
             * Get the BundleContext associated with the passed class reference.
             */
            final BundleContext bundleContext = FrameworkUtil.getBundle(
                    serviceClass).getBundleContext();
            final ServiceReference osgiRef = bundleContext
                    .getServiceReference(serviceClass.getName());
            serviceRef = (T) bundleContext.getService(osgiRef);
        } else {
            serviceRef = bindings.getSling().getService(serviceClass);
        }
        return serviceRef;
    }

    /**
     * Gets the service reference either from BundleContext.
     * 
     * @param <T>
     *            the generic type
     * @param serviceClass
     *            the class whose reference has to be returned.
     * @return T the service reference
     */
    @SuppressWarnings("unchecked")
    public static <T> T getServiceReference(final Class<T> serviceClass) {
        T serviceRef;
        /**
         * Get the BundleContext associated with the passed class reference.
         */
        final BundleContext bundleContext = FrameworkUtil.getBundle(
                serviceClass).getBundleContext();
        final ServiceReference osgiRef = bundleContext
                .getServiceReference(serviceClass.getName());
        serviceRef = (T) bundleContext.getService(osgiRef);
        return serviceRef;
    }


}