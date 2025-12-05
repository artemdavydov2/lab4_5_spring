package org.example.lab4.rest;

import org.example.lab4.dto.ApiResponse;
import org.example.lab4.dto.ProjectDto;
import org.example.lab4.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// REST контролер для CRUD операцій над проєктами
@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {

    // Поле зберігає сервіс для роботи з проєктами
    private final ProjectService projectService;

    // Конструктор ініціалізує контролер сервісом проєктів
    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /api/projects повертає список усіх проєктів
    // Викликає projectService.getAll() для отримання списку Project з БД
    // Перетворює кожен Project на ProjectDto через stream і map
    // Для кожного проєкту витягує дані типу (якщо є)
    // Збирає результат у List та повертає з HTTP 200 OK
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAll() {
        List<ProjectDto> projects = projectService.getAll().stream()
                .map(p -> new ProjectDto(
                        p.getId(),
                        p.getUrl(),
                        p.getType() != null ? p.getType().getName() : null,
                        p.getType() != null ? p.getType().getId() : null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    // GET /api/projects/{id} повертає проєкт за ID
    // Приймає ID з URL через @PathVariable
    // Викликає projectService.findById(id) для пошуку проєкту
    // Якщо знайдено — створює ProjectDto з даними типу та повертає HTTP 200
    // Якщо не знайдено — повертає помилку з HTTP 404
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(p -> {
                    ProjectDto dto = new ProjectDto(
                            p.getId(),
                            p.getUrl(),
                            p.getType() != null ? p.getType().getName() : null,
                            p.getType() != null ? p.getType().getId() : null);
                    return ResponseEntity.ok(ApiResponse.success("OK", dto));
                })
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Проєкт з ID " + id + " не знайдено.")));
    }

    // POST /api/projects створює новий проєкт
    // Приймає ProjectDto з тіла запиту через @RequestBody
    // Викликає projectService.createProject(url, typeName)
    // Сервіс перевіряє унікальність URL та існування типу
    // Якщо результат містить "створено" — повертає HTTP 201 Created
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody ProjectDto dto) {
        String result = projectService.createProject(dto.getUrl(), dto.getTypeName());
        if (result.contains("створено")) {
            return ResponseEntity.status(201).body(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }

    // PUT /api/projects/{id} оновлює проєкт за ID
    // Приймає ID з URL та ProjectDto з тіла запиту
    // Викликає projectService.updateProject(id, url, typeName)
    // Сервіс перевіряє існування проєкту та унікальність URL
    // Якщо результат містить "оновлено" — повертає HTTP 200 OK
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody ProjectDto dto) {
        String result = projectService.updateProject(id, dto.getUrl(), dto.getTypeName());
        if (result.contains("оновлено")) {
            return ResponseEntity.ok(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }

    // DELETE /api/projects/{id} видаляє проєкт за ID
    // Приймає ID з URL через @PathVariable
    // Викликає projectService.deleteProject(id)
    // Сервіс перевіряє існування проєкту перед видаленням
    // Якщо результат містить "видалено" — повертає HTTP 200 OK
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        String result = projectService.deleteProject(id);
        if (result.contains("видалено")) {
            return ResponseEntity.ok(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }
}
