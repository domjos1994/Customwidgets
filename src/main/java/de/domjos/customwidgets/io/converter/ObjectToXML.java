package de.domjos.customwidgets.io.converter;

import android.content.Context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.domjos.customwidgets.io.XMLWriter;

public final class ObjectToXML extends ObjectToFile {
    private String xslt;
    private XMLWriter writer;

    public ObjectToXML(List<Object> objects, String path, Context context, Object... params) {
        super(objects, path, context, params);

        this.xslt = "";
        if(params.length > 0) {
            this.xslt = (String) params[0];
        }
    }

    @Override
    protected void openFile() throws Exception {
        this.writer = new XMLWriter(this.path);
        if(!this.xslt.trim().isEmpty()) {
            this.writer.transformXML(this.xslt);
        }
    }

    @Override
    protected void writeObject(ExportObject exportObject) {
        this.writeExportObjectToTable(null, exportObject);
    }

    private void writeExportObjectToTable(String name, ExportObject exportObject) {
        Map<String, ExportObject> complicatedObjects = new LinkedHashMap<>();
        Map<String, String> simpleObjects = new LinkedHashMap<>();

        for(Map.Entry<String, Object> entry : exportObject.getProperties().entrySet()) {
            if(entry.getValue() instanceof ExportObject) {
                complicatedObjects.put(entry.getKey(), (ExportObject) entry.getValue());
            } else if(entry.getValue() instanceof List) {
                StringBuilder items = new StringBuilder();
                List<?> ls = (List<?>) entry.getValue();
                for(Object item : ls) {
                    items.append(item).append(", ");
                }
                simpleObjects.put(entry.getKey(), items.toString());
            } else {
                simpleObjects.put(entry.getKey(), entry.getValue().toString());
            }
        }

        if(name == null) {
            this.writer.addToRoot("element", simpleObjects, "");
        } else {
            this.writer.addToSubElement(name, simpleObjects, "");
        }

        for(Map.Entry<String, ExportObject> entry : complicatedObjects.entrySet()) {
            this.writeExportObjectToTable(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void closeFile() throws Exception {
        this.writer.close();
    }
}
