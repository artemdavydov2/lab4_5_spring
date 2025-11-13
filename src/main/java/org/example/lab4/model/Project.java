package org.example.lab4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Сутність представляє проєкт у таблиці Projects.
@Getter
@Entity
@Table(name = "Projects")
public class Project {

    // Зберігає ідентифікатор проєкту як автоінкрементний ключ.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    // Зберігає унікальний URL репозиторію проєкту.
    @Setter
    @Column(name = "project_url", unique = true, nullable = false)
    private String url;

    // Зберігає тип проєкту через проміжну таблицю Types_Projects.
    @Setter
    @ManyToOne
    @JoinTable(
            name = "Types_Projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Type type;

    // Встановлює ідентифікатор проєкту для JPA (використовується фреймворком).
    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }
}