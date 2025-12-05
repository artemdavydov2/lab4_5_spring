package org.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Клас реалізує консольний інтерфейс для взаємодії з REST API
public class ConsoleMenu {

    // API-клієнт для відправки запитів до сервера
    private final ApiClient apiClient;

    // Сканер для читання введення користувача
    private final Scanner scanner;

    // Конструктор ініціалізує API-клієнт та сканер
    public ConsoleMenu() {
        this.apiClient = new ApiClient();
        this.scanner = new Scanner(System.in);
    }

    // Запускає головний цикл консольного меню
    public void run() {
        while (true) {
            System.out.println("Меню:");
            System.out.println("1 - Відобразити вміст таблиці Типи");
            System.out.println("2 - Відобразити вміст таблиці Проєкти");
            System.out.println("3 - Відобразити вміст зв'язуючої таблиці Типи Проєктів");
            System.out.println("4 - Вихід з програми");

            String choice = scanner.nextLine().trim();

            if (choice.equals("4")) {
                System.out.println("Програма завершена.");
                break;
            }

            try {
                int option = Integer.parseInt(choice);
                switch (option) {
                    case 1 -> handleTypes();
                    case 2 -> handleProjects();
                    case 3 -> handleTypesProjects();
                    default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Невірний ввід. Спробуйте ще раз.");
            }
        }
    }

    // Інтерфейс таблиці типів
    // Метод обробляє роботу з таблицею типів
    private void handleTypes() {
        try {
            List<Map<String, Object>> types = apiClient.getAllTypes();

            System.out.println("Таблиця Типи:");
            if (types.isEmpty()) {
                System.out.println("Таблиця порожня.");
                System.out.println("Доступна лише дія створення: C TypeName");

                // Цикл введення команди до отримання коректної або 'q'
                while (true) {
                    System.out
                            .print("Введіть команду (наприклад, 'C Backend', або 'q' для повернення у головне меню): ");
                    String commandLine = scanner.nextLine().trim();

                    if (commandLine.equalsIgnoreCase("q")) {
                        return;
                    }

                    if (isValidTypeCommand(commandLine, true)) {
                        String result = processTypeCommand(commandLine, true);
                        System.out.println(result);
                        return;
                    } else {
                        System.out.println("Невірний вибір. Спробуйте ще раз.");
                    }
                }
            }

            // Формуємо таблицю для виводу з вирівнюванням колонок
            List<String> headers = List.of("ID", "Назва");
            List<List<String>> rows = new ArrayList<>();
            for (Map<String, Object> type : types) {
                rows.add(List.of(
                        formatId(type.get("id")),
                        String.valueOf(type.get("name"))));
            }

            TablePrinter.printTable(headers, rows);

            System.out.println("Доступні дії: C - create, U - update, D - delete");

            // Цикл введення команди до отримання коректної або 'q'
            while (true) {
                System.out.print(
                        "Введіть команду (наприклад, 'C Backend', '2 U NewName', '3 D', або 'q' для повернення): ");
                String commandLine = scanner.nextLine().trim();

                if (commandLine.equalsIgnoreCase("q")) {
                    return;
                }

                if (isValidTypeCommand(commandLine, false)) {
                    String result = processTypeCommand(commandLine, false);
                    System.out.println(result);
                    return;
                } else {
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            }

        } catch (Exception e) {
            System.out.println("Помилка з'єднання з сервером: " + e.getMessage());
        }
    }

    // Перевіряє чи команда для типів є валідною (C/U/D)
    private boolean isValidTypeCommand(String input, boolean isTableEmpty) {
        input = input.trim().toUpperCase();

        // Create: C TypeName
        if (input.startsWith("C ") && input.length() > 2) {
            return true;
        }

        if (isTableEmpty) {
            return false;
        }

        // Update: ID U NewName
        if (input.contains(" U ")) {
            String[] parts = input.split(" U ");
            if (parts.length >= 2 && parts[0].trim().matches("\\d+") && !parts[1].trim().isEmpty()) {
                return true;
            }
        }

        // Delete: ID D
        if (input.endsWith(" D")) {
            String idStr = input.substring(0, input.length() - 2).trim();
            return idStr.matches("\\d+");
        }

        return false;
    }

    // Обробляє команди CRUD для типів
    private String processTypeCommand(String input, boolean isTableEmpty) throws Exception {
        input = input.trim();

        // Create: C TypeName
        if (input.toUpperCase().startsWith("C ")) {
            String name = input.substring(2).trim();
            if (name.isEmpty()) {
                return "Вкажіть назву типу для створення.";
            }
            return apiClient.createType(name);
        }

        if (isTableEmpty) {
            return "Таблиця порожня. Доступна лише дія створення: C TypeName";
        }

        // Update: ID U NewName
        if (input.toUpperCase().contains(" U ")) {
            String[] parts = input.split("(?i) U ");
            if (parts.length < 2) {
                return "Невірний формат. Використовуйте: ID U NewName";
            }
            Long id = Long.parseLong(parts[0].trim());
            String name = parts[1].trim();
            if (name.isEmpty()) {
                return "Вкажіть нову назву типу.";
            }
            return apiClient.updateType(id, name);
        }

        // Delete: ID D
        if (input.toUpperCase().endsWith(" D")) {
            String idStr = input.substring(0, input.length() - 2).trim();
            Long id = Long.parseLong(idStr);
            return apiClient.deleteType(id);
        }

        return "Невідома команда.";
    }

    // Інтерфейс таблиці проєктів
    // Метод обробляє роботу з таблицею проєктів
    private void handleProjects() {
        try {
            List<Map<String, Object>> projects = apiClient.getAllProjects();

            System.out.println("Таблиця Проєкти:");
            if (projects.isEmpty()) {
                System.out.println("Таблиця порожня.");
                System.out.println("Доступна лише дія створення: C url type_name");

                // Цикл введення команди до отримання коректної або 'q'
                while (true) {
                    System.out.print(
                            "Введіть команду (наприклад, 'C https://github.com/user/repo Backend', або 'q' для повернення): ");
                    String commandLine = scanner.nextLine().trim();

                    if (commandLine.equalsIgnoreCase("q")) {
                        return;
                    }

                    if (isValidProjectCommand(commandLine, true)) {
                        String result = processProjectCommand(commandLine, true);
                        System.out.println(result);
                        return;
                    } else {
                        System.out.println("Невірний вибір. Спробуйте ще раз.");
                    }
                }
            }

            // Розширений вигляд: ID, URL, Назва типу, ID типу
            List<String> headers = List.of("ID", "URL", "Назва Типу", "ID Типу");
            List<List<String>> rows = new ArrayList<>();
            for (Map<String, Object> project : projects) {
                String typeName = project.get("typeName") != null ? String.valueOf(project.get("typeName")) : "null";
                String typeId = project.get("typeId") != null ? formatId(project.get("typeId")) : "null";
                rows.add(List.of(
                        formatId(project.get("id")),
                        String.valueOf(project.get("url")),
                        typeName,
                        typeId));
            }

            TablePrinter.printTable(headers, rows);

            System.out.println("Доступні дії: C - create, U - update, D - delete");

            // Цикл введення команди до отримання коректної або 'q'
            while (true) {
                System.out.print(
                        "Введіть команду (наприклад, 'C url type_name', '2 U url type_name', '3 D', або 'q' для повернення): ");
                String commandLine = scanner.nextLine().trim();

                if (commandLine.equalsIgnoreCase("q")) {
                    return;
                }

                if (isValidProjectCommand(commandLine, false)) {
                    String result = processProjectCommand(commandLine, false);
                    System.out.println(result);
                    return;
                } else {
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            }

        } catch (Exception e) {
            System.out.println("Помилка з'єднання з сервером: " + e.getMessage());
        }
    }

    // Перевіряє чи команда для проєктів є валідною (C/U/D)
    private boolean isValidProjectCommand(String input, boolean isTableEmpty) {
        input = input.trim().toUpperCase();

        // Create: C url type_name
        if (input.startsWith("C ")) {
            String[] parts = input.substring(2).trim().split("\\s+", 2);
            return parts.length >= 2;
        }

        if (isTableEmpty) {
            return false;
        }

        // Update: ID U url type_name
        if (input.contains(" U ")) {
            String[] mainParts = input.split(" U ");
            if (mainParts.length >= 2 && mainParts[0].trim().matches("\\d+")) {
                String[] params = mainParts[1].trim().split("\\s+", 2);
                return params.length >= 2;
            }
        }

        // Delete: ID D
        if (input.endsWith(" D")) {
            String idStr = input.substring(0, input.length() - 2).trim();
            return idStr.matches("\\d+");
        }

        return false;
    }

    // Обробляє команди CRUD для проєктів
    private String processProjectCommand(String input, boolean isTableEmpty) throws Exception {
        input = input.trim();

        // Create: C url type_name
        if (input.toUpperCase().startsWith("C ")) {
            String[] parts = input.substring(2).trim().split("\\s+", 2);
            if (parts.length < 2) {
                return "Невірний формат. Використовуйте: C url type_name";
            }
            return apiClient.createProject(parts[0], parts[1]);
        }

        if (isTableEmpty) {
            return "Таблиця порожня. Доступна лише дія створення: C url type_name";
        }

        // Update: ID U url type_name
        if (input.toUpperCase().contains(" U ")) {
            String[] mainParts = input.split("(?i) U ");
            if (mainParts.length < 2) {
                return "Невірний формат. Використовуйте: ID U url type_name";
            }
            Long id = Long.parseLong(mainParts[0].trim());
            String[] params = mainParts[1].trim().split("\\s+", 2);
            if (params.length < 2) {
                return "Невірний формат. Використовуйте: ID U url type_name";
            }
            return apiClient.updateProject(id, params[0], params[1]);
        }

        // Delete: ID D
        if (input.toUpperCase().endsWith(" D")) {
            String idStr = input.substring(0, input.length() - 2).trim();
            Long id = Long.parseLong(idStr);
            return apiClient.deleteProject(id);
        }

        return "Невідома команда.";
    }

    // Інтерфейс таблиці зв'язуючої таблиці типів проєктів
    // Метод відображає вміст зв'язуючої таблиці Типи Проєктів лише для перегляду
    private void handleTypesProjects() {
        try {
            List<Map<String, Object>> typesProjects = apiClient.getTypesProjects();

            System.out.println("Зв'язуюча таблиця Типи Проєктів (розширена):");

            if (typesProjects.isEmpty()) {
                System.out.println("Таблиця порожня (немає жодного зв'язку Project-Type).");
            } else {
                List<String> headers = List.of("Project ID", "Type ID", "Type Name");
                List<List<String>> rows = new ArrayList<>();
                for (Map<String, Object> tp : typesProjects) {
                    rows.add(List.of(
                            formatId(tp.get("projectId")),
                            formatId(tp.get("typeId")),
                            String.valueOf(tp.get("typeName"))));
                }
                TablePrinter.printTable(headers, rows);
            }

            // Цикл очікування на 'q'
            while (true) {
                System.out.print("Введіть 'q' для повернення у головне меню: ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("q")) {
                    return;
                } else {
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            }

        } catch (Exception e) {
            System.out.println("Помилка з'єднання з сервером: " + e.getMessage());
        }
    }

    // Форматує ID (перетворює Double у Long для красивого виводу)
    private String formatId(Object id) {
        if (id instanceof Number) {
            return String.valueOf(((Number) id).longValue());
        }
        return String.valueOf(id);
    }
}
