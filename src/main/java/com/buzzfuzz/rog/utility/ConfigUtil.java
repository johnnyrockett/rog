package com.buzzfuzz.rog.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.buzzfuzz.rog.decisions.Config;
import com.buzzfuzz.rog.decisions.ConfigTree;
import com.buzzfuzz.rog.decisions.Constraint;
import com.buzzfuzz.rog.decisions.Target;
import com.buzzfuzz.rog.decisions.ConfigTree.Scope;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigUtil {

    public static ConfigTree createConfigFromFile(String path) {
		if (!path.isEmpty() && path != null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(path);

				ConfigTree fileConfig = new ConfigTree();

				Node xmlConfig = doc.getElementsByTagName("config").item(0);
				
				for (int i=0; i < xmlConfig.getChildNodes().getLength(); i++) {
					Node child = xmlConfig.getChildNodes().item(i);
					if (child.getNodeName().equals("scopes")) {
						evaluateScopes(child.getChildNodes(), fileConfig.getRoot());
					}
				}

				return fileConfig;
			} catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void evaluateScopes(NodeList xmlScopes, Scope configScope) {

		for (int i=0; i < xmlScopes.getLength(); i++) {
			Node xmlScope = xmlScopes.item(i);
			if (xmlScope.getNodeName().equals("scope")) {
				Scope scope = new Scope();
				NodeList schild = xmlScope.getChildNodes();
				for (int j=0; j < schild.getLength(); j++) {
					Node sAtt = schild.item(j);

					if (sAtt.getNodeName().equals("target")) {
						scope.setTarget(parseTarget(sAtt));
					} else if (sAtt.getNodeName().equals("constraint")) {
						scope.setConstraint(parseConstraint(sAtt));
					} else if (sAtt.getNodeName().equals("scopes")) {
						evaluateScopes(sAtt.getChildNodes(), scope);
					}
				}
				configScope.addChild(scope);
			}
		}
    }

    private static Target parseTarget(Node xmlTarget) {
		NodeList tAtts = xmlTarget.getChildNodes();
		Target target = new Target();

		for (int i=0; i < tAtts.getLength(); i++) {
            Node child = tAtts.item(i);
            String nodeName = child.getNodeName();
			if (nodeName.equals("instancePath")) {
				target.setInstancePath(child.getTextContent());
            } else if (nodeName.equals("typeName")) {
                target.setTypeName(child.getTextContent());
            }
			// More later
		}

		return target;
	}

	private static Constraint parseConstraint(Node xmlConstraint) {
		NodeList cAtts = xmlConstraint.getChildNodes();
		Constraint constraint = new Constraint();

		for (int i=0; i < cAtts.getLength(); i++) {
			Node child = cAtts.item(i);
			if (child.getNodeName().equals("nullProb")) {
				double value = Double.parseDouble(child.getTextContent());
				// might want to verify that it is within 0 and 1.0
				constraint.setNullProb(value);
            }
            if (child.getNodeName().equals("lowerBound")) {
                double value = Double.parseDouble(child.getTextContent());
                constraint.setLowerBound(value);
            }
            if (child.getNodeName().equals("upperBound")) {
                double value = Double.parseDouble(child.getTextContent());
                constraint.setUpperBound(value);
            }
            if (child.getNodeName().equals("negative")) {
                constraint.setNegative(child.getTextContent().toLowerCase().equals("false") || child.getTextContent().equals("0"));
            }
            if (child.getNodeName().equals("StringExamples")) {

                NodeList examples = child.getChildNodes();
                String[] stringExamples = new String[examples.getLength()];
                for (int s=0; s < examples.getLength(); s++) {
                    stringExamples[s] = examples.item(s).getTextContent();
                }
                constraint.setStringExamples(stringExamples);
            }
		}

		return constraint;
    }

    public static ConfigTree mergeNewTree(ConfigTree t1, ConfigTree t2) {
        mergeTrees(t1, t2.getRoot());
        return t1;
	}

    // TODO: I don't think that this method works how I inteded because of addPair not working without the effective target

	// Merges two trees together, overriding the first tree with the second where applicable
	private static void mergeTrees(ConfigTree t1, Scope scope) {
		if (t1 == null)
			t1 = new ConfigTree(scope);

		// iterate through target, constraint pairs and addPair continually
		if (scope.getTarget() != null && scope.getConstraint() != null)
			t1.addPair(scope.getTarget(), scope.getConstraint()); // should just have this method validate nulls
		for (Scope child : scope.getChildren()) {
			mergeTrees(t1, child);
		}
    }

    // This should really be the Engine's job to make sure that everything is still thread-safe
	public static synchronized void log(String path, Config config) {
		File corpus = Paths.get(path, "corpus").toFile();
		if (!corpus.exists())
			corpus.mkdir();
		
		File output = Paths.get(corpus.toURI().getPath(), String.valueOf(config.hashCode())).toFile();
		if (output.exists())
			output.delete();
		
		output.mkdir();
		
		// Print the log of decisions that were made
		File logger = Paths.get(output.toURI().getPath(), "log.txt").toFile();
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter(logger));
		    writer.append(config.getLog());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		// Print configuration that was used
		File configuration = Paths.get(output.getPath(), "config.xml").toFile();
		
		Document doc = config.toXML();
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			
		    StreamResult result = new StreamResult(configuration);
		    
            transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
    }

}