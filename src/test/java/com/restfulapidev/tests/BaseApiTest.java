package com.restfulapidev.tests;

import com.restfulapidev.services.ObjectsApiService;
import org.testng.annotations.BeforeClass;

/**
 * Базовый класс для всех API-тестов. Предоставляет общий экземпляр
 * ObjectsApiService — в отличие от UI-тестов, здесь не нужен сложный
 * жизненный цикл (браузер/контекст/страница), RestAssured не требует
 * закрытия соединений между тестами.
 */
public abstract class BaseApiTest {

    protected ObjectsApiService objectsApi;

    @BeforeClass(alwaysRun = true)
    public void setUpService() {
        objectsApi = new ObjectsApiService();
    }
}