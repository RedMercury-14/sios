package com.hdf.sios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

import java.io.IOException;

@SpringBootApplication
public class SiosApplication {

	public static void main(String[] args) {		
//		SpringApplication.run(SiosApplication.class, args);
//		System.err.println("С наилучшеми пожеланиями всей корпоративной культуре от Грушевского Дмитрия");
//		openBrowser("http://localhost:8080/");
		
		new Thread(() -> {
            SpringApplication.run(SiosApplication.class, args);
            System.err.println("С наилучшими пожеланиями всей корпоративной культуре от Грушевского Дмитрия");
        }).start();

        // Запускаем JavaFX GUI
        Application.launch(AppControlGUI.class, args);
	}
	
	private static void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                // Для Windows
                Runtime.getRuntime().exec("cmd /c start " + url);
            } else if (os.contains("mac")) {
                // Для macOS
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Для Linux
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                System.out.println("Не поддерживается данная операционная система.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
