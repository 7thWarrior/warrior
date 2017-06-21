
package com.warrior.core.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.resource.JcrPropertyMap;
import org.apache.sling.jcr.resource.JcrResourceUtil;

import com.warrior.core.logging.LoggerUtil;
import com.warrior.services.impl.AdminSessionServiceImpl;

import javax.jcr.ValueFactory;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionManager;

import java.io.InputStream;
import com.day.cq.commons.jcr.JcrConstants;
/**
 * The Class JcrUtil is used for general methods such as getting session,
 * retrieving absolute nodes, getting node properties, with proper format.
 */
public final class JcrUtil extends com.day.cq.commons.jcr.JcrUtil {

    /** The Constant TRUE. */
    private static final String TRUE = "true";

    /**
     * Instantiates a new jcr util.
     */
    private JcrUtil() {
        super();
    }

    /**
     * Check if property is set and true (return false otherwise).
     *
     * @param properties
     *            Property map
     * @param name
     *            Property name
     * @return True, if property is set and true, false otherwise
     */
    public static boolean isTrue(final ValueMap properties, final String name) {

        final String value = properties.get(name, StringUtils.EMPTY);
        return JcrUtil.TRUE.equals(value);
    }

    /**
     * Safely get session from node.
     *
     * @param node
     *            Node
     * @return Session
     */
    public static Session getSession(final Node node) {

        try {
            if (node != null) {
                return node.getSession();
            }

        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Repository Exception", e);
        }
        return null;
    }

    /**
     * Gets the property string.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property string
     */
    public static String getPropertyString(final Node node, final String name,
            final String defaultValue) {

        return JcrUtil.getProperty(node, name, defaultValue);
    }

    /**
     * Gets the property.
     *
     * @param <T>
     *            the generic type
     * @param node
     *            the node
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property
     */
    public static <T> T getProperty(final Node node, final String name,
            final T defaultValue) {

        return new JcrPropertyMap(node).get(name, defaultValue);
    }

    /**
     * Gets the property boolean.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property boolean
     */
    public static boolean getPropertyBoolean(final Node node,
            final String name, final boolean defaultValue) {

        try {
            final Property property = JcrUtil.getProperty(node, name);
            if (property != null) {
                return JcrUtil.toBoolean(property.getString());
            }
        } catch (final RepositoryException e) {

            LoggerUtil.errorLog(JcrUtil.class, "Repository Exception", e);
        }

        return defaultValue;
    }

    /**
     * Gets the property.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @return the property
     */
    public static Property getProperty(final Node node, final String name) {

        try {
            if (node.hasProperty(name)) {
                return node.getProperty(name);
            }
        } catch (final RepositoryException e) {

            LoggerUtil.debugLog(JcrUtil.class, "Could not get property '"
                    + name + "': ", e);
        }
        return null;
    }

    /**
     * To boolean.
     *
     * @param input
     *            the input
     * @return true, if successful
     */
    public static boolean toBoolean(final Object input) {

        if (input instanceof String) {
            if (input.equals("1") || input.equals("0")) {
                return BooleanUtils.toBoolean(Integer.parseInt((String) input));
            } else {
                return BooleanUtils.toBoolean((String) input);
            }
        } else if (input instanceof Integer) {
            return BooleanUtils.toBoolean((Integer) input);
        } else if (input instanceof Boolean) {
            return BooleanUtils.toBoolean((Boolean) input);
        }
        return false;
    }

    /**
     * Gets the property calendar.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @return the property calendar
     */
    public static Calendar getPropertyCalendar(final Node node,
            final String name) {

        return JcrUtil.getPropertyCalendar(node, name, Calendar.getInstance());
    }

    /**
     * Gets the property calendar.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @param defaultValue
     *            the default value
     * @return the property calendar
     */
    public static Calendar getPropertyCalendar(final Node node,
            final String name, final Calendar defaultValue) {

        try {
            final Property property = JcrUtil.getProperty(node, name);
            if (property != null) {
                if (property.getType() == PropertyType.DATE) {
                    return property.getDate();
                } else if (property.getType() == PropertyType.STRING) {
                    final String dateStr = property.getString();
                    final Date date = DateUtils.parseDate(dateStr,
                            new String[] { Constants.FORMAT_DATE_SHORT });
                    final Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    return cal;
                }
                return property.getDate();
            }
        } catch (final ParseException e) {
            LoggerUtil.errorLog(JcrUtil.class, "ParseExceptionn", e);
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Repository Exception", e);
        }
        return defaultValue;
    }

    /**
     * Gets the absolute node.
     *
     * @param session
     *            the session
     * @param absolutePath
     *            the absolute path
     * @return the absolute node
     */
    public static Node getAbsoluteNode(final Session session,
            final String absolutePath) {

        try {
            if (!StringUtils.isEmpty(absolutePath)
                    && !absolutePath.contains("=")
                    && session.nodeExists(absolutePath)) {
                return session.getNode(absolutePath);
            }
        } catch (final NullPointerException e) {
            LoggerUtil.errorLog(JcrUtil.class, "NullPointer Exception", e);
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Repository Exception", e);
        }
        return null;
    }

