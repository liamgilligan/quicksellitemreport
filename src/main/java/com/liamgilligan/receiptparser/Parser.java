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
    static final Pattern decimalPattern  = Pattern.compile("\\d+\\.\\d{2}");
    static final Pattern refundPattern  = Pattern.compile("-\\d+\\.\\d{2}");
    static final Pattern itemDescriptionPattern  = Pattern.compile("^\\S+(\\s{1,2}\\S+)*");
    static final Pattern startBodyPattern = Pattern.compile("www.MysterySpot.com");
    static final Pattern endBodyPattern = Pattern.compile("((--------)|(Cash Pay Out))");
    static final Pattern quantityPattern = Pattern.compile("\\s-?\\d+\\s");


    //Methods for returning whether a pattern appears.
    private boolean containsDecimal(String currentLine){
        Matcher matcher = decimalPattern.matcher(currentLine);
        return matcher.find();
    }

    private boolean containsRefund(String currentLine){
        Matcher matcher = refundPattern.matcher(currentLine);
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

    //Methods for returning a string that matches the pattern
    private String getDecimal(String currentLine){
        Matcher matcher = decimalPattern.matcher(currentLine);
        matcher.find();
        return matcher.group();
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
        return containsDecimal(currentLine) && containsItemDescription(previousLine) && !containsRefund(currentLine);
    }

    private boolean isRefund(String currentLine, String previousLine) {
        return containsRefund(currentLine) && containsItemDescription(previousLine);
    }

    //Methods for creating Item objects based on the case

    private void createSingleTransactionItem(String currentLine, List<Item> finalList) {
        Item item = new Item(getItemDescription(currentLine), getDecimal(currentLine), "1");
        finalList.add(item);
    }

    private void createMultiTransactionItem(String currentLine, String previousLine, List<Item> finalList) {
        Item item = new Item(getItemDescription(previousLine), getDecimal(currentLine), getQuantity(currentLine).trim());
        finalList.add(item);
    }

    private void createRefundItem(String currentLine, String previousLine, List<Item> finalList) {
        Item item = new Item(getItemDescription(previousLine), getDecimal(currentLine), getQuantity(currentLine).trim());
        finalList.add(item);
    }


    public List<Item> parseFile(List<File> fileList) throws IOException {
        boolean inBody = false;
        List<Item> finalList = new ArrayList<>();
        String previousLine = null;
        for (File currentFile : fileList){
            List<String> receiptList = Files.readAllLines(Paths.get(String.valueOf(currentFile)));
            for (String currentLine : receiptList) {
                if (inBody) {
                    if (isSingleTransaction(currentLine)){
                        createSingleTransactionItem(currentLine, finalList);
                    } else if (isMultiTransaction(currentLine, previousLine)) {
                        createMultiTransactionItem(currentLine, previousLine, finalList);
                    } else if (isRefund(currentLine, previousLine)) {
                        createRefundItem(currentLine, previousLine, finalList);
                    } else if (containsEndBody(currentLine)){
                        inBody = false;
                    }
                } else if (containsStartBody(currentLine)) {
                    inBody = true;
                }
                previousLine = currentLine;
            }
        }
        return finalList;
    }
}

