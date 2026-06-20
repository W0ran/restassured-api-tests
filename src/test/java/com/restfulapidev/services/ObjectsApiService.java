package com.restfulapidev.services;

import com.restfulapidev.models.ObjectRequest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Service layer для эндпоинтов /objects на restful-api.dev.
 * Инкапсулирует все HTTP-вызовы через RestAssured — тесты работают только
 * с методами этого класса, а не с RestAssured напрямую. Это аналог Page Object
 * Model, но для API: единая точка изменения, если поменяется базовый URL,
 * заголовки или формат запроса.
 * Публичные эндпоинты /objects не требуют авторизации (без x-api-key).
 */
public class ObjectsApiService {

    private static final String BASE_URL = "https://api.restful-api.dev";
    private static final String OBJECTS_PATH = "/objects";

    /**
     * Базовая спецификация запроса — общие заголовки и логирование для всех вызовов.
     * AllureRestAssured автоматически прикрепляет request/response к Allure-отчёту.
     */
    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    // ===== GET =====

    /** GET /objects — список всех объектов. */
    public Response getAllObjects() {
        return baseRequest()
                .when()
                .get(OBJECTS_PATH);
    }

    /** GET /objects/{id} — один объект по ID. */
    public Response getObjectById(String id) {
        return baseRequest()
                .pathParam("id", id)
                .when()
                .get(OBJECTS_PATH + "/{id}");
    }

    /** GET /objects?id=X&id=Y — несколько объектов по списку ID. */
    public Response getObjectsByIds(String... ids) {
        RequestSpecification request = baseRequest();
        for (String id : ids) {
            request = request.queryParam("id", id);
        }
        return request
                .when()
                .get(OBJECTS_PATH);
    }

    // ===== POST =====

    /** POST /objects — создание нового объекта. */
    public Response createObject(ObjectRequest objectRequest) {
        return baseRequest()
                .body(objectRequest)
                .when()
                .post(OBJECTS_PATH);
    }

    // ===== PUT =====

    /** PUT /objects/{id} — полная замена существующего объекта. */
    public Response updateObjectFully(String id, ObjectRequest objectRequest) {
        return baseRequest()
                .pathParam("id", id)
                .body(objectRequest)
                .when()
                .put(OBJECTS_PATH + "/{id}");
    }

    // ===== PATCH =====

    /** PATCH /objects/{id} — частичное обновление объекта. */
    public Response updateObjectPartially(String id, Object partialBody) {
        return baseRequest()
                .pathParam("id", id)
                .body(partialBody)
                .when()
                .patch(OBJECTS_PATH + "/{id}");
    }

    // ===== DELETE =====

    /** DELETE /objects/{id} — удаление объекта. */
    public Response deleteObject(String id) {
        return baseRequest()
                .pathParam("id", id)
                .when()
                .delete(OBJECTS_PATH + "/{id}");
    }
}