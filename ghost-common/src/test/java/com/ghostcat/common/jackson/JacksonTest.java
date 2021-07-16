package com.ghostcat.common.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghostcat.common.bean.SimpleBean;

import java.util.HashMap;
import java.util.Map;

public class JacksonTest {

    public static void jsonToMap(String[] args) {
        Map<String, Object> iMap = new HashMap<>();
        //iMap.put("user", "ahahaha");

        SimpleBean sb = new SimpleBean();
        sb.setAge(18);
        sb.setName("wahahahahaha");
        iMap.put("sb", sb);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writeValueAsString(iMap);

            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, SimpleBean.class);
            Map rMap = objectMapper.readValue(json, javaType);
            System.out.println(rMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
