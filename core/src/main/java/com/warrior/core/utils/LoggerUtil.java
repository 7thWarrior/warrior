package aem.community.examples.codes.core.utils;

import javax.servlet.http.HttpSession;

import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LoggerUtil.
 */
public final class LoggerUtil {

    /** The client ip. */
    private static String clientIp;

    /** The session id. */
    private static String sessionId;

    /** The user id. */
    private static String userId;

    /** The Constant USER_ID. */
    private static final String USER_ID = "user id:";

    /** The Constant CLIENT_IP. */
    private static final String CLIENT_IP = "client ip:";

    /** The Constant SESSION_ID. */
    private static final String SESSION_ID = "session id:";

    /** The Constant SPACE. */
    private static final String SPACE = " ";

    /** The Constant DEFAULT_VALUE. */
    private static final String DEFAULT_VALUE = "...";

    /**
     * Instantiates a new logger util.
     */
    private LoggerUtil() {

    }

    /**
     * Gets the info logger.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param request
     *            the request
     * @param msgArray
     *            the msg array
     * @return the info logger
     
    @SuppressWarnings("rawtypes")
    public static void detailedInfoLog(final Class className,
            final String messageText, final SlingHttpServletRequest request,
            final Object... msgArray) {

        final Logger logger = LoggerUtil.getLogger(className);
        LoggerUtil.setLogVariables(request);
        logger.info(LoggerUtil.CLIENT_IP + LoggerUtil.clientIp
                + LoggerUtil.SPACE + LoggerUtil.SESSION_ID
                + LoggerUtil.sessionId + LoggerUtil.SPACE + LoggerUtil.USER_ID
                + LoggerUtil.userId + LoggerUtil.SPACE + messageText, msgArray);

    }

    */
    /**
     * Gets the info log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param msgArray
     *            the msg array
     * @return the info log
     */
    @SuppressWarnings("rawtypes")
    public static void infoLog(final Class className, final String messageText,
            final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);
        logger.info(messageText, msgArray);

    }

    /**
     * Gets the debug logger.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param request
     *            the request
     * @param msgArray
     *            the msg array
     * @return the debug logger
    
    @SuppressWarnings("rawtypes")
    public static void detailedDebugLog(final Class className,
            final String messageText, final SlingHttpServletRequest request,
            final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);

        LoggerUtil.setLogVariables(request);
        logger.debug(LoggerUtil.CLIENT_IP + LoggerUtil.clientIp
                + LoggerUtil.SPACE + LoggerUtil.SESSION_ID
                + LoggerUtil.sessionId + LoggerUtil.SPACE + LoggerUtil.USER_ID
                + LoggerUtil.userId + LoggerUtil.SPACE + messageText, msgArray);
    }

     */
    /**
     * Gets the debug log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param msgArray
     *            the msg array
     * @return the debug log
     */
    @SuppressWarnings("rawtypes")
    public static void debugLog(final Class className,
            final String messageText, final Object... msgArray) {
    if(isDebugEnabled(className)){
    final Logger logger = LoggerUtil.getLogger(className);
            logger.debug(messageText, msgArray);
    }
    }

    /**
     * Detailed error log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param request
     *            the request
     * @param msgArray
     *            the msg array
    
    @SuppressWarnings("rawtypes")
    public static void detailedErrorLog(final Class className,
            final String messageText, final SlingHttpServletRequest request,
            final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);
        LoggerUtil.setLogVariables(request);
        logger.debug(LoggerUtil.CLIENT_IP + LoggerUtil.clientIp
                + LoggerUtil.SPACE + LoggerUtil.SESSION_ID
                + LoggerUtil.sessionId + LoggerUtil.SPACE + LoggerUtil.USER_ID
                + LoggerUtil.userId + LoggerUtil.SPACE + messageText, msgArray);
    }
    
      */
    /**
     * Error log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param msgArray
     *            the msg array
     */
    @SuppressWarnings("rawtypes")
    public static void errorLog(final Class className,
            final String messageText, final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);
        logger.error(messageText, msgArray);
    }

    /**
     * Detailed warn log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param request
     *            the request
     * @param msgArray
     *            the msg array
   
    @SuppressWarnings("rawtypes")
    public static void detailedWarnrLog(final Class className,
            final String messageText, final SlingHttpServletRequest request,
            final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);
        LoggerUtil.setLogVariables(request);
        logger.warn(LoggerUtil.CLIENT_IP + LoggerUtil.clientIp
                + LoggerUtil.SPACE + LoggerUtil.SESSION_ID
                + LoggerUtil.sessionId + LoggerUtil.SPACE + LoggerUtil.USER_ID
                + LoggerUtil.userId + LoggerUtil.SPACE + messageText, msgArray);
    }

      */
    /**
     * Warn log.
     * 
     * @param className
     *            the class name
     * @param messageText
     *            the message text
     * @param msgArray
     *            the msg array
     */
    @SuppressWarnings("rawtypes")
    public static void warnLog(final Class className, final String messageText,
            final Object... msgArray) {
        final Logger logger = LoggerUtil.getLogger(className);
        logger.warn(messageText, msgArray);
    }

    /**
     * determines if the debug is enabled or not.
     * 
     * @param className
     *            the class name
     * @return true, if is debug enabled
     */
    @SuppressWarnings("rawtypes")
    public static boolean isDebugEnabled(final Class className) {
        final Logger logger = LoggerUtil.getLogger(className);
        return logger.isDebugEnabled();
    }

    /**
     * Gets the logger.
     * 
     * @param className
     *            the class name
     * @return the logger
     */
    @SuppressWarnings("rawtypes")
    private static Logger getLogger(final Class className) {

        return LoggerFactory.getLogger(className);
    }

    /**
     * Sets the log variables.
     * 
     * @param request
     *            the request
     * @return the object[]
   
    private static void setLogVariables(final SlingHttpServletRequest request) {

        LoggerUtil.clientIp = RequestUtil.getIP(request);
        final HttpSession session = request.getSession();
        LoggerUtil.sessionId = session.getId();
        LoggerUtil.userId = SessionUtility.getUserIdFromCookie(request);

        if (LoggerUtil.clientIp == null) {
            LoggerUtil.clientIp = LoggerUtil.DEFAULT_VALUE;
        }

        if (LoggerUtil.sessionId == null) {
            LoggerUtil.sessionId = LoggerUtil.DEFAULT_VALUE;
        }

        if (LoggerUtil.userId == null) {
            LoggerUtil.userId = LoggerUtil.DEFAULT_VALUE;
        }

    }
      */

}
