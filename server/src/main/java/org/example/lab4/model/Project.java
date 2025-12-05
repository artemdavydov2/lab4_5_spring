package org.example.lab4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Сутність представляє проєкт у таблиці Projects
// Анотація генерує getter методи для всіх полів
@Getter
// Анотація позначає клас як JPA сутність
@Entity
// Анотація вказує назву таблиці в базі даних
@Table(name = "Projects")
public class Project {

    // Анотація позначає поле як первинний ключ
    @Id
    // Анотація вказує стратегію генерації ідентифікатора
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Анотація вказує назву колонки в таблиці
    @Column(name = "project_id")
    // Поле для зберігання ідентифікатора проєкту
    private Long id;

    // Анотація генерує setter метод для поля
    @Setter
    // Анотація вказує налаштування колонки в таблиці
    @Column(name = "project_url", unique = true, nullable = false)
    // Поле для зберігання URL репозиторію
    private String url;

    // Анотація генерує setter метод для поля
    @Setter
    // Анотація позначає зв'язок багато-до-одного з Type
    @ManyToOne
    // Анотація налаштовує проміжну таблицю для зв'язку
    @JoinTable(name = "Types_Projects", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "type_id"))
    // Поле для зберігання пов'язаного типу
    private Type type;

    // Метод встановлює значення ідентифікатора для JPA
    @SuppressWarnings("unused")
    public void setId(Long id) {
        // Присвоєння значення полю id
        this.id = id;
    }
}