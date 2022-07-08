package com.liamgilligan.receiptparser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private boolean inBody;

    /*
    DEFINING REGEX PATTERNS FOR FUTURE METHODS
     */
    static final Pattern decimalPattern  = Pattern.compile("\\d+\\.\\d{2}");
    static final Pattern refundPattern  = Pattern.compile("-\\d+\\.\\d{2}");
    static final Pattern itemDescriptionPattern  = Pattern.compile("^\\S+(\\s{1,2}\\S+)*");
    static final Pattern websiteLinkPattern = Pattern.compile("www.MysterySpot.com");
    static final Pattern endBodyPattern = Pattern.compile("((--------)|(Cash Pay Out))");
    static final Pattern quantityPattern = Pattern.compile("\\s\\d+\\s");

    private Optional<String> findDecimal(String currentLine) {
        Matcher matcher = decimalPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }
    private Optional<String> findRefund(String currentLine) {
        Matcher matcher = refundPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }
    private Optional<String> findQuantity(String currentLine) {
        Matcher matcher = quantityPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }
     Optional<String> findItemDescription(String currentLine) {
        Matcher matcher = itemDescriptionPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }
    private Optional<String> findWebsiteLink(String currentLine) {
        Matcher matcher = websiteLinkPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }
    private Optional<String> findEndBody(String currentLine) {
        Matcher matcher = endBodyPattern.matcher(currentLine);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        } else {
            return Optional.empty();
        }
    }

    public List<Item> parseFile(List<File> fileList) throws IOException {
        inBody = false;
        List<Item> finalList = new ArrayList<>();
        String previous = null;
        for (File currentFile : fileList){
            List<String> receiptList = Files.readAllLines(Paths.get(String.valueOf(currentFile)));
            for (String current : receiptList) {
                if (inBody) {
                    if (findDecimal(current).isPresent()) {
                        if (findItemDescription(current).isPresent()){
                            if (findRefund(current).isPresent()){
                                finalList.add(new Item(findItemDescription(current).get().trim().replace("\t", " "), findDecimal(current).get(), "-1"));
                            }else{
                                finalList.add(new Item(findItemDescription(current).get().trim().replace("\t", " "), findDecimal(current).get(), "1"));
                            }
                        } else if (findRefund(current).isPresent()) {
                            finalList.add(new Item(findItemDescription(previous).get().trim().replace("\t", " "), "-" + findDecimal(current).get(), findQuantity(current).get().trim()));
                        } else {
                            finalList.add(new Item(findItemDescription(previous).get().trim().replace("\t", " "), findDecimal(current).get(), findQuantity(current).get().trim()));
                        }
                    } else if (findEndBody(current).isPresent()) {
                        inBody = false;
                    }
                } else if (findWebsiteLink(current).isPresent()) {
                    inBody = true;
                }
                previous = current;
            }

        }

        return finalList;
        /*
        if (findDecimal(current).isPresent()) {
                        if (findItemDescription(current).isPresent()){
                            finalList.add(new Item(findItemDescription(current).get().trim(), findDecimal(current).get(), "1"));
                        } else {
                            finalList.add(new Item(findItemDescription(previous).get().trim(), findDecimal(current).get(), findQuantity(current).get().trim()));
                        }
                    } else if (findEndBody(current).isPresent()) {
                        inBody = false;
                    }
         */
    }

}
