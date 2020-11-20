package com.jc.soda;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.json.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Slf4j
public class JsonHelper {

    public final ObjectMapper objectMapper;

    private static final JsonHelper instance = new JsonHelper();

    private JsonHelper() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
            ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTimeDeserializer localDateTimeDeserializer = LocalDateTimeDeserializer.INSTANCE;
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        this.objectMapper = Jackson2ObjectMapperBuilder.json()
            .modules(module)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    }

    public static JsonHelper getInstance() {
        return instance;
    }

    public JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while converting String to JsonNode: ", e);
            throw new ApplicationException(
                content + " failed to convert to JsonNode.", e);
        }
    }

    public <T> T fromString(JsonNode content, Class<T> cls) {
        try {
            return objectMapper.readValue(content.toString(), cls);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while converting String to class: ", e);
            throw new ApplicationException(
                content + " failed to convert to object.", e);
        }
    }

    public <T> T fromString(String content, Class<T> cls) {
        try {
            return objectMapper.readValue(content.toString(), cls);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while converting String to class: ", e);
            throw new ApplicationException(
                content + " failed to convert to object.", e);
        }
    }

    public String toString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred while converting Object to String: ", e);
            throw new ApplicationException(
                value + " failed to convert to String.", e);
        }
    }

}
