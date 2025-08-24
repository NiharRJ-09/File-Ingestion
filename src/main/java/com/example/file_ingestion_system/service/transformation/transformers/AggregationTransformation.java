package com.example.file_ingestion_system.service.transformation.transformers;

import com.example.file_ingestion_system.service.transformation.Transformation;
import org.springframework.stereotype.Component;

import java.util.*;

import java.util.stream.Collectors;
import java.math.BigDecimal;


@Component
public class AggregationTransformation implements Transformation {

    @Override
    public String getTransformType() {
        return "AGGREGATE";
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

    public List<Map<String, Object>> aggregateData(
            List<Map<String, Object>> data,
            Map<String, Object> config) {

        List<String> groupByFields = (List<String>) config.get("groupByFields");
        List<Map<String, Object>> aggregations = (List<Map<String, Object>>) config.get("aggregations");

        // Group the data by the specified fields
        Map<List<Object>, List<Map<String, Object>>> groupedData = data.stream()
                .collect(Collectors.groupingBy(record -> {
                    List<Object> groupingKey = new ArrayList<>();
                    for (String field : groupByFields) {
                        groupingKey.add(record.get(field));
                    }
                    return groupingKey;
                }));

        // Perform aggregations on each group
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<List<Object>, List<Map<String, Object>>> entry : groupedData.entrySet()) {
            Map<String, Object> aggregatedRecord = new HashMap<>();

            // Add grouping keys to the record
            for (int i = 0; i < groupByFields.size(); i++) {
                aggregatedRecord.put(groupByFields.get(i), entry.getKey().get(i));
            }

            // Apply aggregation functions
            List<Map<String, Object>> groupRecords = entry.getValue();
            for (Map<String, Object> agg : aggregations) {
                String type = (String) agg.get("type");
                String sourceField = (String) agg.get("sourceField");
                String targetField = (String) agg.get("targetField");

                switch (type) {
                    case "SUM":
                        aggregatedRecord.put(targetField,
                                calculateSum(groupRecords, sourceField));
                        break;
                    case "AVG":
                        aggregatedRecord.put(targetField,
                                calculateAverage(groupRecords, sourceField));
                        break;
                    case "COUNT":
                        aggregatedRecord.put(targetField, groupRecords.size());
                        break;
                    case "MIN":
                        aggregatedRecord.put(targetField,
                                calculateMin(groupRecords, sourceField));
                        break;
                    case "MAX":
                        aggregatedRecord.put(targetField,
                                calculateMax(groupRecords, sourceField));
                        break;
                }
            }

            result.add(aggregatedRecord);
        }

        return result;
    }

    private BigDecimal calculateSum(List<Map<String, Object>> records, String field) {
        return records.stream()
                .map(record -> convertToBigDecimal(record.get(field)))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAverage(List<Map<String, Object>> records, String field) {
        List<BigDecimal> values = records.stream()
                .map(record -> convertToBigDecimal(record.get(field)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(values.size()), BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateMin(List<Map<String, Object>> records, String field) {
        return records.stream()
                .map(record -> convertToBigDecimal(record.get(field)))
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateMax(List<Map<String, Object>> records, String field) {
        return records.stream()
                .map(record -> convertToBigDecimal(record.get(field)))
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(((Number) value).toString());
        } else {
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
