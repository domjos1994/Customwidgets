package de.domjos.customwidgets.io.converter;

import android.content.Context;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.domjos.customwidgets.io.XMLReader;

public final class XMLToObject<T> extends FileToObject<T> {
    private XMLReader reader;

    public XMLToObject(Class<T> cls, String path, Context context) {
        super(cls, path, context);
    }

    @Override
    protected void openFile() throws Exception {
        this.reader = new XMLReader(this.path);
    }

    @Override
    protected List<T> getObjects() throws Exception {
        List<T> exportObjects = new LinkedList<>();

        for(Node node : this.reader.getElements()) {
            T item = super.cls.newInstance();
            for(int i = 0; i<=node.getAttributes().getLength() - 1; i++) {
                Node subNode = node.getAttributes().item(i);
                this.setField(super.cls, subNode.getNodeName(), subNode.getTextContent(), item);
            }
            for(int i = 0; i<=node.getChildNodes().getLength() - 1; i++) {
                Node subNode = node.getChildNodes().item(i);
                Field field = FieldUtils.getField(cls, subNode.getNodeName(), true);
                this.setField(super.cls, subNode.getNodeName(), this.getSubElement(subNode, field), item);
            }
            exportObjects.add(item);
        }

        return exportObjects;
    }

    private Object getSubElement(Node node, Field field) throws Exception {
        if(Objects.requireNonNull(field.getType()) != Objects.requireNonNull(Object.class)) {
            Object exportObject = Objects.requireNonNull(field.getType()).newInstance();
            for(int i = 0; i<=node.getAttributes().getLength()-1; i++) {
                Node item = node.getAttributes().item(i);
                this.setField(field.getType(), item.getNodeName(), item.getTextContent(), exportObject);
            }
            for(int i = 0; i<=node.getChildNodes().getLength() - 1; i++)  {
                Element sub = (Element) node.getChildNodes().item(i);
                this.setField(field.getType(), sub.getTagName(), this.getSubElement(sub, field), exportObject);
            }
            return exportObject;
        } else {
            Map<String, Object> data = new LinkedHashMap<>();
            for(int i = 0; i<=node.getAttributes().getLength()-1; i++) {
                Node item = node.getAttributes().item(i);
                data.put(item.getNodeName(), item.getTextContent());
            }
            for(int i = 0; i<=node.getChildNodes().getLength() - 1; i++)  {
                Node sub = node.getChildNodes().item(i);
                data.put(sub.getNodeName(), this.getSubElement(sub, field));
            }
            return data;
        }
    }

    @Override
    protected void closeFile() {
        this.reader.close();
    }
}
