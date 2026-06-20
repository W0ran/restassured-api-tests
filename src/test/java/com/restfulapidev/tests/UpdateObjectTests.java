package com.restfulapidev.tests;

import com.restfulapidev.models.ObjectRequest;
import com.restfulapidev.models.ObjectResponse;
import com.restfulapidev.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

@Epic("Objects API")
@Feature("PUT & PATCH /objects/{id}")
public class UpdateObjectTests extends BaseApiTest {

    @Test(description = "Полное обновление объекта через PUT")
    @Severity(SeverityLevel.BLOCKER)
    @Description("PUT /objects/{id} должен полностью заменить объект новыми данными и проставить updatedAt")
    public void testFullUpdateWithPut() {
        ObjectResponse created = objectsApi.createObject(TestDataFactory.createValidObjectRequest())
                .as(ObjectResponse.class);

        ObjectRequest updateRequest = TestDataFactory.createObjectRequestWithAttributes("color", "Midnight Blue");

        Response response = objectsApi.updateObjectFully(created.getId(), updateRequest);

        response.then().statusCode(200);

        ObjectResponse updated = response.as(ObjectResponse.class);
        Assert.assertEquals(updated.getId(), created.getId(), "ID объекта не должен меняться при обновлении");
        Assert.assertEquals(updated.getName(), updateRequest.getName(), "Имя должно полностью замениться на новое");
        Assert.assertEquals(updated.getData().get("color"), "Midnight Blue");
        Assert.assertNotNull(updated.getUpdatedAt(), "Поле updatedAt должно быть проставлено после PUT");
    }

    @Test(description = "PUT заменяет объект полностью — старые поля data исчезают")
    @Severity(SeverityLevel.CRITICAL)
    @Description("В отличие от PATCH, PUT не должен сохранять поля из старой версии объекта, не указанные в новом запросе")
    public void testPutReplacesDataCompletely() {
        ObjectRequest original = TestDataFactory.createValidObjectRequest(); // содержит color, price, year
        ObjectResponse created = objectsApi.createObject(original).as(ObjectResponse.class);

        ObjectRequest replacement = TestDataFactory.createObjectRequestWithAttributes("onlyField", "onlyValue");
        ObjectResponse updated = objectsApi.updateObjectFully(created.getId(), replacement).as(ObjectResponse.class);

        Assert.assertFalse(updated.getData().has("price"),
                "После PUT поле 'price' из старой версии не должно сохраняться");
        Assert.assertTrue(updated.getData().has("onlyField"),
                "После PUT должно остаться только новое поле");
    }

    @Test(description = "Частичное обновление объекта через PATCH")
    @Severity(SeverityLevel.BLOCKER)
    @Description("PATCH /objects/{id} должен обновить только переданные поля, сохранив остальные неизменными")
    public void testPartialUpdateWithPatch() {
        ObjectResponse created = objectsApi.createObject(TestDataFactory.createValidObjectRequest())
                .as(ObjectResponse.class);
        String originalName = created.getName();

        Map<String, Object> patchBody = Map.of("data", Map.of("color", "Sunset Orange"));

        Response response = objectsApi.updateObjectPartially(created.getId(), patchBody);

        response.then().statusCode(200);

        ObjectResponse patched = response.as(ObjectResponse.class);
        Assert.assertEquals(patched.getName(), originalName,
                "PATCH не должен менять поля, которые не были переданы (name)");
        Assert.assertEquals(patched.getData().get("color"), "Sunset Orange");
        Assert.assertNotNull(patched.getUpdatedAt(), "Поле updatedAt должно быть проставлено после PATCH");
    }

    @Test(description = "Обновление несуществующего объекта")
    @Severity(SeverityLevel.NORMAL)
    @Description("PUT по несуществующему ID должен вернуть 404, а не создавать новый объект")
    public void testUpdateNonExistentObject() {
        Response response = objectsApi.updateObjectFully(
                "nonexistent-id-99999",
                TestDataFactory.createValidObjectRequest()
        );

        response.then().statusCode(404);
    }
}