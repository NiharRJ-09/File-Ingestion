package com.example.file_ingestion_system.service.transformation.transformers;

import com.example.file_ingestion_system.service.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JoinTransformation implements Transformation {

    @Override
    public String getTransformType() {
        return "JOIN";
    }

    @Override
    public void transform(Map<String, Object> data, Map<String, Object> config) {
        // Not applicable to single records
        // This transformation operates on the entire dataset
    }

    @Override
    public byte[] getTransformConfig() {
        return new byte[0];
    }

    public List<Map<String, Object>> joinDatasets(
            List<Map<String, Object>> primaryData,
            List<Map<String, Object>> secondaryData,
            Map<String, Object> config) {

        String primaryKey = (String) config.get("primaryKey");
        String secondaryKey = (String) config.get("secondaryKey");
        String joinType = (String) config.get("joinType");

        // Create a lookup map for the secondary dataset
        Map<Object, Map<String, Object>> secondaryLookup = secondaryData.stream()
                .collect(Collectors.toMap(
                        record -> record.get(secondaryKey),
                        record -> record,
                        (r1, r2) -> r1 // In case of duplicate keys, keep the first one
                ));

        // Perform the join operation
        List<Map<String, Object>> joinedData = new ArrayList<>();

        for (Map<String, Object> primaryRecord : primaryData) {
            Object primaryKeyValue = primaryRecord.get(primaryKey);
            Map<String, Object> secondaryRecord = secondaryLookup.get(primaryKeyValue);

            if (secondaryRecord != null) {
                // Match found, merge the records
                Map<String, Object> mergedRecord = new HashMap<>(primaryRecord);

                // Add secondary fields with prefix to avoid name conflicts
                for (Map.Entry<String, Object> entry : secondaryRecord.entrySet()) {
                    if (!entry.getKey().equals(secondaryKey)) {
                        String newKey = config.containsKey("prefix") ?
                                config.get("prefix") + "_" + entry.getKey() :
                                entry.getKey();
                        mergedRecord.put(newKey, entry.getValue());
                    }
                }

                joinedData.add(mergedRecord);
            } else if (!"INNER".equals(joinType)) {
                // For LEFT JOIN, include the primary record
                joinedData.add(new HashMap<>(primaryRecord));
            }
        }

        return joinedData;
    }
}
