package com.liamgilligan.receiptparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    /*
    record regexResult(String regex, String result){}
    @ParameterizedTest
    @MethodSource("recordProvider")
    @Test
    void testItemDescriptionRegex(regexResult r) {

        var parser = new Parser();
        var iD = parser.findItemDescription(r.regex);
        assertEquals(true, iD.isPresent());
        assertEquals(r.result, iD.get().trim());
    }
    static Stream<regexResult> recordProvider(){
        return Stream.of(
                new regexResult("MS Snapback black/white          25.95                                          ", "MS Snapback black/white")
        );
      }
     */

}