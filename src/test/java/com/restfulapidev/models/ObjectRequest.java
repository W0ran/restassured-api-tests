package com.restfulapidev.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO для тела запроса POST/PUT/PATCH на /objects.
 * Используется для создания и обновления объектов — name обязателен
 * для POST/PUT, data может быть любым произвольным набором атрибутов
 * или отсутствовать вовсе (для PATCH, где обновляется только часть полей).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectRequest {

    private String name;
    private ObjectData data;

    public ObjectRequest() {
        // Требуется пустой конструктор для сериализации Jackson
    }

    public ObjectRequest(String name, ObjectData data) {
        this.name = name;
        this.data = data;
    }

    public static ObjectRequest withNameOnly(String name) {
        return new ObjectRequest(name, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectData getData() {
        return data;
    }

    public void setData(ObjectData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectRequest{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}