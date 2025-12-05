package org.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO для передачі даних проєкту через REST API
// Анотація генерує getters, setters, toString, equals, hashCode
@Data
// Анотація генерує конструктор без параметрів для десеріалізації JSON
@NoArgsConstructor
// Анотація генерує конструктор з усіма полями
@AllArgsConstructor
public class ProjectDto {
    // Поле для зберігання ідентифікатора проєкту
    private Long id;
    // Поле для зберігання URL репозиторію
    private String url;
    // Поле для зберігання назви типу проєкту
    private String typeName;
    // Поле для зберігання ідентифікатора типу
    private Long typeId;
}
