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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    //RegEx patterns
    static final Pattern decimalPattern  = Pattern.compile("-?\\d+\\.\\d{2}");
    static final Pattern itemDescriptionPattern  = Pattern.compile("^\\S+(\\s{1,2}\\S+)*");
    static final Pattern startBodyPattern = Pattern.compile("www.MysterySpot.com");
    static final Pattern endBodyPattern = Pattern.compile("((--------)|(Cash Pay Out))");
    static final Pattern quantityPattern = Pattern.compile("\\s-?\\d+\\.?\\d{0,2}\\s");



    //Methods for returning whether a pattern appears.
    private boolean containsDecimal(String currentLine){
        Matcher matcher = decimalPattern.matcher(currentLine);
        return matcher.find();
    }

    private boolean containsItemDescription(String currentLine){
        Matcher matcher = itemDescriptionPattern.matcher(currentLine);
        return matcher.find();
    }

    private boolean containsStartBody(String currentLine){
        Matcher matcher = startBodyPattern.matcher(currentLine);
        return matcher.find();
    }

    private boolean containsEndBody(String currentLine){
        Matcher matcher = endBodyPattern.matcher(currentLine);
        return matcher.find();
    }

    private boolean containsFractionalQuantity(String currentLine){
        Matcher matcher = decimalPattern.matcher(currentLine);
        return matcher.find() && matcher.find() && matcher.find();
    }

    //Methods for returning a string that matches the pattern
    private String getDecimal(String currentLine){
        Matcher matcher = decimalPattern.matcher(currentLine);
        if (!containsFractionalQuantity(currentLine)) {
            matcher.find();
            return matcher.group();
        } else {
            matcher.find();
            matcher.find();
            return matcher.group();
        }
    }

    private String getItemDescription(String currentLine){
        Matcher matcher = itemDescriptionPattern.matcher(currentLine);
        matcher.find();
        return matcher.group();
    }

    private String getQuantity(String currentLine) {
        Matcher matcher = quantityPattern.matcher(currentLine);
        matcher.find();
        return matcher.group();
    }

    //Methods for determining if given string is a Single Transaction, Multi-transaction, refund, etc
    private boolean isSingleTransaction(String currentLine) {
        return containsDecimal(currentLine) && containsItemDescription(currentLine);
    }

    private boolean isMultiTransaction(String currentLine, String previousLine) {
        return containsDecimal(currentLine) && containsItemDescription(previousLine);
    }

    //Methods for creating Item objects based on the case

    private void createSingleTransactionItem(String currentLine, List<ParserReturn.Item> itemList) {
        String itemDescription = getItemDescription(currentLine).trim();
        String itemCost = getDecimal(currentLine).trim();
        ParserReturn.Item item = new ParserReturn
                .Item(itemDescription, itemCost, "1");
        itemList.add(item);
    }

    private void createMultiTransactionItem(String currentLine, String previousLine, List<ParserReturn.Item> itemList) {
        String itemDescription = getItemDescription(previousLine).trim();
        String itemCost = getDecimal(currentLine).trim();
        String quantity = getQuantity(currentLine).trim();
        ParserReturn.Item item = new ParserReturn
                .Item(itemDescription, itemCost, quantity);
        itemList.add(item);
    }

    public ParserReturn parseFile(List<File> fileList) throws IOException {
        boolean inBody = false;
        List<ParserReturn.Item> itemList = new ArrayList<>();
        List<ParserReturn.ErrorMessage> errorList = new ArrayList<>();
        String previousLine = null;
        for (File currentFile : fileList){
            List<String> receiptList = Files
                    .readAllLines(Paths.get(String.valueOf(currentFile)));
            for (String currentLine : receiptList) {
                try {
                    if (inBody) {
                        if (isSingleTransaction(currentLine)) {
                            createSingleTransactionItem(currentLine, itemList);
                        } else if (isMultiTransaction(currentLine, previousLine)) {
                            createMultiTransactionItem(currentLine, previousLine, itemList);
                        } else if (containsEndBody(currentLine)) {
                            inBody = false;
                        }
                    } else if (containsStartBody(currentLine)) {
                        inBody = true;
                    }
                } catch(Exception e) {
                    ParserReturn.ErrorMessage errorMessage = new ParserReturn
                            .ErrorMessage(previousLine, currentLine, e);
                    errorList.add(errorMessage);
                }
                previousLine = currentLine;
            }
        }
        return new ParserReturn(itemList, errorList);
    }
}

