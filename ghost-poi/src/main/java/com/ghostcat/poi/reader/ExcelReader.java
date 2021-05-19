package com.ghostcat.poi.reader;

import com.ghostcat.common.util.GhostDateTimeUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel读取相关
 * @author AssGhost
 */
public class ExcelReader {

    public static List<List<String>> readSheet(Sheet sheet0) {

        List<List<String>> rowList = new ArrayList<>();

        if (null != sheet0) {

            int lastRowNum = sheet0.getLastRowNum();

            for (int r = 0; r < lastRowNum; r++) {
                Row row = sheet0.getRow(r);

                List<String> colList = new ArrayList<>();
                rowList.add(colList);

                if (null == row) {
                    continue;
                }

                short lastCellNum = row.getLastCellNum();
                for (short c = 0; c < lastCellNum; c++) {
                    XSSFCell cell = (XSSFCell)row.getCell(c);

                    if (null == cell) {
                        colList.add(null);
                        continue;
                    }

                    CellType cellType = cell.getCellType();

                    if (CellType.BLANK.equals(cellType)) {
                        colList.add(null);
                    }

                    if (CellType.STRING.equals(cellType)) {
                        String cellValue = cell.getStringCellValue();
                        colList.add(cellValue);
                    }

                    if (CellType.NUMERIC.equals(cellType)) {

                        double cellValue = cell.getNumericCellValue();

                        if (DateUtil.isCellDateFormatted(cell)) {
                            LocalDateTime dateValue = DateUtil.getLocalDateTime(cellValue);
                            String dateStr = GhostDateTimeUtils.date2Str(dateValue);
                            colList.add(dateStr);
                        } else {
                            String numberValue = NumberToTextConverter.toText(cellValue);
                            colList.add(numberValue);
                        }
                    }

                    if (CellType.BOOLEAN.equals(cellType)) {
                        boolean cellValue = cell.getBooleanCellValue();
                        colList.add(String.valueOf(cellValue));
                    }

                    if (CellType.FORMULA.equals(cellType)) {
                        String cellValue = cell.getCellFormula();
                        colList.add(cellValue);
                    }
                }
            }
        }

        return rowList;
    }

    public static List<List<String>> readExcel(Workbook workbook) {
        Sheet sheet0 = workbook.getSheetAt(0);
        return readSheet(sheet0);
    }

    public static List<List<String>> readXlsx(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        return readExcel(workbook);
    }

    public static List<List<String>> readXlsx(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        return readExcel(workbook);
    }

    public static Cell getMergeCell(Sheet sheet, Cell cell) {
        if (null != sheet && null != cell) {
            int rowIndex = cell.getRowIndex();
            int columnIndex = cell.getColumnIndex();

            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();

            for (CellRangeAddress mergedRegion : mergedRegions) {
                int firstRow = mergedRegion.getFirstRow();
                int lastRow = mergedRegion.getLastRow();
                int firstColumn = mergedRegion.getFirstColumn();
                int lastColumn = mergedRegion.getLastColumn();

                if (rowIndex >= firstRow && rowIndex <= lastRow) {
                    if (columnIndex >= firstColumn && columnIndex <= lastColumn) {
                        Row mergeRow = sheet.getRow(firstRow);
                        Cell mergeCell = mergeRow.getCell(firstColumn);
                        return mergeCell;
                    }
                }
            }
        }

        return null;
    }

    public static boolean isXlsxFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        return "xlsx".equals(fileType);
    }

    public static void main(String[] args) {

        try {
            File file = new File(ExcelReader.class.getClassLoader().getResource("excel-tmp/simple-tmp.xlsx").getPath());
            List<List<String>> lists = readXlsx(file);
            System.out.println(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
