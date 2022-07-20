package com.liamgilligan.receiptparser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineTool {
    public static void main(String[] args) throws Exception {
        String test = "Two";
        final Pattern itemDescriptionPattern  = Pattern.compile(".*");
        Matcher matcher = itemDescriptionPattern.matcher(test);
        matcher.find();
        System.out.println(matcher.group());
    }
}
