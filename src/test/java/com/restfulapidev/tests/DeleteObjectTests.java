package com.restfulapidev.tests;

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

@Epic("Objects API")
@Feature("DELETE /objects/{id}")
public class DeleteObjectTests extends BaseApiTest {

    @Test(description = "Удаление существующего объекта")
    @Severity(SeverityLevel.BLOCKER)
    @Description("DELETE /objects/{id} должен вернуть 200 и подтверждающее сообщение")
    public void testDeleteExistingObject() {
        ObjectResponse created = objectsApi.createObject(TestDataFactory.createValidObjectRequest())
                .as(ObjectResponse.class);

        Response response = objectsApi.deleteObject(created.getId());

        response.then()
                .statusCode(200)
                .body("message", org.hamcrest.Matchers.containsString(created.getId()));
    }

    @Test(description = "Удалённый объект больше не доступен через GET")
    @Severity(SeverityLevel.CRITICAL)
    @Description("После успешного удаления повторный GET по тому же ID должен вернуть 404")
    public void testDeletedObjectIsNotRetrievable() {
        ObjectResponse created = objectsApi.createObject(TestDataFactory.createValidObjectRequest())
                .as(ObjectResponse.class);

        objectsApi.deleteObject(created.getId()).then().statusCode(200);

        Response getResponse = objectsApi.getObjectById(created.getId());

        getResponse.then().statusCode(404);
    }

    @Test(description = "Повторное удаление уже удалённого объекта")
    @Severity(SeverityLevel.NORMAL)
    @Description("Повторный DELETE по уже удалённому ID должен вернуть 404, а не 200")
    public void testDeleteAlreadyDeletedObject() {
        ObjectResponse created = objectsApi.createObject(TestDataFactory.createValidObjectRequest())
                .as(ObjectResponse.class);

        objectsApi.deleteObject(created.getId()).then().statusCode(200);

        Response secondDeleteResponse = objectsApi.deleteObject(created.getId());

        secondDeleteResponse.then().statusCode(404);
    }

    @Test(description = "Удаление несуществующего объекта")
    @Severity(SeverityLevel.NORMAL)
    @Description("DELETE по никогда не существовавшему ID должен вернуть 404")
    public void testDeleteNonExistentObject() {
        Response response = objectsApi.deleteObject("nonexistent-id-99999");

        response.then().statusCode(404);
    }
}