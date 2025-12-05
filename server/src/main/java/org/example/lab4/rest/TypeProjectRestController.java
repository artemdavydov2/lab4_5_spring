package org.example.lab4.rest;

import org.example.lab4.dto.TypeProjectDto;
import org.example.lab4.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// REST контролер для отримання зв'язків між типами та проєктами
@RestController
@RequestMapping("/api/types-projects")
public class TypeProjectRestController {

    // Поле зберігає сервіс для роботи з проєктами
    private final ProjectService projectService;

    // Конструктор ініціалізує контролер сервісом проєктів
    public TypeProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /api/types-projects повертає список зв'язків між типами і проєктами
    // Викликає projectService.getAll() для отримання всіх проєктів
    // Фільтрує проєкти, залишаючи лише ті, що мають тип
    // Для кожного проєкту створює TypeProjectDto з projectId, typeId, typeName
    // Збирає результат у List та повертає з HTTP 200 OK
    @GetMapping
    public ResponseEntity<List<TypeProjectDto>> getAll() {
        List<TypeProjectDto> typesProjects = projectService.getAll().stream()
                .filter(p -> p.getType() != null)
                .map(p -> new TypeProjectDto(
                        p.getId(),
                        p.getType().getId(),
                        p.getType().getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(typesProjects);
    }
}
