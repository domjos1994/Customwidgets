package de.domjos.customwidgets.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLWriter {
    private String path;
    private Source xslt;
    private Document document;
    private Element root;
    private Element subElement;

    public XMLWriter(String path) throws Exception {
        this.path = path;
        this.xslt = null;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.newDocument();
        this.root = this.document.createElement("export");
        this.document.appendChild(this.root);
    }

    public void addToRoot(String name, Map<String, String> attributes, String value) {
        this.subElement = this.document.createElement(name);
        this.root.appendChild(this.subElement);
        for(Map.Entry<String, String> attribute : attributes.entrySet()) {
            this.subElement.setAttribute(attribute.getKey(), attribute.getValue());
        }
        if(value != null) {
            if(!value.trim().isEmpty()) {
                this.subElement.setNodeValue(value);
            }
        }
    }

    public void addToSubElement(String name, Map<String, String> attributes, String value) {
        Element tmp = this.document.createElement(name);
        this.subElement.appendChild(tmp);
        this.subElement = tmp;
        for(Map.Entry<String, String> attribute : attributes.entrySet()) {
            this.subElement.setAttribute(attribute.getKey(), attribute.getValue());
        }
        if(value != null) {
            if(!value.trim().isEmpty()) {
                this.subElement.setNodeValue(value);
            }
        }
    }

    public void transformXML(String xsltPath) {
        File file = new File(xsltPath);

        if(file.exists()) {
            this.xslt = new StreamSource(file);
        }
    }

    public void close() throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer;
        if(this.xslt!=null) {
            transformer = transformerFactory.newTransformer(this.xslt);
        } else {
            transformer = transformerFactory.newTransformer();
        }
        DOMSource source = new DOMSource(this.document);
        StreamResult result = new StreamResult(this.path);
        transformer.transform(source, result);
    }
}
