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

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.LogManager;


public class FileWriter {



    //Properties.setProperty("java.util.logging.config.file", "jul-log.properties")
    final static Logger logger = LoggerFactory.getLogger(Parser.class);
    private void writeErrorFile(List<ParserReturn.ErrorMessage> errorMessageList, List<ParserReturn.Item> itemList) {
        Integer brokenLines = errorMessageList.size();
        int totalLines = itemList.size() + errorMessageList.size();
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(brokenLines + " lines could not be read, " + brokenLines + " out of " + totalLines + " lines failed.");
        errorAlert.showAndWait();

        InputStream inputStream = JournalConverterApp.class.getResourceAsStream("jul-log.properties");
        if(inputStream != null) {
            try {
                LogManager.getLogManager().readConfiguration(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("JavaLoggingSupport: failed to process " + "jul-log.properties");
            }
        } else {
            System.err.println("JavaLoggingSupport: failed to load " + "jul-log.properties");
        }

        for (ParserReturn.ErrorMessage e : errorMessageList) {
            logger.error(e.currentLine() + e.previousLine(), e.error());
        }

    }
    public void writeTopPerformersOutputFile(List<ParserReturn.Item> itemList, List<ParserReturn.ErrorMessage> errorMessageList, String saveFilePath) {
        try {
            BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(saveFilePath));
            for (ParserReturn.Item i : itemList) {
                String lineToAppend = (i.itemDescription() + "\t" + i.price() + "\t" + i.quantity());
                bw.write(lineToAppend);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch(IOException e) {
        }
        if (!errorMessageList.isEmpty()) {
            writeErrorFile(errorMessageList, itemList);
        }
    }

}
