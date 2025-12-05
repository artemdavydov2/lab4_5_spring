package org.example.lab4.service;

import org.example.lab4.model.Project;
import org.example.lab4.model.Type;
import org.example.lab4.repository.ProjectRepository;
import org.example.lab4.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Клас інкапсулює бізнес-логіку CRUD для сутності Project та її зв'язку з Type
@Service
public class ProjectService {

    // Поле зберігає репозиторій для доступу до таблиці Projects
    private final ProjectRepository projectRepository;

    // Поле зберігає репозиторій для доступу до таблиці Types
    private final TypeRepository typeRepository;

    // Конструктор ініціалізує сервіс репозиторіями проєктів і типів
    public ProjectService(ProjectRepository projectRepository, TypeRepository typeRepository) {
        this.projectRepository = projectRepository;
        this.typeRepository = typeRepository;
    }

    // Повертає список усіх проєктів із бази даних
    // Викликає метод репозиторія для отримання проєктів відсортованих за ID
    public List<Project> getAll() {
        return projectRepository.findAllByOrderByIdAsc();
    }

    // Знаходить проєкт за ID
    // Викликає метод репозиторія findById та повертає Optional
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    // Створює новий проєкт з указаним URL і типом
    // Перевіряє чи URL не порожній
    // Перевіряє унікальність URL серед існуючих проєктів
    // Якщо вказано typeName — шукає тип за назвою та прив'язує до проєкту
    // Зберігає проєкт через репозиторій
    // Повертає повідомлення про результат операції
    public String createProject(String url, String typeName) {
        if (url == null || url.isBlank()) {
            return "Вкажіть URL для створення.";
        }

        String trimmedUrl = url.trim();

        // Перевіряє, чи проєкт з таким URL уже існує
        Optional<Project> existingProject = projectRepository.findByUrl(trimmedUrl);
        if (existingProject.isPresent()) {
            return "Проєкт з таким URL вже існує.";
        }

        Project project = new Project();
        project.setUrl(trimmedUrl);

        if (typeName != null && !typeName.isBlank()) {
            Optional<Type> optionalType = typeRepository.findByName(typeName.trim());
            if (optionalType.isEmpty()) {
                return "Тип з назвою " + typeName + " не знайдено. Створіть тип спочатку.";
            }
            project.setType(optionalType.get());
        }

        try {
            projectRepository.save(project);
            return "Проєкт створено.";
        } catch (Exception e) {
            return "Помилка при створенні проєкту: " + e.getMessage();
        }
    }

    // Оновлює існуючий проєкт за ID, змінюючи URL та тип
    // Перевіряє чи всі параметри не порожні
    // Шукає проєкт за ID у базі даних
    // Перевіряє унікальність нового URL серед інших проєктів
    // Шукає тип за назвою та прив'язує до проєкту
    // Зберігає оновлений проєкт через репозиторій
    // Повертає повідомлення про результат операції
    public String updateProject(Long id, String url, String typeName) {
        if (id == null || url == null || url.isBlank() || typeName == null || typeName.isBlank()) {
            return "Вкажіть ID, url та type_name для оновлення.";
        }

        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            return "Проєкт з ID " + id + " не знайдено.";
        }

        String trimmedUrl = url.trim();

        // Перевіряє, чи заданий URL вже не використовується іншим проєктом
        Optional<Project> projectWithSameUrl = projectRepository.findByUrl(trimmedUrl);
        if (projectWithSameUrl.isPresent() && !projectWithSameUrl.get().getId().equals(id)) {
            return "Проєкт з таким URL вже існує.";
        }

        Optional<Type> optionalType = typeRepository.findByName(typeName.trim());
        if (optionalType.isEmpty()) {
            return "Тип з назвою " + typeName + " не знайдено.";
        }

        Project project = optionalProject.get();
        project.setUrl(trimmedUrl);
        project.setType(optionalType.get());

        try {
            projectRepository.save(project);
            return "Проєкт оновлено.";
        } catch (Exception e) {
            return "Помилка при оновленні проєкту: " + e.getMessage();
        }
    }

    // Видаляє проєкт за переданим ID
    // Перевіряє чи ID не порожній
    // Перевіряє існування проєкту за ID
    // Видаляє проєкт через репозиторій
    // Повертає повідомлення про результат операції
    public String deleteProject(Long id) {
        if (id == null) {
            return "Вкажіть ID для видалення.";
        }
        if (!projectRepository.existsById(id)) {
            return "Проєкт з ID " + id + " не знайдено.";
        }
        try {
            projectRepository.deleteById(id);
            return "Проєкт видалено.";
        } catch (Exception e) {
            return "Помилка при видаленні проєкту: " + e.getMessage();
        }
    }

}