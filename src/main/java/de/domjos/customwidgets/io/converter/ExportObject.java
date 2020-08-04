package de.domjos.customwidgets.io.converter;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ExportObject {
    private Map<String, Object> properties;

    public ExportObject() {
        this.properties = new LinkedHashMap<>();
    }

    public void addProperty(String key, Object object) {
        this.properties.put(key, object);
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
