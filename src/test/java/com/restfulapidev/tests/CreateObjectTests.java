package com.restfulapidev.tests;

import com.restfulapidev.models.ObjectRequest;
import com.restfulapidev.models.ObjectResponse;
import com.restfulapidev.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Objects API")
@Feature("POST /objects")
public class CreateObjectTests extends BaseApiTest {

    @Test(description = "Создание объекта с именем и данными")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /objects с валидным телом должен вернуть 200, присвоить ID и сохранить переданные данные")
    public void testCreateObjectWithData() {
        ObjectRequest request = TestDataFactory.createValidObjectRequest();

        Response response = objectsApi.createObject(request);

        response.then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/object-schema.json"));

        ObjectResponse created = response.as(ObjectResponse.class);
        Assert.assertNotNull(created.getId(), "Созданному объекту должен быть присвоен ID");
        Assert.assertEquals(created.getName(), request.getName(), "Имя в ответе должно совпадать с переданным");
        Assert.assertNotNull(created.getCreatedAt(), "Поле createdAt должно быть заполнено сервером");
        Assert.assertEquals(created.getData().get("color"), request.getData().get("color"),
                "Атрибут 'color' должен совпадать с переданным в запросе");
    }

    @Test(description = "Создание объекта только с именем, без data")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /objects без поля data должен успешно создать объект с data = null")
    public void testCreateObjectWithoutData() {
        ObjectRequest request = TestDataFactory.createObjectRequestWithoutData();

        Response response = objectsApi.createObject(request);

        response.then().statusCode(200);

        ObjectResponse created = response.as(ObjectResponse.class);
        Assert.assertEquals(created.getName(), request.getName());
        Assert.assertNull(created.getData(), "Поле data должно остаться null, если не передано в запросе");
    }

    @Test(description = "Созданный объект доступен через последующий GET")
    @Severity(SeverityLevel.CRITICAL)
    @Description("После успешного создания объект должен быть немедленно доступен по своему ID через GET")
    public void testCreatedObjectIsRetrievable() {
        ObjectRequest request = TestDataFactory.createValidObjectRequest();
        ObjectResponse created = objectsApi.createObject(request).as(ObjectResponse.class);

        Response getResponse = objectsApi.getObjectById(created.getId());

        getResponse.then().statusCode(200);

        ObjectResponse fetched = getResponse.as(ObjectResponse.class);
        Assert.assertEquals(fetched.getId(), created.getId());
        Assert.assertEquals(fetched.getName(), created.getName());
    }

    @Test(description = "Создание объекта с произвольной вложенной структурой data")
    @Severity(SeverityLevel.NORMAL)
    @Description("API должен принимать любой набор атрибутов в data без жёсткой схемы")
    public void testCreateObjectWithCustomAttributes() {
        ObjectRequest request = TestDataFactory.createObjectRequestWithAttributes("warranty_months", 24);

        Response response = objectsApi.createObject(request);

        response.then().statusCode(200);

        ObjectResponse created = response.as(ObjectResponse.class);
        Assert.assertEquals(created.getData().get("warranty_months"), 24);
    }
}