package org.example.lab4.controller;

import org.example.lab4.service.TypeService;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Клас відповідає за парсинг і обробку команд CRUD для таблиці Типи.
@Component
public class TypeCommandController {

    // Шаблон команди: [optional ID] [action] [params...], напр. "C Name", "2 U NewName", "3 D".
    private static final Pattern COMMAND_PATTERN =
            Pattern.compile("(?i)^(?:\\s*(\\d+)\\s+)?([cud])(?:\\s+(.*))?$");

    // Поле зберігає сервіс бізнес-логіки для сутності Type.
    private final TypeService typeService;

    // Конструктор ініціалізує контролер сервісом типів.
    public TypeCommandController(TypeService typeService) {
        this.typeService = typeService;
    }

    // Обробляє одну команду над таблицею Типи і повертає текст повідомлення для користувача.
    public String handleCommand(String commandLine, boolean tableEmpty) {
        Matcher matcher = COMMAND_PATTERN.matcher(commandLine);
        if (!matcher.matches()) {
            return "Невірний формат команди.";
        }

        String idGroup = matcher.group(1);
        String actionGroup = matcher.group(2);
        String paramsGroup = matcher.group(3);

        Long id = null;
        if (idGroup != null && !idGroup.equals("0")) {
            try {
                id = Long.parseLong(idGroup);
            } catch (NumberFormatException e) {
                return "Невірний формат ID.";
            }
        }

        String action = actionGroup.toUpperCase();
        String param = paramsGroup != null ? paramsGroup.trim() : "";

        return switch (action) {
            case "C" -> {
                if (param.isEmpty()) {
                    yield "Вкажіть назву типу для створення (C TypeName).";
                }
                // Для створення ID не потрібен, навіть якщо користувач його випадково ввів.
                yield typeService.createType(param);
            }
            case "U" -> {
                if (tableEmpty) {
                    yield "Таблиця порожня, доступна лише дія C.";
                }
                if (id == null || param.isEmpty()) {
                    yield "Для оновлення використовуйте формат: ID U NewName (наприклад, '2 U GameNew').";
                }
                yield typeService.updateType(id, param);
            }
            case "D" -> {
                if (tableEmpty) {
                    yield "Таблиця порожня, нема чого видаляти.";
                }
                if (id == null) {
                    yield "Для видалення використовуйте формат: ID D (наприклад, '3 D').";
                }
                yield typeService.deleteType(id);
            }
            default -> "Невідома дія.";
        };
    }
}