package com.example.file_ingestion_system.service.transformation.transformers;

import com.example.file_ingestion_system.service.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RenameFieldTransformation implements Transformation {

    @Override
    public String getTransformType() {
        return "RENAME_FIELD";
    }

    @Override
    public void transform(Map<String, Object> data, Map<String, Object> config) {
        String sourceField = (String) config.get("sourceField");
        String targetField = (String) config.get("targetField");

        if (data.containsKey(sourceField)) {
            Object value = data.get(sourceField);
            data.put(targetField, value);
            data.remove(sourceField);
        }
    }

    @Override
    public byte[] getTransformConfig() {
        return new byte[0];
    }
}
