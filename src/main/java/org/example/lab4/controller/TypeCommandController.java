package org.example.lab4.controller;

import org.example.lab4.service.TypeService;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

// Клас відповідає за парсинг і обробку команд CRUD для таблиці Типи
@Component
public class TypeCommandController implements CommandController {

    // Шаблон команди коли, наприклад, "C Name", "2 U NewName", "3 D"
    private static final Pattern COMMAND_PATTERN =
            Pattern.compile("(?i)^(?:\\s*(\\d+)\\s+)?([cud])(?:\\s+(.*))?$");

    // Поле зберігає сервіс бізнес-логіки для сутності Type
    private final TypeService typeService;

    // Конструктор ініціалізує контролер сервісом типів
    public TypeCommandController(TypeService typeService) {
        this.typeService = typeService;
    }

    // Повертає регулярний вираз для парсингу команд
    @Override
    public Pattern getCommandPattern() {
        return COMMAND_PATTERN;
    }

    // Обробляє створення типу: C TypeName
    @Override
    public String handleCreate(String params) {
        if (params.isEmpty()) {
            return "Вкажіть назву типу для створення (C TypeName).";
        }
        // Для створення ID не потрібен, навіть якщо користувач його випадково ввів
        return typeService.createType(params);
    }

    // Обробляє оновлення типу: ID U NewName
    @Override
    public String handleUpdate(Long id, String params, boolean tableEmpty) {
        if (tableEmpty) {
            return "Таблиця порожня, доступна лише дія C.";
        }
        if (id == null || params.isEmpty()) {
            return "Для оновлення використовуйте формат: ID U NewName (наприклад, '2 U GameNew').";
        }
        return typeService.updateType(id, params);
    }

    // Обробляє видалення типу: ID D
    @Override
    public String handleDelete(Long id, boolean tableEmpty) {
        if (tableEmpty) {
            return "Таблиця порожня, нема чого видаляти.";
        }
        if (id == null) {
            return "Для видалення використовуйте формат: ID D (наприклад, '3 D').";
        }
        return typeService.deleteType(id);
    }
}