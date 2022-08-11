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

import java.util.List;

public record ParserReturn(List<Item> itemList, List<ErrorMessage> errorList) {

    record Item(String itemDescription, String price, String quantity) {}
    record ErrorMessage(String previousLine, String currentLine, Throwable error) {}
}
