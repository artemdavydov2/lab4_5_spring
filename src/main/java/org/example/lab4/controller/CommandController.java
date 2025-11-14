// CommandController.java
package org.example.lab4.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Інтерфейс інкапсулює спільну логіку парсингу команд CRUD у вигляді рядків
public interface CommandController {

    // Повертає регулярний вираз для парсингу команд
    Pattern getCommandPattern();

    // Обробляє створення (дія C)
    String handleCreate(String params);

    // Обробляє оновлення (дія U)
    String handleUpdate(Long id, String params, boolean tableEmpty);

    // Обробляє видалення (дія D)
    String handleDelete(Long id, boolean tableEmpty);

    // Обробляє одну команду CRUD і повертає текст повідомлення для користувача
    default String handleCommand(String commandLine, boolean tableEmpty) {
        Matcher matcher = getCommandPattern().matcher(commandLine);
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
        String params = paramsGroup != null ? paramsGroup.trim() : "";

        return switch (action) {
            case "C" -> handleCreate(params);
            case "U" -> handleUpdate(id, params, tableEmpty);
            case "D" -> handleDelete(id, tableEmpty);
            default -> "Невідома дія.";
        };
    }
}