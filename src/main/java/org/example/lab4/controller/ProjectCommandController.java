package org.example.lab4.controller;

import org.example.lab4.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

// Клас відповідає за парсинг і обробку команд CRUD для таблиці Проєкти
@Component
public class ProjectCommandController implements CommandController {

    // Той самий шаблон команди, що й для типів
    private static final Pattern COMMAND_PATTERN =
            Pattern.compile("(?i)^(?:\\s*(\\d+)\\s+)?([cud])(?:\\s+(.*))?$");

    // Поле зберігає сервіс бізнес-логіки для сутності Project
    private final ProjectService projectService;

    // Конструктор ініціалізує контролер сервісом проєктів
    public ProjectCommandController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Повертає регулярний вираз для парсингу команд
    @Override
    public Pattern getCommandPattern() {
        return COMMAND_PATTERN;
    }

    // Обробляє створення проєкту: C url type_name
    @Override
    public String handleCreate(String params) {
        if (params.isEmpty()) {
            return "Для створення проєкту використовуйте формат: C url type_name.";
        }
        String[] parts = params.split("\\s+", 2);
        if (parts.length < 2) {
            return "Вкажіть url та type_name для створення (наприклад, 'C https://github.com/user/repo Backend').";
        }
        String url = parts[0];
        String typeName = parts[1];
        return projectService.createProject(url, typeName);
    }

    // Обробляє оновлення проєкту: ID U url type_name
    @Override
    public String handleUpdate(Long id, String params, boolean tableEmpty) {
        if (tableEmpty) {
            return "Таблиця порожня, доступна лише дія C.";
        }
        if (id == null || params.isEmpty()) {
            return "Для оновлення використовуйте формат: ID U url type_name (наприклад, '2 U https://github.com/user/repo Backend').";
        }
        String[] parts = params.split("\\s+", 2);
        if (parts.length < 2) {
            return "Вкажіть url та type_name для оновлення.";
        }
        String url = parts[0];
        String typeName = parts[1];
        return projectService.updateProject(id, url, typeName);
    }

    // Обробляє видалення проєкту: ID D
    @Override
    public String handleDelete(Long id, boolean tableEmpty) {
        if (tableEmpty) {
            return "Таблиця порожня, нема чого видаляти.";
        }
        if (id == null) {
            return "Для видалення використовуйте формат: ID D (наприклад, '3 D').";
        }
        return projectService.deleteProject(id);
    }
}