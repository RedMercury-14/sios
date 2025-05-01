package com.hdf.sios.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

@Controller
public class MainController {

    private Path uploadPath;

    @PostConstruct
    public void init() {
        try {
            // Всегда берём текущую рабочую директорию
            Path jarDir = Paths.get(System.getProperty("user.dir"));

            // Папка 'photos' рядом с .jar
            uploadPath = jarDir.resolve("photos");
            Files.createDirectories(uploadPath);

            System.out.println("📁 Фото будут сохраняться в: " + uploadPath.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Ошибка при определении директории сохранения файлов:");
            e.printStackTrace();
        }
    }




    @GetMapping("/")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/arrayUpload")
    public String handleMultipleUploadArray(@RequestParam("files") List<MultipartFile> files, Model model) {
        if (files == null || files.isEmpty()) {
            model.addAttribute("message", "Файлы не выбраны");
            return "upload";
        }

        int successCount = 0;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path destination = uploadPath.resolve(filename);
                    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    successCount++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        model.addAttribute("message", "Загружено файлов: " + successCount);
        return "upload";
    }
    
    
    
    
    @PostMapping("/upload")
    public String handleMultipleUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file == null || file.isEmpty()) {
            model.addAttribute("message", "Файлы не выбраны");
            return "upload";
        }

        int successCount = 0;
        if (!file.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path destination = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                successCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("message", "Загружено файлов: " + successCount);
        return "upload";
    }
}
