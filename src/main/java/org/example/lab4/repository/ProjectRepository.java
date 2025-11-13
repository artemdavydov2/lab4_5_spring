package org.example.lab4.repository;

import org.example.lab4.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Інтерфейс надає CRUD-операції та пошук проєктів у таблиці Projects.
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Повертає проєкт за його унікальним URL.
    Optional<Project> findByUrl(String url);
}