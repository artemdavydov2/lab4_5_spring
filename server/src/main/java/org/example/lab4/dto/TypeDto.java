package org.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO для передачі даних типу через REST API
// Анотація генерує getters, setters, toString, equals, hashCode
@Data
// Анотація генерує конструктор без параметрів для десеріалізації JSON
@NoArgsConstructor
// Анотація генерує конструктор з усіма полями
@AllArgsConstructor
public class TypeDto {
    // Поле для зберігання ідентифікатора типу
    private Long id;
    // Поле для зберігання назви типу
    private String name;
}
