package org.example.lab4.view;

import java.util.List;

// Клас надає утилітний метод для виводу таблиць з вирівнюванням колонок
public class TablePrinter {

    // Виводить таблицю з заданими заголовками і рядками, вирівнюючи колонки по лівому краю
    public static void printTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || headers.isEmpty()) {
            throw new IllegalArgumentException("Список заголовків не може бути порожнім.");
        }

        int columnCount = headers.size();
        int[] widths = new int[columnCount];

        // Початково ширини колонок дорівнюють довжинам заголовків
        for (int i = 0; i < columnCount; i++) {
            widths[i] = headers.get(i).length();
        }

        // Оновлюємо ширини за значеннями в рядках
        if (rows != null) {
            for (List<String> row : rows) {
                for (int i = 0; i < columnCount; i++) {
                    String cell = (row.size() > i && row.get(i) != null) ? row.get(i) : "";
                    widths[i] = Math.max(widths[i], cell.length());
                }
            }
        }

        // Будуємо форматний рядок, типу "%-3s | %-10s | %-5s%n"
        StringBuilder formatBuilder = new StringBuilder();
        for (int i = 0; i < columnCount; i++) {
            formatBuilder.append("%-").append(widths[i]).append("s");
            if (i < columnCount - 1) {
                formatBuilder.append(" | ");
            } else {
                formatBuilder.append("%n");
            }
        }
        String format = formatBuilder.toString();

        // Виводимо заголовок
        System.out.printf(format, headers.toArray());

        // Виводимо рядки даних
        if (rows != null) {
            for (List<String> row : rows) {
                Object[] cells = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    cells[i] = (row.size() > i && row.get(i) != null) ? row.get(i) : "";
                }
                System.out.printf(format, cells);
            }
        }
    }
}