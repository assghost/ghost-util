package com.ghostcat.poi.writer;

import com.ghostcat.common.util.GhostDateTimeUtils;
import com.ghostcat.common.util.GhostDateUtils;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ExcelWriter {

    public static void setCellValue(Cell cell, Object value) {
        if (cell != null && value != null) {

            if (value instanceof Double) {
                cell.setCellValue((Double)value);
            }
            else if (value instanceof Integer) {
                cell.setCellValue((Integer)value);
            }
            else if (value instanceof BigDecimal) {
                BigDecimal bd = (BigDecimal) value;

                int oldScale = bd.scale();

                if (0 <= oldScale && 2 > oldScale) {
                    bd.setScale(2);
                }

                cell.setCellValue(bd.toString());
            }
            else if (value instanceof Date) {
                Date date = (Date) value;
                cell.setCellValue(GhostDateTimeUtils.formatDate(date));
            }
            else if (value instanceof LocalDate) {
                LocalDate date = (LocalDate) value;
                cell.setCellValue(GhostDateUtils.date2Str(date));
            }
            else if (value instanceof LocalDateTime) {
                LocalDateTime date = (LocalDateTime) value;
                cell.setCellValue(GhostDateTimeUtils.date2Str(date));
            }
            else if (value instanceof String) {
                String str = (String) value;
                if (str.startsWith("=")) {
                    cell.setCellFormula(str);
                } else {
                    cell.setCellValue(str);
                }
            } else {
                Sheet sheet = cell.getSheet();
                Workbook workbook = sheet.getWorkbook();
                CreationHelper creationHelper = workbook.getCreationHelper();
                RichTextString richTextString = creationHelper.createRichTextString(value.toString());
                cell.setCellValue(richTextString);
            }
        } else {
            cell.setBlank();
        }
    }

    public static void addComment(Cell cell, String text, String author) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();
        //poi工具类
        CreationHelper creationHelper = workbook.getCreationHelper();
        //绘图对象
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        //注解对象，在单元格的左上或右下角
        ClientAnchor anchor = creationHelper.createClientAnchor();

        anchor.setCol1(cell.getColumnIndex());
        anchor.setRow1(cell.getRowIndex());
        anchor.setCol2(5);
        anchor.setRow2(6);

        Comment cellComment = drawing.createCellComment(anchor);
        RichTextString richTextString = creationHelper.createRichTextString(text);
        cellComment.setString(richTextString);
        cellComment.setAuthor(author);
        cell.setCellComment(cellComment);
    }

    public static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    public static CellStyle createContentStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    public static CellStyle createContentStyle(Cell cell) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();

        return createContentStyle(workbook);
    }

    public static CellStyle createMoneyStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        DataFormat dataFormat = workbook.createDataFormat();
        short format = dataFormat.getFormat("_ * #,##0.00_ ;_ * -#,##0.00_ ;_ * \"-\"??_ ;_ @_ ");
        cellStyle.setDataFormat(format);

        return cellStyle;
    }

    public static CellStyle createMoneyStyle(Cell cell) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();

        return createMoneyStyle(workbook);
    }

    public static CellStyle createNumericStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        DataFormat dataFormat = workbook.createDataFormat();
        short format = dataFormat.getFormat("0.00");
        cellStyle.setDataFormat(format);

        return cellStyle;
    }

    public static CellStyle createNumericStyle(Cell cell) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();

        return createNumericStyle(workbook);
    }
}
