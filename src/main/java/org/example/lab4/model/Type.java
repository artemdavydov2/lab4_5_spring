package org.example.lab4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Сутність представляє тип проєкту в таблиці Types
@Getter
@Entity
@Table(name = "Types")
public class Type {

    // Зберігає ідентифікатор типу як автоінкрементний ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    // Зберігає унікальну назву типу
    @Setter
    @Column(name = "type_name", unique = true, nullable = false)
    private String name;

    // Встановлює ідентифікатор типу для JPA (використовується фреймворком)
    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }
}
