package org.example.client;

// Точка входу для консольного клієнта REST API
public class ClientApplication {

    // Метод запускає консольне меню клієнта
    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu();
        menu.run();
    }
}
