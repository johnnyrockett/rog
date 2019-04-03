package com.buzzfuzz.rog.decisions;

import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.buzzfuzz.rog.decisions.ConfigTree.Scope;
import com.buzzfuzz.rog.decisions.Target;

public class Config {

	ConfigTree config;
    StringBuilder log;

    Method callerMethod;

	public Config() {
        this(new ConfigTree());
    }

    public Config(ConfigTree tree) {
        config = tree;
        log = new StringBuilder();
    }

    public ConfigTree getTree() {
        return this.config;
    }

	public void log(String msg) {
		log.append(msg);
	}

	public StringBuilder getLog() {
		return log;
    }

    public void setCallerMethod(Method method) {
        log(method.toString() + '\n');
        this.callerMethod = method;
    }

    public Method getCallerMethod() {
        return this.callerMethod;
    }
	
	@Override
    public int hashCode() {
        return config.hashCode();
    }

	public void addPair(Target target, Constraint constraint) {
		config.addPair(target, constraint);
	}

	public Constraint findConstraintFor(Target target) {
		return config.findPairFor(target, config.getRoot()).x.y.y;
	}
	
	private static void setAttribute(Document doc, Element parent, String name, String value) {
		Element elem = doc.createElement(name);
	    elem.setTextContent(value);
	    parent.appendChild(elem);
	}
	
	// TODO: These should be moved out to a utility class
	private static void appendScopes(Document doc, Element elem, Scope parent) {
		
		// Add target if it exists
		if (parent.getTarget() != null) {
			Target target = parent.getTarget();
			Element xmlTarget = doc.createElement("target");
			
			if (target.getInstancePath() != null)
				setAttribute(doc, xmlTarget, "instancePath", target.getInstancePath());
			if (target.getTypeName() != null)
				setAttribute(doc, xmlTarget, "typeName", target.getTypeName());
			
			elem.appendChild(xmlTarget);
		}
		
		// Add constraint if it exists
		if (parent.getConstraint() != null) {
			Constraint constraint = parent.getConstraint();
			Element xmlConstraint = doc.createElement("constraint");
			
			if (constraint.getNullProb() != null)
				setAttribute(doc, xmlConstraint, "nullProb", constraint.getNullProb().toString());
			if (constraint.getProb() != null)
                setAttribute(doc, xmlConstraint, "prob", constraint.getProb().toString());
            if (constraint.isNegative() != null)
                setAttribute(doc, xmlConstraint, "negative", constraint.isNegative() ? "True" : "False");
            if (constraint.getLowerBound() != null)
                setAttribute(doc, xmlConstraint, "lowerBound", constraint.getLowerBound().toString());
            if (constraint.getUpperBound() != null)
                setAttribute(doc, xmlConstraint, "upperBound", constraint.getUpperBound().toString());
            if (constraint.getStringExamples() != null) {
                Element sExamples = doc.createElement("StringExamples");
                for (String example : constraint.getStringExamples()) {
                    setAttribute(doc, sExamples, "StringExample", example);
                }
                xmlConstraint.appendChild(sExamples);
            }

			elem.appendChild(xmlConstraint);
		}
			
		// Recursively add children
		if (parent.getChildren().size() > 0) {
			Element xmlScopes = doc.createElement("scopes");

			for (Scope child : parent.getChildren()) {
				Element childScope = doc.createElement("scope");
				appendScopes(doc, childScope, child);
				xmlScopes.appendChild(childScope);
			}
			
			elem.appendChild(xmlScopes);
		}
	}
	
	public Document toXML() {
		Document doc = null;
		try {
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		    // root elements
		    doc = docBuilder.newDocument();
		    Element rootElement = doc.createElement("config");
		    doc.appendChild(rootElement);
		    
		    appendScopes(doc, rootElement, config.getRoot());

		  } catch (ParserConfigurationException pce) {
		    pce.printStackTrace();
		  }
		return doc;
	}

}