    /**
     * Gets the absolute node.
     *
     * @param node
     *            the node
     * @param absolutePath
     *            the absolute path
     * @return the absolute node
     */
    public static Node getAbsoluteNode(final Node node,
            final String absolutePath) {

        return JcrUtil.getAbsoluteNode(JcrUtil.getSession(node), absolutePath);
    }

    /**
     * Gets the property string array.
     *
     * @param property
     *            the property
     * @return the property string array
     */
    public static String[] getPropertyStringArray(final Property property) {

        final List<String> stringList = new LinkedList<String>();
        try {
            if (property != null && property.getType() == PropertyType.STRING) {
                if (property.isMultiple()) {
                    final Value[] values = property.getValues();
                    for (final Value value : values) {
                        stringList.add(value.getString());
                    }
                } else {
                    stringList.add(property.getString());
                }
            }
        } catch (final NullPointerException e) {
            LoggerUtil.errorLog(JcrUtil.class, "NullPointer Exception", e);
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Repository Exception", e);
        }
        return stringList.toArray(new String[stringList.size()]);
    }

    /**
     * Gets the property string array.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @return the property string array
     */
    public static String[] getPropertyStringArray(final Node node,
            final String name) {

        return JcrUtil.getPropertyStringArray(JcrUtil.getProperty(node, name));
    }

    /**
     * Gets the property string.
     *
     * @param node
     *            the node
     * @param name
     *            the name
     * @return the property string
     */
    public static String getPropertyString(final Node node, final String name) {

        return JcrUtil.getPropertyString(node, name, StringUtils.EMPTY);
    }

    /**
     * Safely set property on JCR node.
     *
     * @param node
     *            Node
     * @param name
     *            Property name
     * @param value
     *            Value
     * @param autoSave
     *            Auto save?
     * @return True, if successful
     */
    public static boolean setProperty(final Node node, final String name,
            final Object value, final boolean autoSave) {
        try {
            JcrResourceUtil.setProperty(node, name, value);
            if (autoSave) {
                JcrUtil.save(node.getSession());
            }
            return true;
        } catch (final NullPointerException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not set property: {}", e);
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not set property: {}", e);
        }
        return false;
    }
    
    
    /**
     * Safely set property on JCR node.
     *
     * @param node
     *            Node
     * @param name
     *            Property name
     * @param value
     *            Value
     * @param autoSave
     *            Auto save?
     * @return True, if successful
     */
     public static synchronized boolean setProperty(final Node node, final String name,
            final Object value, final boolean autoSave,javax.jcr.Session session) {
        try {
        	
             if(!node.isCheckedOut()){
            	 LoggerUtil.debugLog(JcrUtil.class,"Inside synchronized setProperty method, Operating node is not checked out; "
            	 		+ "checking out current node  -->"+node.getPath());
            	 VersionManager vm = session.getWorkspace().getVersionManager();
            	 vm.checkout(node.getParent().getParent().getPath());
             }
            JcrResourceUtil.setProperty(node, name, value);
            if (autoSave) {
                JcrUtil.save(session,node);
            }
            return true;
        } catch (final NullPointerException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not set property: {}", e);
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not set property: {}", e);
        }
        return false;
    }
    
    
    /**
     * Sets the date property value in the input node.
     * @param node The node for which property is to be set.
     * @param name The name of the property to be set.
     * @param date The date value to be set in the property.
     * @return {@code true} if the property has been set; {@code false}
     * if setting the property failed.
     */
	public static boolean setDateProperty(final Node node, final String name,
			final Date date) {
		return JcrUtil.setDateProperty(node, name, date, true);
	}
	
