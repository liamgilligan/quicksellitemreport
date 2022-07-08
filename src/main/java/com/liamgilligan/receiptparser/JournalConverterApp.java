package com.liamgilligan.receiptparser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class JournalConverterApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        List<File> fileList = choose(primaryStage);
        Parser parser = new Parser();
        List<Item> finalList = parser.parseFile(fileList);
        try (
                FileWriter fos = new FileWriter("itemquantity.csv");
                PrintWriter dos = new PrintWriter(fos)
        ){
        for (Item i : finalList) {
            dos.print(i.itemDescription()+"\t");
            dos.print(i.price()+"\t");
            dos.print(i.quantity()+"\t");
            dos.println();
        }
    }
        Platform.exit();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public List<File> choose(Stage ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Journal Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Journal Files", "*.JOU"));
        return fileChooser.showOpenMultipleDialog(ownerWindow);
    }

}
