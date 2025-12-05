package org.example.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

// Клас для відправки HTTP-запитів до REST API сервера
public class ApiClient {

    // Базова URL-адреса REST API сервера
    private static final String BASE_URL = "http://localhost:8080/api";

    // HTTP-клієнт для виконання запитів
    private final HttpClient httpClient;

    // Бібліотека для серіалізації/десеріалізації JSON
    private final Gson gson;

    // Конструктор ініціалізує HTTP-клієнт та Gson
    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    // Таблиця типів
    // Отримує список усіх типів з сервера
    // Створює GET запит до /api/types
    // Відправляє запит через httpClient.send()
    // Отримує відповідь у вигляді JSON рядка
    // Десеріалізує JSON у List через Gson та повертає
    public List<Map<String, Object>> getAllTypes() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/types"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    // Створює новий тип на сервері
    // Серіалізує назву типу в JSON об'єкт
    // Створює POST запит до /api/types з JSON тілом
    // Встановлює Content-Type: application/json
    // Відправляє запит та повертає повідомлення з відповіді
    public String createType(String name) throws Exception {
        String json = gson.toJson(Map.of("name", name));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/types"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Оновлює існуючий тип на сервері
    // Серіалізує нову назву типу в JSON об'єкт
    // Створює PUT запит до /api/types/{id} з JSON тілом
    // Встановлює Content-Type: application/json
    // Відправляє запит та повертає повідомлення з відповіді
    public String updateType(Long id, String name) throws Exception {
        String json = gson.toJson(Map.of("name", name));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/types/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Видаляє тип на сервері за ID
    // Створює DELETE запит до /api/types/{id}
    // Відправляє запит через httpClient.send()
    // Повертає повідомлення з JSON відповіді
    public String deleteType(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/types/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Таблиця проєктів
    // Отримує список усіх проєктів з сервера
    // Створює GET запит до /api/projects
    // Відправляє запит через httpClient.send()
    // Отримує відповідь у вигляді JSON рядка
    // Десеріалізує JSON у List через Gson та повертає
    public List<Map<String, Object>> getAllProjects() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    // Створює новий проєкт на сервері
    // Серіалізує url та typeName в JSON об'єкт
    // Створює POST запит до /api/projects з JSON тілом
    // Встановлює Content-Type: application/json
    // Відправляє запит та повертає повідомлення з відповіді
    public String createProject(String url, String typeName) throws Exception {
        String json = gson.toJson(Map.of("url", url, "typeName", typeName));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Оновлює існуючий проєкт на сервері
    // Серіалізує url та typeName в JSON об'єкт
    // Створює PUT запит до /api/projects/{id} з JSON тілом
    // Встановлює Content-Type: application/json
    // Відправляє запит та повертає повідомлення з відповіді
    public String updateProject(Long id, String url, String typeName) throws Exception {
        String json = gson.toJson(Map.of("url", url, "typeName", typeName));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Видаляє проєкт на сервері за ID
    // Створює DELETE запит до /api/projects/{id}
    // Відправляє запит через httpClient.send()
    // Повертає повідомлення з JSON відповіді
    public String deleteProject(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/projects/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseMessage(response.body());
    }

    // Звʼязуюча таблиця типів проєктів
    // Отримує список зв'язків типів і проєктів з сервера
    // Створює GET запит до /api/types-projects
    // Відправляє запит через httpClient.send()
    // Отримує відповідь у вигляді JSON рядка
    // Десеріалізує JSON у List через Gson та повертає
    public List<Map<String, Object>> getTypesProjects() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/types-projects"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
    }

    // Допоміжний метод
    // Витягує повідомлення з JSON-відповіді сервера
    // Десеріалізує JSON рядок у Map
    // Шукає ключ "message" у Map
    // Якщо знайдено — повертає значення, інакше — "Невідома відповідь"
    private String parseMessage(String json) {
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        return (String) map.getOrDefault("message", "Невідома відповідь");
    }
}
