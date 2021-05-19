package com.ghostcat.poi.util;

import com.ghostcat.poi.annotations.ExportCol;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExportColParser {
    /**
     * 读取实体类字段与模板表头的关系
     * @param clazz
     * @return
     */
    public static Map<String, String> parseAntExportCol(Class clazz) {
        Map<String, String> fieldMap = new LinkedHashMap<>();

        if (null != clazz) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                ExportCol annotation = field.getAnnotation(ExportCol.class);
                if (null != annotation) {
                    String colName = annotation.value();
                    String fieldName = field.getName();
                    fieldMap.put(colName, fieldName);
                }
            }
        }

        return fieldMap;
    }
}
