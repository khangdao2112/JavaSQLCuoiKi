package app;

import app.program.*;

public class Main {
    public static void main(String[] args) {
        try {
            MainProgram.login();
            MainProgram.menu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}