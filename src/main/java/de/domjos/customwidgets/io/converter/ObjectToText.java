package de.domjos.customwidgets.io.converter;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.domjos.customwidgets.io.TextWriter;

public final class ObjectToText extends ObjectToFile {
    private TextWriter textWriter;
    private boolean noLines;

    public ObjectToText(List<Object> objects, String path, Context context, Object... params) {
        super(objects, path, context, params);
    }

    @Override
    protected void openFile() throws Exception {
        this.textWriter = new TextWriter(super.path);
        this.noLines = true;
    }

    @Override
    protected void writeObject(ExportObject exportObject) {
        if(this.noLines) {
            this.textWriter.writeLine(exportObject.getProperties().keySet().toArray(new String[]{}));
            this.noLines = false;
        }

        List<String> values = new LinkedList<>();
        for(Object value : exportObject.getProperties().values()) {
            if(value instanceof ExportObject) {
                StringBuilder subItem = new StringBuilder("(");
                for(Map.Entry<String, Object> sub : ((ExportObject) value).getProperties().entrySet()) {
                    subItem.append(sub.getKey()).append(":").append(sub.getValue().toString()).append("|");
                }
                subItem.append(")");
                values.add(subItem.toString());
            } else if(value instanceof List<?>) {
                StringBuilder subItem = new StringBuilder("(");
                for(Object subValue : ((List<?>) value)) {
                    subItem.append(subValue.toString()).append("|");
                }
                subItem.append(")");
                values.add(subItem.toString());
            } else {
                values.add(value.toString());
            }
        }
        this.textWriter.writeLine(values.toArray(new String[]{}));
    }

    @Override
    protected void closeFile() throws Exception {
        this.textWriter.closeWriter();
    }
}
