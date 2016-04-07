package ru.prime.app.validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Validator {

	private List<DataEntity> beansList = new ArrayList<DataEntity>();
	
	String fileName;



	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// TODO add map instead of string
	private Map<String, String> getConstraintValue(Node constraint) {
		Map<String, String> result = new HashMap<String, String>();
		NodeList valuesList = constraint.getChildNodes();
		for (int x = 0; x < valuesList.getLength(); x++) {
			if (valuesList.item(x).getNodeName().equals("element")) {
				// Processing value
				// Node valueNode
				NamedNodeMap list = valuesList.item(x).getAttributes();
				String type = list.getNamedItem("name").getTextContent();
				if (type.equals("regexp")) {
					String val = valuesList.item(x).getTextContent();
					result.put("regexp", val);
					System.out.println("      Constraint regexp " + val);
				} else if (type.equals("value")) {
					String val = valuesList.item(x).getTextContent();
					result.put("value", val);
					System.out.println("      Constraint value " + val);
				}if (type.equals("message")) {
					String val = valuesList.item(x).getTextContent();
					result.put("message", val);
					System.out.println("      Constraint regexp " + val);
				} else if (type.equals("inclusive")) {
					String val = valuesList.item(x).getTextContent();
					result.put("inclusive", val);
					System.out.println("      Constraint inclusive " + val);
				} else if (type.equals("min")) {
					String val = valuesList.item(x).getTextContent();
					result.put("min", val);
					System.out.println("      Constraint min " + val);
				} else if (type.equals("max")) {
					String val = valuesList.item(x).getTextContent();
					result.put("max", val);
					System.out.println("      Constraint max " + val);
				} else if (type.equals("integer")) {
					String val = valuesList.item(x).getTextContent();
					result.put("integer", val);
					System.out.println("      Constraint integer " + val);
				} else if (type.equals("fraction")) {
					String val = valuesList.item(x).getTextContent();
					result.put("fraction", val);
					System.out.println("      Constraint fraction " + val);
				} else if (type.equals("flag")) {
					String val = valuesList.item(x).getTextContent();
					result.put("flag", val);
					System.out.println("      Constraint flag " + val);
				}
			}
		}

		return result;
	}

	private Constraint processConstraint(Node constraintNode) {
		NamedNodeMap constraintAtts = constraintNode.getAttributes();
		String constraintType = constraintAtts.getNamedItem("annotation").getTextContent();
		String arr[] = constraintType.split("\\.");
		constraintType = arr.length == 0 ? constraintType : arr[arr.length - 1];
		System.out.println("    Constraint type is " + constraintType);
		String value = null;
		String regexp = null;
		String min = null;
		String max = null;
		Map<String, String> params = getConstraintValue(constraintNode);
		Constraint constraint = new Constraint();
		constraint.setMessage(params.get("message"));
		if (constraintType.equals("AssertTrue")) {
			constraint.setType(ConstraintType.ASSERT_TRUE);
		} else if (constraintType.equals("AssertFalse")) {
			constraint.setType(ConstraintType.ASSERT_FALSE);
		} else if (constraintType.equals("DecimalMax")) {
			constraint.setType(ConstraintType.DECIMAL_MAX);
			constraint.setValue(params.get("value"));
			constraint.setInclusive(params.get("inclusive"));
		} else if (constraintType.equals("DecimalMin")) {
			constraint.setType(ConstraintType.DECIMAL_MIN);
			constraint.setValue(params.get("value"));
			constraint.setInclusive(params.get("inclusive"));		
		} else if (constraintType.equals("Digits")) {
			constraint.setType(ConstraintType.DIGITS);
			constraint.setInteger(params.get("integer"));
			constraint.setFraction(params.get("fraction"));		
		} else if (constraintType.equals("Future")) {
			constraint.setType(ConstraintType.FUTURE);
		} else if (constraintType.equals("Past")) {
			constraint.setType(ConstraintType.PAST);
		} else if (constraintType.equals("Max")) {
			constraint.setType(ConstraintType.MAX);
			constraint.setValue(params.get("value"));
		} else if (constraintType.equals("Min")) {
			constraint.setType(ConstraintType.MIN);
			constraint.setValue(params.get("value"));
		} else if (constraintType.equals("NotNull")) {
			constraint.setType(ConstraintType.NOT_NULL);
		} else if (constraintType.equals("Pattern")) {
			constraint.setType(ConstraintType.PATTERN);
			constraint.setRegexp(params.get("regexp"));
			constraint.setFlag(params.get("flag"));			
		} else if (constraintType.equals("Size")) {
			constraint.setType(ConstraintType.SIZE);
			constraint.setMin(params.get("min"));
			constraint.setMin(params.get("max"));
		} else if (constraintType.equals("Unique")) {
			constraint.setType(ConstraintType.UNIQUE);
		}
		/*
		 * http://docs.jboss.org/hibernate/stable/validator/reference/en-US/
		 * html_single/#validator-defineconstraints-spec AssertTrue AssertFalse
		 * DecimalMax(value=,inclusive=) 
		 * DecimalMin(value=,inclusive=)
		 * Digits(integer=,fraction=) (number of digits) 
		 * Future - in future from
		 * now Past - in past from now 
		 * Max(value=) 
		 * Min(value=) 
		 * NotNull Null
		 * Pattern(regex=,flag=) 
		 * Size(min=, max=)
		 */
		return constraint;

	}

	private Field processField(Node fieldNode) {
		Field result = null;
		NamedNodeMap fieldAtts = fieldNode.getAttributes();
		String fieldName = fieldAtts.getNamedItem("name").getTextContent();
		System.out.println("  Processing field " + fieldName);
		NodeList constraintsList = fieldNode.getChildNodes();
		result = new Field();
		for (int i = 0; i < constraintsList.getLength(); i++) {
			if (constraintsList.item(i).getNodeName().equals("constraint")) {
				result.addConstraint(processConstraint(constraintsList.item(i)));
			}
		}
		return result;

	}

	private DataEntity processBean(Node beanNode) {
		DataEntity result = new DataEntity();
		NamedNodeMap beanAtts = beanNode.getAttributes();
		if (beanAtts != null)
			System.out.println("Parse bean " + beanAtts.getNamedItem("class"));
		NodeList fieldsList = beanNode.getChildNodes();
		for (int i = 0; i < fieldsList.getLength(); i++) {
			if (fieldsList.item(i).getNodeName().equals("field")) {
				result.addField(processField(fieldsList.item(i)));
			}
		}
		return result;

	}

	public List<DataEntity> parse() throws ParserConfigurationException, SAXException, IOException {
		List<DataEntity> result = new ArrayList<DataEntity>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(fileName));
		NodeList topList = doc.getChildNodes();
		for (int i = 0; i < topList.getLength(); i++) {
			Node topNode = topList.item(i);
			if (topNode.getNodeType() == Node.ELEMENT_NODE) {
				// if (nd.getNodeName().equals(""))
				// return subnode;

				NodeList beansList = topNode.getChildNodes();
				for (int j = 0; j < beansList.getLength(); j++) {
					Node beanNode = beansList.item(j);
					if (beanNode.getNodeName().equals("bean")) {
						result.add(processBean(beanNode));
					}
				}
			}
		}
		beansList.addAll(result);
		return result;

	}

	public boolean validateField(String entityName, String valueName, String value){
		
		
		return false;
	}
	
	
	// TODO remove this. Just for debugging.
	public void run() {
		try {
			List<DataEntity> list = parse();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
