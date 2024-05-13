package app;

import app.program.*;

public class Main {
    public static void main(String[] args) {
        try {
            MainProgram mainProgram = new MainProgram();
            mainProgram.login();
            mainProgram.menu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}