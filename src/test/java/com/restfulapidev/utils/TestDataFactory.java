package com.restfulapidev.utils;

import com.restfulapidev.models.ObjectData;
import com.restfulapidev.models.ObjectRequest;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Фабрика тестовых данных для тестов /objects.
 * Генерирует уникальные имена/значения на каждый вызов, чтобы тесты,
 * выполняемые параллельно или повторно, не конфликтовали друг с другом
 * и не зависели от состояния, оставшегося от предыдущих прогонов.
 */
public class TestDataFactory {

    private TestDataFactory() {
        // utility-класс, не предназначен для инстанцирования
    }

    /** Создаёт ObjectRequest с уникальным именем и одним набором тестовых атрибутов. */
    public static ObjectRequest createValidObjectRequest() {
        ObjectData data = new ObjectData();
        data.setAttribute("color", "Cosmic Black");
        data.setAttribute("price", randomPrice());
        data.setAttribute("year", 2026);

        return new ObjectRequest(uniqueName(), data);
    }

    /** Создаёт ObjectRequest только с именем, без поля data (data = null допустимо по API). */
    public static ObjectRequest createObjectRequestWithoutData() {
        return ObjectRequest.withNameOnly(uniqueName());
    }

    /** Создаёт ObjectRequest с произвольным переданным набором атрибутов. */
    public static ObjectRequest createObjectRequestWithAttributes(String key, Object value) {
        ObjectData data = new ObjectData();
        data.setAttribute(key, value);
        return new ObjectRequest(uniqueName(), data);
    }

    /** Генерирует уникальное имя объекта на основе текущего времени — исключает коллизии между тестами. */
    public static String uniqueName() {
        return "QA Test Object " + System.currentTimeMillis();
    }

    private static double randomPrice() {
        return ThreadLocalRandom.current().nextDouble(10.0, 2000.0);
    }
}