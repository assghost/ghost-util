package com.ghostcat.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (null != bigDecimal) {
            //小数精度小于两位的设置成两位
            int oldScale = bigDecimal.scale();
            if (0 <= oldScale && 2 > oldScale) {
                jsonGenerator.writeNumber(bigDecimal.setScale(2));
            } else {
                jsonGenerator.writeNumber(bigDecimal);
            }
        } else {
            jsonGenerator.writeNull();
        }
    }
}
