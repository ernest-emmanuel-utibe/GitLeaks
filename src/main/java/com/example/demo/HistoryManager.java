package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class HistoryManager {
    private final LinkedList<String> history = new LinkedList<>();
    private final int maxHistorySize = 10; // Keep last 10 searches

    public void addSearch(String city) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = city + " - " + timestamp;

        // Add newest at front
        history.addFirst(entry);

        // Remove oldest if exceeds max size
        if (history.size() > maxHistorySize) {
            history.removeLast();
        }
    }

    public List<String> getHistory() {
        return history;
    }

}
