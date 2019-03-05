package com.buzzfuzz.rog.utility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.buzzfuzz.rog.decisions.ConfigTree;
import com.buzzfuzz.rog.decisions.Constraint;
import com.buzzfuzz.rog.decisions.Target;
import com.buzzfuzz.rog.decisions.ConfigTree.Scope;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigUtil {

    public static ConfigTree addConfigFile(String path) {
		if (!path.isEmpty() && path != null) {
			try {
				DocumentBuilderFactory factory =	DocumentBuilderFactory.newInstance();
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
			if (child.getNodeName().equals("instancePath")) {
				target.setInstancePath(child.getTextContent());
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
		}
		
		return constraint;
    }

    public static ConfigTree mergeNewTree(ConfigTree t1, ConfigTree t2) {
        mergeTrees(t1, t2.getRoot());
        return t1;
	}

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

}