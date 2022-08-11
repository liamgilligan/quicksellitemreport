/*
    QuickSell Item Report Generator - Generates Top Performing Item Lists
    Copyright (C) 2022 Liam Gilligan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liamgilligan.receiptparser;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    public void showAboutScreen(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AboutScreen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void showTutorialScreen(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TutorialScreen.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void createTopPerformersOutputFile(ActionEvent e) throws IOException {
        //Gets Stage from Action Event
        Node source = (Node) e.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();

        //Opens a file selector menu and creates a List of Files from the selection
        List<File> fileList = chooseJournalFiles(primaryStage);

        //Opens a file save location menu and grabs its file path
        File outputFile = createOutputFile(primaryStage);
        String outputFilePath = outputFile.getAbsolutePath();

        //Parses the file List into a List of Items
        Parser parser = new Parser();
        List<ParserReturn.Item> finalList = parser.parseFile(fileList).itemList();
        List<ParserReturn.ErrorMessage> errorMessageList = parser.parseFile(fileList).errorList();

        //writes finalList to the save location
        FileWriter writer = new FileWriter();
        writer.writeTopPerformersOutputFile(finalList, errorMessageList, outputFilePath);
    }
    private List<File> chooseJournalFiles(Stage ownerWindow) {
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
