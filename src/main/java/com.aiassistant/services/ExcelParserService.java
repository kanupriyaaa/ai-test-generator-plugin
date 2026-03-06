package com.aiassistant.services;
import com.aiassistant.models.TestCase;

import com.aiassistant.models.TestCase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelParserService {

    public List<TestCase> parseExcel(File file) {

        List<TestCase> testCases = new ArrayList<>();


        try {

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                Cell testIdCell = row.getCell(0);
                Cell actionCell = row.getCell(1);
                Cell inputCell = row.getCell(2);
                Cell expectedCell = row.getCell(3);

                if (testIdCell == null || testIdCell.toString().trim().isEmpty()) {
                    continue; // skip empty rows
                }

                TestCase testCase = new TestCase();

                testCase.setTestId(testIdCell.toString());
                testCase.setAction(actionCell != null ? actionCell.toString() : "");
                testCase.setInput(inputCell != null ? inputCell.toString() : "");
                testCase.setExpected(expectedCell != null ? expectedCell.toString() : "");

                testCases.add(testCase);
            }

            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return testCases;

    }
    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case BLANK:
                return "";

            default:
                return "";
        }
    }

}