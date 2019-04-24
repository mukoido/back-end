package com.brainacad;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.apache.http.HttpResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brainacad.JsonUtils.listFromJSONByPath;
import static com.brainacad.JsonUtils.stringFromJSONByPath;


public class RestTest{

    private static final String URL="https://reqres.in/";

    @Test//GET метод
    public void checkGetResponseStatusCode() throws IOException {
        String endpoint="/api/users";

        //8TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper


        //Выполняем REST GET запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"page=2");

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);
    }

    @Test//GET метод
    public void listUsers() throws IOException {
        String endpoint="/api/users";

         //Выполняем REST GET запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"page=2");

        //Конвертируем входящий поток тела ответа в строку
        String body=HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("NEW", null, body);
    }

    @Test//POST метод
    public void checkPostResponseStatusCode() throws IOException {
        String endpoint="/api/users";



        //создаём тело запроса
        String requestBody="{\"name\": \"morpheus\",\"job\": \"leader\"}";

        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL+endpoint,requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 201", 201, statusCode);
    }

    @Test//POST метод
    public void checkPostResponseBodyNotNull() throws IOException {
        String endpoint="/api/users";

         //создаём тело запроса
        String requestBody="{\"name\": \"morpheus\",\"job\": \"leader\"}";

        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL+endpoint,requestBody);

        //Конвертируем входящий поток тела ответа в строку
        String body=HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("Body shouldn't be null", null, body);
    }
    @Test//GET ListUsers
    public void ListUsers() throws IOException {
        String endpoint="/api/users";
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"page=2");
        String body=HttpClientHelper.getBodyFromResponse(response);
        String jsonPath="$.data[*].first_name";
        List listUsers = listFromJSONByPath(body,jsonPath);
        List expectedUsers = Arrays.asList("Eve","Charles","Tracey");
        Assert.assertEquals ("Error msg",  expectedUsers,listUsers);
        System.out.println(body);
    }

    @Test//GET SingleUser
    public void SingleUser() throws IOException {
        String endpoint="/api/users/2";
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"");
        String body=HttpClientHelper.getBodyFromResponse(response);
        String jsonPath="$.data.first_name";
        String singleUser = stringFromJSONByPath(body,jsonPath);
        String expectedUser =("Janet");
        Assert.assertEquals ("Error msg",  expectedUser,singleUser);
        System.out.println(body);
    }

    @Test//GET SingleUserNotFound
    public void SingleUserNotFound() throws IOException {
        String endpoint="/api/users/23";
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"");
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 404", 404, statusCode);

    }

        @Test//POST Create
        public void CREATE() throws IOException {
            String endpoint="/api/users/2";
            HttpResponse response = HttpClientHelper.post(URL+endpoint,"");
            String body=HttpClientHelper.getBodyFromResponse(response);
            String jsonPath="$.createdAt";
            String create = stringFromJSONByPath(body,jsonPath);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC();
            DateTime dt = formatter.parseDateTime(stringFromJSONByPath(body,jsonPath));
            System.out.println(create);
            Assert.assertTrue("PostCreatedBUG", dt.plusMinutes(-70).isBeforeNow());
    }

    @Test// json test
    public void validateJsonTest() throws Exception {
        String endpoint="/api/users";
        HttpResponse response = HttpClientHelper.get(URL+endpoint, "page=2" );
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals("Response status code should be 200", 200, statusCode);
        String body=HttpClientHelper.getBodyFromResponse(response);
        ProcessingReport result = MyJsonValidator.validateJson(body, "schema\\schema1");
        Assert.assertTrue(result.toString(),result.isSuccess());
    }
    //TODO: напишите по тесткейсу на каждый вариант запроса на сайте https://reqres.in
    //TODO: в тескейсах проверьте Result Code и несколько параметров из JSON ответа (если он есть)

}
