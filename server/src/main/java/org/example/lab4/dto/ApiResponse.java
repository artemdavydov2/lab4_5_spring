package org.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Універсальна відповідь REST API з індикатором успіху та повідомленням
// Анотація генерує getters, setters, toString, equals, hashCode
@Data
// Анотація генерує конструктор без параметрів для десеріалізації JSON
@NoArgsConstructor
// Анотація генерує конструктор з усіма полями
@AllArgsConstructor
public class ApiResponse {
    // Поле вказує чи операція була успішною
    private boolean success;
    // Поле містить текстове повідомлення про результат операції
    private String message;
    // Поле містить додаткові дані відповіді
    private Object data;

    // Статичний метод створює успішну відповідь з повідомленням
    public static ApiResponse success(String message) {
        // Повертає новий об'єкт ApiResponse з success=true
        return new ApiResponse(true, message, null);
    }

    // Статичний метод створює успішну відповідь з повідомленням та даними
    public static ApiResponse success(String message, Object data) {
        // Повертає новий об'єкт ApiResponse з success=true та даними
        return new ApiResponse(true, message, data);
    }

    // Статичний метод створює відповідь з помилкою
    public static ApiResponse error(String message) {
        // Повертає новий об'єкт ApiResponse з success=false
        return new ApiResponse(false, message, null);
    }
}
