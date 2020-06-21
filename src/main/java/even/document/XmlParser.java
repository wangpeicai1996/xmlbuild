package even.document;

import even.enetity.MapperConfiguration;
import even.enetity.Select;
import org.apache.ibatis.builder.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class XmlParser {

    private boolean validation;
    private EntityResolver entityResolver;
    private Properties variables;
    private XPath xpath;
    private InputStream inputStream;
    private MapperConfiguration mapperConfiguration;
    private Document doc;

    public XmlParser(MapperConfiguration mapperConfiguration, boolean validation, Properties variables, EntityResolver entityResolver, InputStream inputStream) {
        this.validation = validation;
        this.entityResolver = entityResolver;
        this.variables = variables;
        //获取xpath对象，用于解析xpath表达式
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
        this.inputStream = inputStream;
        this.mapperConfiguration = mapperConfiguration;
    }

    public Document createDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validation);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            //开始生成document对象
            return builder.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回单个节点
     * @param root
     * @param expression
     * @return
     */
    public Node evalNode(Object root, String expression) {
        try {
            Node node = (Node) xpath.evaluate(expression, root, XPathConstants.NODE);
            if (node == null) {
                return null;
            }
            return node;
        } catch (Exception e) {
            throw new BuilderException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

    /**
     * 返回节点列表
     * @param root
     * @param expression
     * @return
     */
    public NodeList evalNodes(Object root, String expression) {
        try {
            NodeList nodeList = (NodeList) xpath.evaluate(expression, root, XPathConstants.NODESET);
            if (nodeList == null) {
                return null;
            }
            return nodeList;
        } catch (Exception e) {
            throw new BuilderException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

    public void parse() {
        doc = this.createDocument();
        Node mapper = this.evalNode(doc,"/mapper");
        parseSelect(this.evalNodes(mapper, "select"));
    }

    private void parseSelect(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node no = nodeList.item(i);
            if (no.getNodeName().equals("select")) {
                Select select = new Select();
                NamedNodeMap attributes = no.getAttributes();
                String id = attributes.getNamedItem("id").getNodeValue();
                select.setId(id);
                select.setResultType(attributes.getNamedItem("resultType").getNodeValue());
                select.setSql(no.getTextContent().trim());
                Map<String, Select> selectMap = mapperConfiguration.getSelectmap();
                selectMap.put(id, select);
            }
        }
    }


}
