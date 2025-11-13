package org.example.lab4.ui;

import org.example.lab4.model.Project;
import org.example.lab4.model.Type;
import org.example.lab4.service.ProjectService;
import org.example.lab4.service.TypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

// Клас відповідає за консольний інтерфейс користувача та навігацію по меню.
@Component
public class CliView implements CommandLineRunner {

    // Поле зберігає сервіс для роботи з типами.
    private final TypeService typeService;

    // Поле зберігає сервіс для роботи з проєктами.
    private final ProjectService projectService;

    // Поле зберігає сканер для читання введення користувача з консолі.
    private final Scanner scanner = new Scanner(System.in);

    // Конструктор ініціалізує консольний інтерфейс сервісами типів і проєктів.
    public CliView(TypeService typeService, ProjectService projectService) {
        this.typeService = typeService;
        this.projectService = projectService;
    }

    // Метод запускає головне меню застосунку та обробляє вибір користувача.
    @Override
    public void run(String... args) {
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

    // Метод обробляє роботу з таблицею типів.
    private void handleTypes() {
        List<Type> types = typeService.getAll();

        System.out.println("Таблиця Типи:");
        if (types.isEmpty()) {
            System.out.println("Таблиця порожня.");
            System.out.println("Доступна лише дія створення: C TypeName");
            System.out.print("Введіть дію (наприклад, 'C Backend', або 'q' для повернення у головне меню): ");

            String actionLine = scanner.nextLine().trim();
            if (actionLine.equalsIgnoreCase("q")) {
                return;
            }

            String[] parts = actionLine.split("\\s+", 2);
            if (parts.length == 0 || !parts[0].equalsIgnoreCase("C")) {
                System.out.println("Дозволена тільки дія C для порожньої таблиці.");
                return;
            }

            String name = parts.length > 1 ? parts[1].trim() : "";
            System.out.println(typeService.createType(name));
            return;
        }

        System.out.println("ID | Назва");
        for (Type type : types) {
            System.out.println(type.getId() + " | " + type.getName());
        }

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть ID (0 для створення нового, або 'q' для повернення у головне меню): ");
        String idInput = scanner.nextLine().trim();
        if (idInput.equalsIgnoreCase("q")) {
            return;
        }
        Long id = parseId(idInput);

        System.out.print("Введіть дію (наприклад, 'C TypeName', 'U NewName', 'D', або 'q' для повернення): ");
        String actionLine = scanner.nextLine().trim();
        if (actionLine.equalsIgnoreCase("q")) {
            return;
        }

        String[] parts = actionLine.split("\\s+", 2);
        if (parts.length < 1) {
            System.out.println("Невірний формат дії.");
            return;
        }

        String action = parts[0].toUpperCase();
        String param = parts.length > 1 ? parts[1].trim() : "";

        switch (action) {
            case "C" -> System.out.println(typeService.createType(param));
            case "U" -> System.out.println(typeService.updateType(id, param));
            case "D" -> System.out.println(typeService.deleteType(id));
            default -> System.out.println("Невідома дія.");
        }
    }

    // Метод обробляє роботу з таблицею проєктів.
    private void handleProjects() {
        List<Project> projects = projectService.getAll();

        System.out.println("Таблиця Проєкти:");
        if (projects.isEmpty()) {
            System.out.println("Таблиця порожня.");
            System.out.println("Доступна лише дія створення: C url type_name");
            System.out.print("Введіть дію (наприклад, 'C https://github.com/user/repo Backend', або 'q' для повернення): ");

            String actionLine = scanner.nextLine().trim();
            if (actionLine.equalsIgnoreCase("q")) {
                return;
            }

            String[] parts = actionLine.split("\\s+", 3);
            if (parts.length < 3 || !parts[0].equalsIgnoreCase("C")) {
                System.out.println("Для порожньої таблиці доступна тільки дія 'C url type_name'.");
                return;
            }

            String url = parts[1];
            String typeName = parts[2];
            System.out.println(projectService.createProject(url, typeName));
            return;
        }

        System.out.println("ID | URL | Назва Типу");
        for (Project project : projects) {
            String typeName = project.getType() != null
                    ? project.getType().getName()
                    : "null";
            System.out.println(project.getId() + " | " + project.getUrl() + " | " + typeName);
        }

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть ID (0 для створення нового, або 'q' для повернення у головне меню): ");
        String idInput = scanner.nextLine().trim();
        if (idInput.equalsIgnoreCase("q")) {
            return;
        }
        Long id = parseId(idInput);

        System.out.print("Введіть дію (наприклад, 'C url type_name', 'U url type_name', 'D', або 'q' для повернення): ");
        String actionLine = scanner.nextLine().trim();
        if (actionLine.equalsIgnoreCase("q")) {
            return;
        }

        String[] parts = actionLine.split("\\s+", 3);
        if (parts.length < 1) {
            System.out.println("Невірний формат дії.");
            return;
        }

        String action = parts[0].toUpperCase();

        switch (action) {
            case "C" -> {
                if (parts.length == 3) {
                    String url = parts[1];
                    String typeName = parts[2];
                    System.out.println(projectService.createProject(url, typeName));
                } else {
                    System.out.println("Вкажіть url та type_name для створення.");
                }
            }
            case "U" -> {
                if (id == null || parts.length != 3) {
                    System.out.println("Вкажіть ID, url та type_name для оновлення.");
                } else {
                    String url = parts[1];
                    String typeName = parts[2];
                    System.out.println(projectService.updateProject(id, url, typeName));
                }
            }
            case "D" -> System.out.println(projectService.deleteProject(id));
            default -> System.out.println("Невідома дія.");
        }
    }

    // Метод відображає вміст зв'язуючої таблиці Типи Проєктів лише для перегляду.
    private void handleTypesProjects() {
        List<Project> projects = projectService.getAll();

        System.out.println("Зв'язуюча таблиця Типи Проєктів:");
        System.out.println("Project ID | Type ID");

        boolean hasLinks = false;
        for (Project project : projects) {
            if (project.getType() != null) {
                hasLinks = true;
                System.out.println(project.getId() + " | " + project.getType().getId());
            }
        }

        if (!hasLinks) {
            System.out.println("Таблиця порожня (немає жодного зв'язку Project-Type).");
        }

        System.out.print("Введіть 'q' для повернення у головне меню: ");
        scanner.nextLine();
    }

    // Метод перетворює рядок у Long-значення ID або повертає null для 0 чи помилки.
    private Long parseId(String idStr) {
        if (idStr.isEmpty() || idStr.equals("0")) {
            return null;
        }
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID.");
            return null;
        }
    }
}