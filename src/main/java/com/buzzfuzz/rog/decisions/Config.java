package com.buzzfuzz.rog.decisions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.buzzfuzz.rog.decisions.Target;

public class Config {

    ConfigTree config;
    List<Choice> choices;
    StringBuilder log;

    Method callerMethod;

	public Config() {
        this(new ConfigTree());
    }

    public Config(ConfigTree tree) {
        config = tree;
        log = new StringBuilder();
        this.choices = new ArrayList<Choice>();
    }

    public ConfigTree getTree() {
        return this.config;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Choice> getChoices() {
        return this.choices;
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
        return config.hashCode() + choices.hashCode();
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

    private static void appendTarget(Document doc, Element elem, Target target) {
        if (target != null) {
			Element xmlTarget = doc.createElement("target");

			if (target.getInstancePath() != null)
				setAttribute(doc, xmlTarget, "instancePath", target.getInstancePath());
			if (target.getTypeName() != null)
				setAttribute(doc, xmlTarget, "typeName", target.getTypeName());

			elem.appendChild(xmlTarget);
		}
    }

	// TODO: These should be moved out to a utility class
	private static void appendScopes(Document doc, Element elem, Scope parent) {

        // Add target if it exists
        appendTarget(doc, elem, parent.getTarget());
		
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

    private static void appendChoices(Document doc, Element elem, List<Choice> choices) {
        Element choicesElement = doc.createElement("choices");
        elem.appendChild(choicesElement);
        for (Choice choice : choices) {
            Element choiceElem = doc.createElement("choice");

            appendTarget(doc, choiceElem, choice.getTarget());

            Element type = doc.createElement(choice.getChoiceType());
            type.setAttribute("value", choice.getValueString());
            choiceElem.appendChild(type);

            choicesElement.appendChild(choiceElem);
        }
    }

    public Config clone() {
        Config clone = new Config(this.getTree().clone());
        clone.setCallerMethod(this.callerMethod);
        return clone;
    }

	public Document toXML() {
		Document doc = null;
		try {
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		    // root elements
		    doc = docBuilder.newDocument();
		    Element configElement = doc.createElement("config");
		    doc.appendChild(configElement);
		    
            appendScopes(doc, configElement, config.getRoot());

            appendChoices(doc, configElement, this.getChoices());

		  } catch (ParserConfigurationException pce) {
		    pce.printStackTrace();
		  }
		return doc;
	}

}
