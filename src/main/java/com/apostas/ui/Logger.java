package com.apostas.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private final String nome;

    private Logger(String nome) {
        this.nome = nome;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName());
    }

    public void info(String msg) {
        System.out.println("[INFO] " + agora() + " [" + nome + "] " + msg);
    }

    public void erro(String msg) {
        System.err.println("[ERROR] " + agora() + " [" + nome + "] " + msg);
    }

    private String agora() {
        DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalDateTime.now().format(FMT);
    }
}
