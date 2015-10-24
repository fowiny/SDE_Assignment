
/**
 * Based on code made by Festo Owiny
 */
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PeopleReader {

    Document doc;
    XPath xpath;

    public void loadXML() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        doc = builder.parse("people.xml");

        //creating xpath object
        getXPathObj();
    	}

   	 public XPath getXPathObj() {

    	    XPathFactory factory = XPathFactory.newInstance();
    	    xpath = factory.newXPath();
	    return xpath;
    	}

	//Method to return weight of person with corresponding person ID
    	public Node getWeightById(String personID) throws XPathExpressionException {

    	    XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile/weight");
    	    Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
    	    return node;
    	}

	//Method to return height of person with corresponding person ID
   	public Node getHeightById(String personID) throws XPathExpressionException {

   	   XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile/height");
       	   Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
           return node;
        }

	//Function that prints all people in the list with detail
    	public NodeList getAllPerson() throws XPathExpressionException {

        XPathExpression expr = xpath.compile("/people/person");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;

    	}

	//Function that accepts id as parameter and prints the HealthProfile of the person with that id	
	public Node getHealthProfileById(String personID) throws XPathExpressionException {

        XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile");
        Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return node;
   	 }

	//Function which accepts weight and operator as parameters and prints people with the condition
	public NodeList getPersonWithCondition(String weight, String condition) throws XPathExpressionException {

        XPathExpression expr = xpath.compile("//child::healthprofile[weight " + condition + "'" + weight + "']");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;
   	 }

   	 public static void main(String[] args) throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {

		PeopleReader p = new PeopleReader();
		p.loadXML();

		System.out.println("All people:");
		NodeList personNodes=p.getAllPerson();
		for (int i = 0; i < personNodes.getLength(); i++) {
			System.out.println(personNodes.item(i).getTextContent());
		    }
	
		System.out.println("Health profile of person with Id=5:");
		Node healthProfileNode=p.getHealthProfileById("0005"); 
		System.out.println(healthProfileNode.getTextContent());
	

		System.out.println("People with weight > 90: ");
		NodeList conditionNodes=p.getPersonWithCondition("90",">");
		for (int i = 0; i < conditionNodes.getLength(); i++) {
			System.out.println(conditionNodes.item(i).getTextContent());
		    }

    }
}
