package org.example.lab4.repository;

import org.example.lab4.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Інтерфейс надає CRUD-операції та пошук типів у таблиці Types.
@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    // Повертає тип за його унікальною назвою.
    Optional<Type> findByName(String name);
}