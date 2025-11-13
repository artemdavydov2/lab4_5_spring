package org.example.lab4.service;

import org.example.lab4.model.Type;
import org.example.lab4.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Клас інкапсулює бізнес-логіку CRUD для сутності Type.
@Service
public class TypeService {

    // Поле зберігає репозиторій для доступу до таблиці Types.
    private final TypeRepository typeRepository;

    // Конструктор ініціалізує сервіс репозиторієм типів.
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    // Повертає список усіх типів із бази даних.
    public List<Type> getAll() {
        return typeRepository.findAll();
    }

    // Створює новий тип з переданою назвою.
    public String createType(String name) {
        if (name == null || name.isBlank()) {
            return "Вкажіть назву для створення.";
        }
        Type type = new Type();
        type.setName(name.trim());
        try {
            typeRepository.save(type);
            return "Тип створено.";
        } catch (Exception e) {
            return "Помилка при створенні типу: " + e.getMessage();
        }
    }

    // Оновлює назву існуючого типу за його ID.
    public String updateType(Long id, String newName) {
        if (id == null || newName == null || newName.isBlank()) {
            return "Вкажіть ID та нову назву для оновлення.";
        }
        Optional<Type> optionalType = typeRepository.findById(id);
        if (optionalType.isEmpty()) {
            return "Тип з ID " + id + " не знайдено.";
        }
        Type type = optionalType.get();
        type.setName(newName.trim());
        try {
            typeRepository.save(type);
            return "Тип оновлено.";
        } catch (Exception e) {
            return "Помилка при оновленні типу: " + e.getMessage();
        }
    }

    // Видаляє тип за переданим ID.
    public String deleteType(Long id) {
        if (id == null) {
            return "Вкажіть ID для видалення.";
        }
        if (!typeRepository.existsById(id)) {
            return "Тип з ID " + id + " не знайдено.";
        }
        try {
            typeRepository.deleteById(id);
            return "Тип видалено.";
        } catch (Exception e) {
            return "Помилка при видаленні типу: " + e.getMessage();
        }
    }
}