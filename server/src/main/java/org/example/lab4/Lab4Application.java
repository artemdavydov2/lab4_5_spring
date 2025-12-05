package org.example.lab4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Клас відповідає за запуск Spring Boot застосунку
// Анотація вмикає автоконфігурацію та сканування компонентів
@SpringBootApplication
public class Lab4Application {

    // Метод запускає Spring Boot застосунок
    public static void main(String[] args) {
        // Виклик методу run для запуску контексту Spring з вбудованим Tomcat
        SpringApplication.run(Lab4Application.class, args);
    }
}