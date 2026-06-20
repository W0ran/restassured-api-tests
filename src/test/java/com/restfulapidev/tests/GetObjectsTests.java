package com.restfulapidev.tests;

import com.restfulapidev.models.ObjectResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Objects API")
@Feature("GET /objects")
public class GetObjectsTests extends BaseApiTest {

    @Test(description = "Получение списка всех объектов")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /objects должен вернуть 200 и непустой список объектов, соответствующий JSON Schema")
    public void testGetAllObjects() {
        Response response = objectsApi.getAllObjects();

        response.then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/objects-list-schema.json"));

        List<ObjectResponse> objects = response.jsonPath().getList(".", ObjectResponse.class);
        Assert.assertFalse(objects.isEmpty(), "Список объектов не должен быть пустым");
    }

    @Test(description = "Получение объекта по существующему ID")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /objects/{id} с валидным ID должен вернуть 200 и корректную структуру объекта")
    public void testGetObjectByValidId() {
        Response response = objectsApi.getObjectById("7");

        response.then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/object-schema.json"));

        ObjectResponse object = response.as(ObjectResponse.class);
        Assert.assertEquals(object.getId(), "7", "ID в ответе должен совпадать с запрошенным");
        Assert.assertNotNull(object.getName(), "Имя объекта не должно быть null");
    }

    @Test(description = "Получение объекта по несуществующему ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /objects/{id} с несуществующим ID должен вернуть 404")
    public void testGetObjectByInvalidId() {
        Response response = objectsApi.getObjectById("nonexistent-id-99999");

        response.then()
                .statusCode(404);
    }

    @Test(description = "Получение нескольких объектов по списку ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /objects?id=3&id=5&id=10 должен вернуть только запрошенные объекты")
    public void testGetObjectsByMultipleIds() {
        Response response = objectsApi.getObjectsByIds("3", "5", "10");

        response.then().statusCode(200);

        List<ObjectResponse> objects = response.jsonPath().getList(".", ObjectResponse.class);
        Assert.assertEquals(objects.size(), 3, "Должно вернуться ровно 3 объекта по запрошенным ID");

        List<String> returnedIds = objects.stream().map(ObjectResponse::getId).toList();
        Assert.assertTrue(returnedIds.containsAll(List.of("3", "5", "10")),
                "В ответе должны быть все запрошенные ID");
    }

    @Test(description = "Поля объекта содержат ожидаемые типы данных")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка, что вложенное поле data корректно десериализуется в динамическую структуру")
    public void testObjectDataFieldDeserialization() {
        Response response = objectsApi.getObjectById("7"); // Apple MacBook Pro 16, есть data

        ObjectResponse object = response.as(ObjectResponse.class);

        Assert.assertNotNull(object.getData(), "Поле data не должно быть null для объекта с id=7");
        Assert.assertFalse(object.getData().isEmpty(), "Поле data должно содержать атрибуты");
    }
}