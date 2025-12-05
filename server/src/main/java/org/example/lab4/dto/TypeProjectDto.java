package org.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO для передачі даних зв'язку Тип-Проєкт через REST API
// Анотація генерує getters, setters, toString, equals, hashCode
@Data
// Анотація генерує конструктор без параметрів для десеріалізації JSON
@NoArgsConstructor
// Анотація генерує конструктор з усіма полями
@AllArgsConstructor
public class TypeProjectDto {
    // Поле для зберігання ідентифікатора проєкту
    private Long projectId;
    // Поле для зберігання ідентифікатора типу
    private Long typeId;
    // Поле для зберігання назви типу
    private String typeName;
}
