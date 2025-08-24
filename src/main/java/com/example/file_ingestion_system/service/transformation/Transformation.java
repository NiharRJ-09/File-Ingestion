package com.example.file_ingestion_system.service.transformation;

import java.util.Map;

public interface Transformation {
    String getTransformType();
    void transform(Map<String, Object> data, Map<String, Object> config);

    byte[] getTransformConfig();
}
