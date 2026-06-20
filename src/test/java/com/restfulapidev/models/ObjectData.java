package com.restfulapidev.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Представляет произвольное поле "data" объекта restful-api.dev.
 * API намеренно не имеет фиксированной схемы для "data" — это может быть
 * любой набор ключ-значение (цена, цвет, год выпуска и т.д.) или null.
 * Поэтому вместо жёстких полей используем динамическую Map с @JsonAnySetter/@JsonAnyGetter —
 * это позволяет работать с любым набором атрибутов товара без потери данных при сериализации.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectData {

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    @JsonAnySetter
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object get(String key) {
        return attributes.get(key);
    }

    public boolean has(String key) {
        return attributes.containsKey(key);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public String toString() {
        return "ObjectData" + attributes;
    }
}