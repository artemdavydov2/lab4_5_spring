package org.example.lab4.view;

import org.example.lab4.controller.ProjectCommandController;
import org.example.lab4.controller.TypeCommandController;
import org.example.lab4.model.Project;
import org.example.lab4.model.Type;
import org.example.lab4.service.ProjectService;
import org.example.lab4.service.TypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// Клас відповідає за консольний інтерфейс користувача та навігацію по меню
@Component
public class CliView implements CommandLineRunner {

    // Поле зберігає сервіс для роботи з типами
    private final TypeService typeService;

    // Поле зберігає сервіс для роботи з проєктами
    private final ProjectService projectService;

    // Поле зберігає контролер для парсингу та обробки команд над таблицею Типи
    private final TypeCommandController typeCommandController;

    // Поле зберігає контролер для парсингу та обробки команд над таблицею Проєкти
    private final ProjectCommandController projectCommandController;

    // Поле зберігає сканер для читання введення користувача з консолі
    private final Scanner scanner = new Scanner(System.in);

    // Загальний прапорець для відображення інформації із зв'язаних таблиць
    private static final boolean SHOW_EXTENDED_INFO_VIEW = true;

    // Конструктор ініціалізує консольний інтерфейс сервісами та контролерами типів і проєктів
    public CliView(TypeService typeService,
                   ProjectService projectService,
                   TypeCommandController typeCommandController,
                   ProjectCommandController projectCommandController) {
        this.typeService = typeService;
        this.projectService = projectService;
        this.typeCommandController = typeCommandController;
        this.projectCommandController = projectCommandController;
    }

    // Метод запускає головне меню застосунку та обробляє вибір користувача
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

    // Метод обробляє роботу з таблицею типів
    private void handleTypes() {
        List<Type> types = typeService.getAll();

        System.out.println("Таблиця Типи:");
        if (types.isEmpty()) {
            System.out.println("Таблиця порожня.");
            System.out.println("Доступна лише дія створення: C TypeName");
            System.out.print("Введіть команду (наприклад, 'C Backend', або 'q' для повернення у головне меню): ");

            String commandLine = scanner.nextLine().trim();
            if (commandLine.equalsIgnoreCase("q")) {
                return;
            }

            String result = typeCommandController.handleCommand(commandLine, true);
            System.out.println(result);
            return;
        }

        // Формуємо таблицю для виводу з вирівнюванням колонок
        List<String> headers = List.of("ID", "Назва");
        List<List<String>> rows = types.stream()
                .map(type -> List.of(
                        String.valueOf(type.getId()),
                        type.getName()
                ))
                .collect(Collectors.toList());

        TablePrinter.printTable(headers, rows);

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть команду (наприклад, 'C Backend', '2 U NewName', '3 D', або 'q' для повернення): ");

        String commandLine = scanner.nextLine().trim();
        if (commandLine.equalsIgnoreCase("q")) {
            return;
        }

        String result = typeCommandController.handleCommand(commandLine, false);
        System.out.println(result);
    }

    // Метод обробляє роботу з таблицею проєктів
    private void handleProjects() {
        List<Project> projects = projectService.getAll();

        System.out.println("Таблиця Проєкти:");
        if (projects.isEmpty()) {
            System.out.println("Таблиця порожня.");
            System.out.println("Доступна лише дія створення: C url type_name");
            System.out.print("Введіть команду (наприклад, 'C https://github.com/user/repo Backend', або 'q' для повернення): ");

            String commandLine = scanner.nextLine().trim();
            if (commandLine.equalsIgnoreCase("q")) {
                return;
            }

            String result = projectCommandController.handleCommand(commandLine, true);
            System.out.println(result);
            return;
        }

        List<String> headers;
        List<List<String>> rows;

        if (SHOW_EXTENDED_INFO_VIEW) {
            // Розширений вигляд: ID, URL, Назва типу, ID типу
            headers = List.of("ID", "URL", "Назва Типу", "ID Типу");
            rows = projects.stream()
                    .map(project -> {
                        String typeName = project.getType() != null
                                ? project.getType().getName()
                                : "null";
                        String typeId = project.getType() != null
                                ? String.valueOf(project.getType().getId())
                                : "null";
                        return List.of(
                                String.valueOf(project.getId()),
                                project.getUrl(),
                                typeName,
                                typeId
                        );
                    })
                    .collect(Collectors.toList());
        } else {
            // Стандартний вигляд: ID, URL, Назва типу
            headers = List.of("ID", "URL", "Назва Типу");
            rows = projects.stream()
                    .map(project -> {
                        String typeName = project.getType() != null
                                ? project.getType().getName()
                                : "null";
                        return List.of(
                                String.valueOf(project.getId()),
                                project.getUrl(),
                                typeName
                        );
                    })
                    .collect(Collectors.toList());
        }

        // Спільна частина винесена з if/else
        TablePrinter.printTable(headers, rows);

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть команду (наприклад, 'C url type_name', '2 U url type_name', '3 D', або 'q' для повернення): ");

        String commandLine = scanner.nextLine().trim();
        if (commandLine.equalsIgnoreCase("q")) {
            return;
        }

        String result = projectCommandController.handleCommand(commandLine, false);
        System.out.println(result);
    }

    // Метод відображає вміст зв'язуючої таблиці Типи Проєктів лише для перегляду
    private void handleTypesProjects() {
        List<Project> projects = projectService.getAll();

        if (SHOW_EXTENDED_INFO_VIEW) {
            System.out.println("Зв'язуюча таблиця Типи Проєктів (розширена):");

            List<String> headers = List.of("Project ID", "Type ID", "Type Name");
            List<List<String>> rows = projects.stream()
                    .filter(project -> project.getType() != null)
                    .map(project -> List.of(
                            String.valueOf(project.getId()),
                            String.valueOf(project.getType().getId()),
                            project.getType().getName()
                    ))
                    .collect(Collectors.toList());

            if (rows.isEmpty()) {
                System.out.println("Таблиця порожня (немає жодного зв'язку Project-Type).");
            } else {
                TablePrinter.printTable(headers, rows);
            }
        } else {
            System.out.println("Зв'язуюча таблиця Типи Проєктів:");

            List<String> headers = List.of("Project ID", "Type ID");
            List<List<String>> rows = projects.stream()
                    .filter(project -> project.getType() != null)
                    .map(project -> List.of(
                            String.valueOf(project.getId()),
                            String.valueOf(project.getType().getId())
                    ))
                    .collect(Collectors.toList());

            if (rows.isEmpty()) {
                System.out.println("Таблиця порожня (немає жодного зв'язку Project-Type).");
            } else {
                TablePrinter.printTable(headers, rows);
            }
        }

        System.out.print("Введіть 'q' для повернення у головне меню: ");
        scanner.nextLine();
    }
}