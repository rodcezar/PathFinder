package br.unirio.bsi.pm.optimalpathfinder.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlUtils {
	
	/**
	 * Returns a single element having a given tag
	 */
	public static Element getSingleElement(Element element, String tagName)
	{
		Node node = element.getFirstChild();

		while (node != null)
		{
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().compareTo(tagName) == 0)
				return (Element) node;

			node = node.getNextSibling();
		}

		return null;
	}

	/**
	 * Returns a list of elements having a given tag
	 */
	public static List<Element> getElements(Element element, String tagName)
	{
		Node node = element.getFirstChild();
		
		List<Element> elements = new ArrayList<Element>();

		while (node != null)
		{
			if (node.getNodeType() == Node.ELEMENT_NODE
					&& node.getNodeName().compareTo(tagName) == 0)
				elements.add((Element) node);
			
			node = node.getNextSibling();
		}

		return elements;
	}
	
	/**
	 * Returns the list of integers inside a sequence of repeating tags
	 */
	public static ArrayList<Integer> getIntegerInsideTags(Element element)
	{
		String values = element.getTextContent();
								
		final Pattern pattern = Pattern.compile("\\d+"); // the regex
		final Matcher matcher = pattern.matcher(values); // your string
		
		final ArrayList<Integer> ints = new ArrayList<Integer>(); 
		
		while (matcher.find()) { // for each match
		    ints.add(Integer.parseInt(matcher.group())); // convert to int
		}
		
		return ints;
	}
	
	/**
	 * Loads an optional string attribute from a XML element
	 */
	public static String getStringAttribute(Element element, String name)
	{
		String value = element.getAttribute(name);
		return (value != null) ? value : "";
	}

	/**
	 * Loads an optional integer attribute from a XML element
	 */
	public static int getIntAttribute(Element element, String name)
	{
		String value = element.getAttribute(name);

		if (value == null)
			return 0;

		if (value.length() == 0)
			return 0;

		return Integer.parseInt(value);
	}

	/**
	 * Loads an optional double attribute from a XML element
	 */
	public static double getDoubleAttribute(Element element, String name)
	{
		String value = element.getAttribute(name);

		if (value == null)
			return 0.0;

		if (value.length() == 0)
			return 0.0;

		return Double.parseDouble(value);
	}

	/**
	 * Loads an optional string element value from a XML element
	 */
	public static String getStringNode(Element element, String name)
	{
		return getStringNode(element, name, "");
	}

	/**
	 * Loads an optional string element value from a XML element
	 */
	public static String getStringNode(Element element, String name, String _default)
	{
		Element child = getSingleElement(element, name);

		if (child == null)
			return _default;

		String value = child.getTextContent();

		if (value == null)
			return _default;

		return value;
	}

	/**
	 * Loads an optional integer element value from a XML element
	 */
	public static int getIntNode(Element element, String name)
	{
		return getIntNode(element, name, 0);
	}

	/**
	 * Loads an optional integer element value from a XML element
	 */
	public static int getIntNode(Element element, String name, int _default)
	{
		Element child = getSingleElement(element, name);

		if (child == null)
			return _default;

		String value = child.getTextContent();

		if (value == null)
			return _default;

		if (value.length() == 0)
			return _default;

		return Integer.parseInt(value);
	}

	/**
	 * Loads an optional Boolean element value from a XML element
	 */
	public static boolean getBooleanNode(Element element, String name, boolean _default)
	{
		Element child = getSingleElement(element, name);

		if (child == null)
			return _default;

		String value = child.getTextContent();

		if (value == null)
			return _default;

		if (value.compareTo("S") == 0)
			return true;

		if (value.compareTo("N") == 0)
			return false;

		return _default;
	}

	/**
	 * Loads an optional double element value from a XML element
	 */
	public static double getDoubleNode(Element element, String name)
	{
		Element child = getSingleElement(element, name);

		if (child == null)
			return 0.0;

		String value = child.getTextContent();

		if (value == null)
			return 0.0;

		if (value.length() == 0)
			return 0.0;

		return Double.parseDouble(value);
	}


	/**
	 * Creates an element without text content
	 */
	public static Element createElement(Element parent, Document document, String name)
	{
		Element element = document.createElement(name);
		parent.appendChild(element);
		return element;
	}

	/**
	 * Creates an element with a given text content
	 */
	public static Element createElement(Element parent, Document document, String name, String value)
	{
		Element element = createElement(parent, document, name);
		element.setTextContent(value);
		return element;
	}

	/**
	 * Creates an element with content in the form of an integer
	 */
	public static Element createElement(Element parent, Document document, String name, int value)
	{
		return createElement(parent, document, name, Integer.toString(value));
	}

	/**
	 * Creates an element with content in the form of a Boolean
	 */
	public static Element createElement(Element parent, Document document, String name, boolean value)
	{
		return createElement(parent, document, name, value ? "S" : "N");
	}

	/**
	 * Creates an element with content in the form of a double
	 */
	public static Element createElement(Element parent, Document document, String name, double value)
	{
		return createElement(parent, document, name, Double.toString(value));
	}


	/**
	 * Creates an attribute with a given text content
	 */
	public static void setAttribute(Element element, String name, String value)
	{
		element.setAttribute(name, value);
	}

	/**
	 * Creates an attribute with content in the form of an integer
	 */
	public static void setAttribute(Element element, String name, int value)
	{
		setAttribute(element, name, Integer.toString(value));
	}

	/**
	 * Creates an attribute with content in the form of a Boolean
	 */
	public static void setAttribute(Element element, String name, boolean value)
	{
		setAttribute(element, name, value ? "S" : "N");
	}

	/**
	 * Creates an attribute with content in the form of a double
	 */
	public static void setAttribute(Element element, String name, double value)
	{
		setAttribute(element, name, Double.toString(value));
	}


	
}
