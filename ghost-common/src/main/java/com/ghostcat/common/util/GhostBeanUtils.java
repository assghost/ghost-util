package com.ghostcat.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ghostcat.common.bean.SimpleBean;
import com.ghostcat.common.jackson.JacksonConfig;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author AssGhost
 */
public class GhostBeanUtils {

    private static String CLASS_FIELD = "class";

    private static ObjectMapper OBJECT_MAPPER = buildObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }

    private static ObjectMapper buildObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        JacksonConfig.configObjectMapper(objectMapper);

        return objectMapper;
    }

    public static <T> Map<String, Object> bean2Map(T bean, Class<T> clazz) throws InvocationTargetException, IllegalAccessException {

        if (null != clazz) {

            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);

            Map<String, Object> map = new LinkedHashMap<>(propertyDescriptors.length);

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String propertyName = propertyDescriptor.getName();
                if (propertyName.equals(CLASS_FIELD)) {
                    continue;
                }

                Method readMethod = propertyDescriptor.getReadMethod();
                if (null != readMethod) {
                    Object readValue = readMethod.invoke(bean);

                    map.put(propertyName, readValue);
                }
            }

            return map;
        }

        return new LinkedHashMap(0);
    }

    public static <T> T map2Bean(Map<String, ?> map, Class<T> clazz) throws JsonProcessingException {

        if (null != map && null != clazz) {
            String json = OBJECT_MAPPER.writeValueAsString(map);
            return OBJECT_MAPPER.readValue(json, clazz);
        }

        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(10);

        map.put("name", "Ethan");
        map.put("age", "10");
        map.put("birth", "2021-05-20");
        map.put("money", "100.1");

        try {
            SimpleBean simpleBean = map2Bean(map, SimpleBean.class);
            System.out.println(getObjectMapper().writeValueAsString(simpleBean));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
