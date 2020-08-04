package de.domjos.customwidgets.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLReader {
    private Document document;
    private Node root;

    public XMLReader(String path) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.parse(new File(path));
        this.root = this.document.getElementsByTagName("export").item(0);
    }

    public List<Node> getElements() {
        List<Node> elements = new LinkedList<>();
        NodeList nl = this.root.getChildNodes();
        for(int i = 0; i<=nl.getLength()-1; i++) {
            elements.add(nl.item(i));
        }
        return elements;
    }

    public void close() {
        this.document = null;
    }
}
