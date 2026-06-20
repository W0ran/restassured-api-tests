package com.restfulapidev.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO для десериализации ответа restful-api.dev на запросы к /objects.
 * Покрывает все варианты ответа: GET/POST возвращают id+name+data(+createdAt),
 * PUT/PATCH дополнительно возвращают updatedAt. Поля, которых нет в конкретном
 * ответе, остаются null — это нормально, т.к. структура ответа отличается
 * в зависимости от типа запроса (создание / обновление / чтение).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectResponse {

    private String id;
    private String name;
    private ObjectData data;
    private String createdAt;
    private String updatedAt;

    public ObjectResponse() {
        // Требуется пустой конструктор для десериализации Jackson
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ObjectResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}