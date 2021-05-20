package com.ghostcat.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ghostcat.common.util.GhostDateTimeUtils;
import com.ghostcat.common.util.GhostDateUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JacksonConfig {

    /**
     * 设置jackson的一些序列化和反序列化逻辑
     * springboot的{@link ObjectMapper} 可以用{@link Jackson2ObjectMapperBuilder} 创建
     * @param objectMapper
     * @return
     */
    public static ObjectMapper configObjectMapper(ObjectMapper objectMapper) {

        SimpleModule simpleModule = new SimpleModule();
        //设置BigDecimal至少保留两位小数
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        simpleModule.addDeserializer(BigDecimal.class, new NumberDeserializers.BigDecimalDeserializer());

        objectMapper.registerModule(simpleModule);

        //设置LocalDate和LocalDateTime的默认序列化和反序列化格式
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(GhostDateTimeUtils.getDefaultDateTimeFormatter()));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(GhostDateTimeUtils.getDefaultDateTimeFormatter()));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(GhostDateUtils.getDefaultDateTimeFormatter()));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(GhostDateUtils.getDefaultDateTimeFormatter()));

        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }
}