	/**
	 * Sets the date property value in the input node.
     * @param node The node for which property is to be set.
     * @param name The name of the property to be set.
     * @param date The date value to be set in the property.
	 * @param autoSave {@code true} if property is to be saved after being set.
	 * @return {@code true} if the property has been set; {@code false}
     * if setting the property failed.
	 */
	public static boolean setDateProperty(final Node node, final String name,
			final Date date, final boolean autoSave) {
		if (date != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			try {
				node.setProperty(name, cal);
				if (autoSave) {
	                JcrUtil.save(node.getSession());
	            }
				return true;
			} catch (ValueFormatException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (VersionException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (LockException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (ConstraintViolationException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (RepositoryException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			}
		}
		return false;
	}
	
	/**
     * Sets the date property value in the input node.
     * @param node The node for which property is to be set.
     * @param name The name of the property to be set.
     * @param cal The calendar value to be set in the property.
     * @return {@code true} if the property has been set; {@code false}
     * if setting the property failed.
     */
	public static boolean setCalendarProperty(final Node node, final String name,
			final Calendar cal) {
		return JcrUtil.setCalendarProperty(node, name, cal, true);
	}
	
	/**
	 * Sets the date property value in the input node.
     * @param node The node for which property is to be set.
     * @param name The name of the property to be set.
     * @param cal The calendar value to be set in the property.
	 * @param autoSave {@code true} if property is to be saved after being set.
	 * @return {@code true} if the property has been set; {@code false}
     * if setting the property failed.
	 */
	public static boolean setCalendarProperty(final Node node, final String name,
			final Calendar cal, final boolean autoSave) {
		if (cal != null) {
			try {
				node.setProperty(name, cal);
				if (autoSave) {
	                JcrUtil.save(node.getSession());
	            }
				return true;
			} catch (ValueFormatException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (VersionException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (LockException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (ConstraintViolationException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			} catch (RepositoryException e) {
				LoggerUtil.errorLog(JcrUtil.class,
						"Could not set property: {}", e);
			}
		}
		return false;
	}

    /**
     * Safely save session.
     *
     * @param session
     *            Session
     */
    public static synchronized void save(final Session session,final Node node) {
        try {
        	if(!node.isCheckedOut()){
           	 LoggerUtil.debugLog(JcrUtil.class,"Inside synchronized save method, Operating node is not checked out; "
           	 		+ "checking out current node  -->"+node.getPath());
           	 VersionManager vm = session.getWorkspace().getVersionManager();
           	 vm.checkout(node.getParent().getParent().getPath());
            }
            if (session != null && session.hasPendingChanges()) {
                session.refresh(true);
                session.save();
            }
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not save session: ", e);
        }
    }
    
    
    /**
     * Safely save session.
     *
     * @param session
     *            Session
     */
    public static void save(final Session session) {
        try {
            if (session != null && session.hasPendingChanges()) {
                session.refresh(true);
                session.save();
            }
        } catch (final RepositoryException e) {
            LoggerUtil.errorLog(JcrUtil.class, "Could not save session: ", e);
        }
    }

    /**
     * Extract property.
     *
     * @param node
     *            the node
     * @param property
     *            the property
     * @return the string
     */
    public static String extractProperty(final Node node, final String property) {
        String name = StringUtils.EMPTY;
        try {
            if (node.hasProperty(property)
                    && (node.getProperty(property).isMultiple())) {
                final Value[] values = node.getProperty(property).getValues();
                final StringBuilder sBuild = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    if (i < (values.length - 1)) {
                        sBuild.append(values[i].getString()).append(',');
                    } else {
                        sBuild.append(values[i].getString());
                    }
                }
                name = sBuild.toString();
            } else if (node.hasProperty(property)) {
                name = node.getProperty(property).getString();
            }
        } catch (final PathNotFoundException pnfe) {
            LoggerUtil
                    .errorLog(
                            JcrUtil.class,
                            "PathNotFoundException exception occured while getting property {0}: {1}",
                            property, pnfe);
        } catch (final ValueFormatException vfe) {
            LoggerUtil
                    .errorLog(
                            JcrUtil.class,
                            "ValueFormatException exception occured while getting property {0}: {1}",
                            property, vfe);
        } catch (final RepositoryException rfe) {
            LoggerUtil
                    .errorLog(
                            JcrUtil.class,
                            "RepositoryException exception occured while getting property {0}: {1}",
                            property, rfe);
        }
        return name;
    }

    
    /**
     * Create file
     * @param parentNode Parent node
    * @param name File node name
     * @param data File data
     * @param mimeType Mime type
     * @param autoSave Auto save?
     * @return File node
     */
    public static Node createFile(Node parentNode, final String name, final InputStream data,
                                  final String mimeType, final boolean autoSave) {
        try {
            String fileName = (StringUtils.contains(name, "\\"))
                ? StringUtils.substringAfterLast(name, "\\")
                : name;
            Node fileNode = parentNode.addNode(fileName, JcrConstants.NT_FILE);
            Node resNode = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);
            final ValueFactory valueFactory = resNode.getSession().getValueFactory();
            resNode.setProperty(JcrConstants.JCR_DATA, valueFactory.createBinary(data));
            resNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
            if (autoSave) {
                save(resNode.getSession());
            }
            return fileNode;
        }
        catch (RepositoryException e) {	
            LoggerUtil
            .errorLog(
                    JcrUtil.class,
                    "RepositoryException exception occured while createFile");
            
        }
        return null;
    }
    
	/**
	 * Fetches a node if it exists at the absolute path or creates a node at the
	 * specified absolute path if it does not already exists.
	 * 
	 * @param absolutePath
	 *            The path where node has to be created.
	 * @param nodeType
	 *            The node type.
	 * @param session
	 *            The session object.
	 * @return Node fetched from / created at the specified absolute path.
	 */
	public static Node createNode(String absolutePath, String nodeType,
			Session session) {
		Node node = null;
		try {
			node = com.day.cq.commons.jcr.JcrUtil.createPath(absolutePath,
					nodeType, session);
			JcrUtil.save(session);
		} catch (RepositoryException re) {
			LoggerUtil.errorLog(JcrUtil.class,
					"Reporsitory Exception while creating node [{}].\n{}",
					absolutePath, re);
		}
		return node;
	}
	

}
