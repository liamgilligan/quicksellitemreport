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