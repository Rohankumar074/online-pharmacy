package com.onlinepharmacy.notification;

public class TestDotenv {
    public static void main(String[] args) {
        try {
            io.github.cdimascio.dotenv.Dotenv d = io.github.cdimascio.dotenv.Dotenv.configure().directory("..").load();
            System.out.println("Loaded from ..: " + d.get("MAIL_USERNAME"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
