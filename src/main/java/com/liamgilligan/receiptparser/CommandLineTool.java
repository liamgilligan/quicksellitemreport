package com.liamgilligan.receiptparser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CommandLineTool {
    public static void main(String[] args) throws Exception {
        File file = new File("/home/liamgilligan/Desktop");
        System.out.println(file.getAbsolutePath());
        /*
        String fileName = args[0];
        Parser parser = new Parser();
        List<String> testDataList = Files.readAllLines(Paths.get(fileName));
        List<Item> finalList = parser.parseFile(testDataList);
        finalList.forEach(r -> System.out.printf("%s\t%s\t%s\n", r.itemDescription(), r.price(), r.quantity()));

         */
    }
}
