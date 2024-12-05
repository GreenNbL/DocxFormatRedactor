package org.example.docxformatredactor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Controller {
    @FXML
    private Button buttonFileChoose;

    @FXML
    private Button buttonFileEdit;

    @FXML
    private Label labelFileSelected;

    @FXML
    private Label labelProcessFormatation;

    @FXML
    private TextField textFieldHeight;

    @FXML
    private TextField textFieldWidth;

    private Stage primaryStage;

    private List<String> docxFiles;

    private String saveDirectory;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        labelFileSelected.setVisible(false);
        labelProcessFormatation.setVisible(false);
        // Обработчик события для кнопки выбора файла
        buttonFileChoose.setOnAction((ActionEvent event) -> openFiles());
        buttonFileEdit.setOnAction(event -> saveFiles());
    }

    // Метод для выбора директории
    private void chooseDirectory() {
        // Создаем DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку для сохранения файлов");

        // Открываем диалоговое окно выбора директории
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        // Проверяем, была ли выбрана директория
        if (selectedDirectory != null) {
            this.saveDirectory=selectedDirectory.getAbsolutePath();
            System.out.println("Выбранная папка: " + selectedDirectory.getAbsolutePath());
            // Здесь вы можете сохранить путь к выбранной папке для дальнейшего использования
        } else {
            System.out.println("Папка не выбрана");
        }
    }
    private void editFormatFiles()
    {
        for(String filePath: docxFiles) {
            try (XWPFDocument document = new XWPFDocument(new FileInputStream(filePath))) {
                // Установка размеров страниц (A3)
                int width=(int) Math.round(Double.parseDouble(textFieldWidth.getText())*1440/2.54);
                int hieght=(int) Math.round(Double.parseDouble(textFieldHeight.getText())*1440/2.54);
                document.getDocument().getBody().addNewSectPr()
                        .addNewPgSz().setW(width); // ширина A3 в twentieths of a point
                document.getDocument().getBody().getSectPr().getPgSz().setH(hieght); // высота A3

                // Сохранение документа
                String outputFilePath=saveDirectory+"\\";
                int lastIndexSlash = filePath.lastIndexOf("\\");
                String fileNameWithExtension = filePath.substring(lastIndexSlash + 1);
                String[] parts=fileNameWithExtension.split("\\.");
                outputFilePath+=parts[0]+"(formatted)."+parts[1];
                try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
                    document.write(out);
                }
                System.out.println("Формат страницы успешно изменен на A3.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveFiles() {
        labelProcessFormatation.setVisible(true);
        chooseDirectory();
        editFormatFiles();
        labelProcessFormatation.setText("Файлы отформатированы");
    }

    public void openFiles() {
        labelProcessFormatation.setVisible(false);
        labelFileSelected.setVisible(false);
        if(docxFiles!=null)
            docxFiles.clear();
        // Открытие диалогового окна для выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DOCX Files", "*.docx"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            docxFiles= new ArrayList<>();
            docxFiles.add(selectedFile.getAbsolutePath());
            // Обработка, если выбран файл
            System.out.println("Выбран файл: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Файл не выбран ");
            // Если файл не выбран, открываем диалог для выбора папки
            DirectoryChooser directoryChooser = new DirectoryChooser();
            // Выбор папки
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                // Запись всех docx файлов в директории
                docxFiles=findDocxFiles(selectedDirectory);
                System.out.println("Найденные файлы:");
                docxFiles.forEach(System.out::println);
            }
        }
        if(docxFiles!=null) {
            labelFileSelected.setVisible(true);
        }
    }

    // Метод, который находит все .docx файлы в директории
    private List<String> findDocxFiles(File directory) {
        List<String> docxFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Рекурсивный вызов для поиска в подкаталогах
                    docxFiles.addAll(findDocxFiles(file));
                } else
                {
                    if (file.getAbsolutePath().toLowerCase().endsWith(".docx")) {
                        docxFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return docxFiles;
    }


}