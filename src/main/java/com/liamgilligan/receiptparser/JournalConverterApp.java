package com.liamgilligan.receiptparser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class JournalConverterApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();

        List<File> fileList = chooseJournalFiles(primaryStage);

        File outputFile = createOutputFile(primaryStage);
        String outputFilePath = outputFile.getAbsolutePath();

        Parser parser = new Parser();
        List<Item> finalList = parser.parseFile(fileList);

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
        for (Item i:finalList) {
            String lineToAppend = (i.itemDescription() + "\t" + i.price() + "\t" + i.quantity());
            bw.write(lineToAppend);
            bw.newLine();
        }
        bw.flush();
        bw.close();

        Platform.exit();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public List<File> chooseJournalFiles(Stage ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Journal Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Journal Files (*.JOU)", "*.JOU"));
        return fileChooser.showOpenMultipleDialog(ownerWindow);
    }
    public File createOutputFile(Stage ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Output Location");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tab Seperated Values (*.tsv)", "*.tsv"));
        return fileChooser.showSaveDialog(ownerWindow);
    }
}
