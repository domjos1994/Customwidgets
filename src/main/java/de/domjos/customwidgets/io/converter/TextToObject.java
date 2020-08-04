package de.domjos.customwidgets.io.converter;

import android.content.Context;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.domjos.customwidgets.io.TextReader;

public final class TextToObject<T> extends FileToObject<T> {
    private TextReader textReader;


    public TextToObject(Class<T> cls, String path, Context context) {
        super(cls, path, context);
    }

    @Override
    protected void openFile() throws Exception {
        this.textReader = new TextReader(super.path);
    }

    @Override
    protected List<T> getObjects() throws Exception {
        List<Map<String, String>> content = this.textReader.readFile();
        List<T> exportObjects = new LinkedList<>();

        for(Map<String, String> row : content) {
            T item = super.cls.newInstance();
            for(Map.Entry<String, String> entry : row.entrySet()) {
                if(entry.getValue().trim().startsWith("(") && entry.getValue().trim().endsWith(")")) {
                    Field field = FieldUtils.getField(cls, entry.getKey(), true);
                    this.setField(super.cls, entry.getKey(), this.getSubElement(entry.getValue().trim(), field), item);
                } else {
                    this.setField(super.cls, entry.getKey(), entry.getValue(), item);
                }
            }
            exportObjects.add(item);
        }

        return exportObjects;
    }

    private Object getSubElement(String item, Field field) throws Exception {
        if(Objects.requireNonNull(field.getType()) != Objects.requireNonNull(Object.class)) {
            Object exportObject = Objects.requireNonNull(field.getType()).newInstance();
            for(String subItem : item.split("\\|")) {
                if(!subItem.trim().isEmpty()) {
                    if(subItem.trim().contains(":")) {
                        this.setField(field.getType(), subItem.split(":")[0].trim(), subItem.split(":")[1].trim(), exportObject);
                    }
                }
            }
            return exportObject;
        } else {
            Map<String, Object> data = new LinkedHashMap<>();
            for(String subItem : item.split("\\|")) {
                if(!subItem.trim().isEmpty()) {
                    if(subItem.trim().contains(":")) {
                        data.put(subItem.split(":")[0].trim(), subItem.split(":")[1].trim());
                    }
                }
            }
            return data;
        }
    }

    @Override
    protected void closeFile() {
        this.textReader = null;
    }
}
