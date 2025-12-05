package org.example.lab4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Сутність представляє тип проєкту в таблиці Types
// Анотація генерує getter методи для всіх полів
@Getter
// Анотація позначає клас як JPA сутність
@Entity
// Анотація вказує назву таблиці в базі даних
@Table(name = "Types")
public class Type {

    // Анотація позначає поле як первинний ключ
    @Id
    // Анотація вказує стратегію генерації ідентифікатора
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Анотація вказує назву колонки в таблиці
    @Column(name = "type_id")
    // Поле для зберігання ідентифікатора типу
    private Long id;

    // Анотація генерує setter метод для поля
    @Setter
    // Анотація вказує налаштування колонки в таблиці
    @Column(name = "type_name", unique = true, nullable = false)
    // Поле для зберігання назви типу
    private String name;

    // Метод встановлює значення ідентифікатора для JPA
    @SuppressWarnings("unused")
    public void setId(Long id) {
        // Присвоєння значення полю id
        this.id = id;
    }
}
