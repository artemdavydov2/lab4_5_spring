package org.example.lab4.rest;

import org.example.lab4.dto.ApiResponse;
import org.example.lab4.dto.TypeDto;
import org.example.lab4.service.TypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// REST контролер для CRUD операцій над типами проєктів
@RestController
@RequestMapping("/api/types")
public class TypeRestController {

    // Поле зберігає сервіс для роботи з типами
    private final TypeService typeService;

    // Конструктор ініціалізує контролер сервісом типів
    public TypeRestController(TypeService typeService) {
        this.typeService = typeService;
    }

    // GET /api/types повертає список усіх типів
    // Викликає typeService.getAll() для отримання списку Type з БД
    // Перетворює кожен Type на TypeDto через stream і map
    // Збирає результат у List та повертає з HTTP 200 OK
    @GetMapping
    public ResponseEntity<List<TypeDto>> getAll() {
        List<TypeDto> types = typeService.getAll().stream()
                .map(t -> new TypeDto(t.getId(), t.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }

    // GET /api/types/{id} повертає тип за ID
    // Приймає ID з URL через @PathVariable
    // Викликає typeService.findById(id) для пошуку типу
    // Якщо знайдено — повертає TypeDto з HTTP 200 OK
    // Якщо не знайдено — повертає помилку з HTTP 404
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return typeService.findById(id)
                .map(t -> ResponseEntity.ok(ApiResponse.success("OK", new TypeDto(t.getId(), t.getName()))))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Тип з ID " + id + " не знайдено.")));
    }

    // POST /api/types створює новий тип
    // Приймає TypeDto з тіла запиту через @RequestBody
    // Викликає typeService.createType(name) для створення типу
    // Якщо результат містить "створено" — повертає HTTP 201 Created
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody TypeDto dto) {
        String result = typeService.createType(dto.getName());
        if (result.contains("створено")) {
            return ResponseEntity.status(201).body(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }

    // PUT /api/types/{id} оновлює тип за ID
    // Приймає ID з URL та TypeDto з тіла запиту
    // Викликає typeService.updateType(id, newName) для оновлення
    // Якщо результат містить "оновлено" — повертає HTTP 200 OK
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody TypeDto dto) {
        String result = typeService.updateType(id, dto.getName());
        if (result.contains("оновлено")) {
            return ResponseEntity.ok(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }

    // DELETE /api/types/{id} видаляє тип за ID
    // Приймає ID з URL через @PathVariable
    // Викликає typeService.deleteType(id) для видалення
    // Якщо результат містить "видалено" — повертає HTTP 200 OK
    // Інакше — повертає помилку з HTTP 400 Bad Request
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        String result = typeService.deleteType(id);
        if (result.contains("видалено")) {
            return ResponseEntity.ok(ApiResponse.success(result));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(result));
    }
}
