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

// Клас відповідає за консольний інтерфейс користувача та навігацію по меню.
@Component
public class CliView implements CommandLineRunner {

    // Поле зберігає сервіс для роботи з типами.
    private final TypeService typeService;

    // Поле зберігає сервіс для роботи з проєктами.
    private final ProjectService projectService;

    // Поле зберігає контролер для парсингу та обробки команд над таблицею Типи.
    private final TypeCommandController typeCommandController;

    // Поле зберігає контролер для парсингу та обробки команд над таблицею Проєкти.
    private final ProjectCommandController projectCommandController;

    // Поле зберігає сканер для читання введення користувача з консолі.
    private final Scanner scanner = new Scanner(System.in);

    // Загальний прапорець для відображення інформації із зв'язаних таблиць.
    private static final boolean SHOW_EXTENDED_INFO_VIEW = true;

    // Конструктор ініціалізує консольний інтерфейс сервісами та контролерами типів і проєктів.
    public CliView(TypeService typeService,
                   ProjectService projectService,
                   TypeCommandController typeCommandController,
                   ProjectCommandController projectCommandController) {
        this.typeService = typeService;
        this.projectService = projectService;
        this.typeCommandController = typeCommandController;
        this.projectCommandController = projectCommandController;
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
            System.out.print("Введіть команду (наприклад, 'C Backend', або 'q' для повернення у головне меню): ");

            String commandLine = scanner.nextLine().trim();
            if (commandLine.equalsIgnoreCase("q")) {
                return;
            }

            // Таблиця порожня → передаємо true у параметр tableEmpty контролера.
            String result = typeCommandController.handleCommand(commandLine, true);
            System.out.println(result);
            return;
        }

        System.out.println("ID | Назва");
        for (Type type : types) {
            System.out.println(type.getId() + " | " + type.getName());
        }

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть команду (наприклад, 'C Backend', '2 U NewName', '3 D', або 'q' для повернення): ");

        String commandLine = scanner.nextLine().trim();
        if (commandLine.equalsIgnoreCase("q")) {
            return;
        }

        // Таблиця не порожня → передаємо false у параметр tableEmpty контролера.
        String result = typeCommandController.handleCommand(commandLine, false);
        System.out.println(result);
    }

    // Метод обробляє роботу з таблицею проєктів.
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

        if (SHOW_EXTENDED_INFO_VIEW) {
            // Розширений вигляд, коли показуємо ще й ID типу.
            System.out.println("ID | URL | Назва Типу | ID Типу");
            for (Project project : projects) {
                String typeName = project.getType() != null
                        ? project.getType().getName()
                        : "null";
                String typeId = project.getType() != null
                        ? String.valueOf(project.getType().getId())
                        : "null";
                System.out.println(project.getId() + " | " + project.getUrl()
                        + " | " + typeName + " | " + typeId);
            }
        } else {
            // Стандартний вигляд, коли показуємо як було, тобто без ID типу.
            System.out.println("ID | URL | Назва Типу");
            for (Project project : projects) {
                String typeName = project.getType() != null
                        ? project.getType().getName()
                        : "null";
                System.out.println(project.getId() + " | " + project.getUrl()
                        + " | " + typeName);
            }
        }

        System.out.println("Доступні дії: C - create, U - update, D - delete");
        System.out.print("Введіть команду (наприклад, 'C url type_name', '2 U url type_name', '3 D', або 'q' для повернення): ");

        String commandLine = scanner.nextLine().trim();
        if (commandLine.equalsIgnoreCase("q")) {
            return;
        }

        String result = projectCommandController.handleCommand(commandLine, false);
        System.out.println(result);
    }

    // Метод відображає вміст зв'язуючої таблиці Типи Проєктів лише для перегляду.
    private void handleTypesProjects() {
        List<Project> projects = projectService.getAll();

        if (SHOW_EXTENDED_INFO_VIEW) {
            // Розширений вигляд, коли показуємо ще й назву типу.
            System.out.println("Зв'язуюча таблиця Типи Проєктів (розширена):");
            System.out.println("Project ID | Type ID | Type Name");

            boolean hasLinks = false;
            for (Project project : projects) {
                if (project.getType() != null) {
                    hasLinks = true;
                    System.out.println(
                            project.getId() + " | "
                                    + project.getType().getId() + " | "
                                    + project.getType().getName()
                    );
                }
            }

            if (!hasLinks) {
                System.out.println("Таблиця порожня (немає жодного зв'язку Project-Type).");
            }
        } else {
            // Стандартний вигляд, коли показуємо лише ID, тобто без назви типу.
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
        }

        System.out.print("Введіть 'q' для повернення у головне меню: ");
        scanner.nextLine();
    }
}