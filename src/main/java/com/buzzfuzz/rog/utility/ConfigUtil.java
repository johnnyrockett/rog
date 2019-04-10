package com.buzzfuzz.rog.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.buzzfuzz.rog.decisions.Config;
import com.buzzfuzz.rog.decisions.ConfigTree;
import com.buzzfuzz.rog.decisions.Constraint;
import com.buzzfuzz.rog.decisions.Scope;
import com.buzzfuzz.rog.decisions.Choice;
import com.buzzfuzz.rog.decisions.Target;

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

    public static Config createConfigFromFile(String path) {
		if (!path.isEmpty() && path != null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				Document doc = builder.parse(path);

				Config config = new Config();

				Node xmlConfig = doc.getElementsByTagName("config").item(0);
				
				for (int i=0; i < xmlConfig.getChildNodes().getLength(); i++) {
					Node child = xmlConfig.getChildNodes().item(i);
					if (child.getNodeName().equals("scopes")) {
						evaluateScopes(child.getChildNodes(), config.getTree().getRoot());
                    } else if (child.getNodeName().equals("choices")) {
                        List<Choice> choices = evaluateChoices(child.getChildNodes());
                        config.setChoices(choices);
                    }
				}

				return config;
			} catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Choice> evaluateChoices(NodeList xmlChoices) {

        List<Choice> choices = new ArrayList<Choice>();

        for (int c=0; c < xmlChoices.getLength(); c++) {
            Node xmlChoice = xmlChoices.item(c);
            if (xmlChoice.getNodeName().equals("choice")) {
                Choice choice = new Choice();
                NodeList details = xmlChoice.getChildNodes();
                for (int d=0; d < details.getLength(); d++) {
                    Node choiceDetail = details.item(d);

                    if (choiceDetail.getNodeName().equals("target")) {
                        // System.out.println("parsing choice target");
                        choice.setTarget(parseTarget(choiceDetail));
                    } else if (choiceDetail.getNodeName().equals("INT")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        try {
                            choice.setValue(Integer.parseInt(value));
                        } catch( Exception e) {
                            // eat it
                        }
                    } else if (choiceDetail.getNodeName().equals("LONG")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        try {
                            choice.setValue(Long.parseLong(value));
                        } catch( Exception e) {
                            // eat it
                        }
                    } else if (choiceDetail.getNodeName().equals("CHAR")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue(value.charAt(0));
                    } else if (choiceDetail.getNodeName().equals("FLOAT")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        try {
                            choice.setValue(Float.parseFloat(value));
                        } catch( Exception e) {
                            // eat it
                        }
                    } else if (choiceDetail.getNodeName().equals("DOUBLE")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        try {
                            choice.setValue(Double.parseDouble(value));
                        } catch( Exception e) {
                            // eat it
                        }
                    } else if (choiceDetail.getNodeName().equals("BOOL")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue(Boolean.parseBoolean(value));
                    } else if (choiceDetail.getNodeName().equals("BYTE")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue(Byte.parseByte(value));
                    } else if (choiceDetail.getNodeName().equals("SHORT")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue(Short.parseShort(value));
                    } else if (choiceDetail.getNodeName().equals("STRING")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue(value);
                    } else if (choiceDetail.getNodeName().equals("ENUM")) {
                        String value = choiceDetail.getAttributes().getNamedItem("value").getNodeValue();
                        choice.setValue("enum", Integer.parseInt(value));
                    }
                }
                choices.add(choice);
            }
        }
        return choices;
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

    public static void minimize(ConfigTree tree) {
        // for each set of scope children, I should see where their targets overlap
        // and create a new super scope that fits the super scopes as children
        // Solving this involves finding the greatest common factor of a target
        minimize(tree.getRoot().getChildren());
    }

    public static void minimize(List<Scope> scopes) {
        if (scopes == null || scopes.isEmpty())
            return;
        List<Scope> children = new ArrayList<Scope>(scopes);
        Collections.shuffle(children);

        // find the smallest multiple that still works and make nested
        Target multiple = null;
        for (Scope child : children) {
            Target intersection = findIntersection(multiple, child.getTarget());
            if (!Target.isEmpty(intersection))
                multiple = intersection;
        }

        // Add every scope that the multiple applies to to a new scope as children
        List<Scope> nextGen = new ArrayList<Scope>();
        for (Scope child : children) {
            if (Target.validateContext(multiple, child.getTarget())) {
                nextGen.add(child);
            }
        }

        // if (multiple.getInstancePath() != null)
        //     System.out.println(multiple.getInstancePath() + ", " + nextGen.size());

        if (nextGen.size() > 2) {   // put them in a nested scope
            for (Scope child : nextGen) {
                scopes.remove(child);
                if (child.getTarget() != null) // not sure how this would ever happen... but it does apparently?
                    child.getTarget().subtract(multiple);
                // child.setConstraint(new Constraint()); // just to give an insentive to do nesting
            }
            Scope parent = new Scope(nextGen);
            parent.setTarget(multiple);
            parent.setConstraint(new Constraint());
            scopes.add(parent);
        }

        // minimize all of the children as well
        // for (Scope child : children) {
        //     minimize(child.getChildren());
        // }
    }

    // NOTE: if t1 is null, default to t2
    public static Target findIntersection(Target t1, Target t2) {
        if (t1 == null)
            return Target.clone(t2);
        if (t2 == null)
            return t1.clone();

        Target intersection = new Target();

        if (t1.getInstancePath() != null && t2.getInstancePath() != null) {
            String[] ip1 = t1.getInstancePath().split(".");
            String[] ip2 = t2.getInstancePath().split(".");
            if (ip1.length != 0 && ip2.length != 0) {
                int count = 0;
                for (; count < ip1.length; count++) {
                    if (!ip1[count].equals(ip2[count]))
                    break;
                }
                if (count > 0) {
                    intersection.setInstancePath(String.join(".", Arrays.copyOf(ip1, count)));
                }
            }
        } if (t1.getTypeName() != null && t2.getTypeName() != null && t1.getTypeName().equals(t2.getTypeName())) {
            intersection.setTypeName(t1.getTypeName());
        }
        // As more is added to Target, intersection logic will need to go here
        return intersection;
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

        int hash = config.hashCode();
		File output = Paths.get(corpus.toURI().getPath(), String.valueOf(hash)).toFile();
		if (output.exists()) {
            output.delete();
        }
		
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
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
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