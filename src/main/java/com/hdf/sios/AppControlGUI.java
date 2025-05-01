package com.hdf.sios;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.boot.SpringApplication;

import java.awt.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class AppControlGUI extends Application{

	private static SpringApplication springApplication;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	    primaryStage.setTitle("Управление приложением SIOS");

	    // Создаем кнопки
	    Button stopButton = new Button("Остановить приложение");
	    Button openPhotosButton = new Button("Открыть папку с фото");
	    Button openBrowserButton = new Button("Открыть в браузере");

	    // Настройка действий для кнопок
//	    stopButton.setOnAction(e -> stopApplication());
	    openPhotosButton.setOnAction(e -> openPhotosDirectory());
	    openBrowserButton.setOnAction(e -> openBrowser("http://localhost:8080/"));

	 // Создаем горизонтальный layout (HBox вместо VBox)
	    HBox hbox = new HBox(10, stopButton, openPhotosButton, openBrowserButton);
	    hbox.setStyle("-fx-padding: 15; -fx-alignment: center;");

	    // Делаем кнопки одинаковой ширины (опционально)
	    stopButton.setPrefWidth(150);
	    openPhotosButton.setPrefWidth(150);
	    openBrowserButton.setPrefWidth(150);
	    
	    // Основной контейнер (прижимаем HBox к верху)
	    BorderPane root = new BorderPane();
	    root.setTop(hbox); // Кнопки будут вверху
	    
	    // Настраиваем обработчик закрытия окна
	    primaryStage.setOnCloseRequest(event -> {
	        event.consume(); // Предотвращаем стандартное закрытие
	        stopApplication();
	    });

	    // Настраиваем сцену
	    Scene scene = new Scene(root, 300, 200);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	private void stopApplication() {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Подтверждение выхода");
	    alert.setHeaderText("Вы уверены, что хотите закрыть приложение?");
	    alert.setContentText("Все несохраненные данные могут быть потеряны.");

	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        Platform.exit();
	        System.exit(0);
	    }
	}
	
	private void openPhotosDirectory() {
		System.setProperty("java.awt.headless", "false");
		System.out.println("Headless mode: " + GraphicsEnvironment.isHeadless());
        try {
         // Всегда берём текущую рабочую директорию
            Path jarDir = Paths.get(System.getProperty("user.dir"));
            String photosPath = jarDir.resolve("photos").toAbsolutePath().toString();
            System.err.println(photosPath);
            File photosDir = new File(photosPath);
            if (photosDir.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(photosDir);
                }
            } else {
                // Если папка не существует, можно открыть диалог выбора папки
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Выберите папку с фото");
                Stage stage = new Stage();
                File selectedDirectory = directoryChooser.showDialog(stage);
                
                if (selectedDirectory != null) {
                    Desktop.getDesktop().open(selectedDirectory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void openBrowser(String url) {
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
