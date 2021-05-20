package com.ghostcat.poi.writer;

import com.ghostcat.common.util.GhostBeanUtils;
import com.ghostcat.poi.annotations.ExportCol;
import com.ghostcat.poi.util.ExportColParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel模板导出工具
 * @author AssGhost
 */
public class GhostExcelWriter {

    private static int BUF_SIZE = 1024;

    private static int DEFAUlT_HEADER_ROW_NUM = 0;

    /**
     * 读项目里的Excel模板
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Workbook readTmplateExcel(String fileName) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource(fileName);

        try (InputStream is = classPathResource.getInputStream()) {

            byte[] bytes = readFileToByte(is);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            XSSFWorkbook workbook = new XSSFWorkbook(bis);

            return workbook;
        }
    }

    /**
     * 输入流转byte
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readFileToByte(InputStream inputStream) throws IOException {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buf = new byte[BUF_SIZE];

            int len = 0;
            while ((len = inputStream.read(buf, 0, BUF_SIZE)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();

            return bos.toByteArray();

        }
    }

    public static byte[] readFileToByte(File file) throws IOException {
        try(FileInputStream fis = new FileInputStream(file)) {
            return readFileToByte(fis);
        }
    }

    /**
     * 按表头对应位置映射实体类字段名
     * @param sheet
     * @param headerRowNum
     * @param clazz
     * @return
     */
    public static Map<String, Integer> getHeaderKeyList(Sheet sheet, int headerRowNum, Class clazz) {

        //表头名与字段名的对应关系
        Map<String, String> colFieldMap = ExportColParser.parseAntExportCol(clazz);

        int lastRowNum = sheet.getLastRowNum();

        if (lastRowNum < headerRowNum) {
            headerRowNum = lastRowNum;
        }

        //表头行
        Row headerRow = sheet.getRow(headerRowNum);

        short lastCellNum = headerRow.getLastCellNum();

        Map<String, Integer> headerIndexMap = new LinkedHashMap<>(lastCellNum);

        for (int c = 0; c < lastCellNum; c++) {
            Cell headerCell = headerRow.getCell(c);

            if (null != headerCell) {
                //表头名称
                String headerName = headerCell.getStringCellValue();

                if (!StringUtils.isEmpty(headerName)) {
                    String headerKey = colFieldMap.get(headerName);
                    if (!StringUtils.isEmpty(headerKey)) {
                        headerIndexMap.put(headerKey, c);
                    }
                }
            }
        }

        return headerIndexMap;
    }

    public static <T> void appendValues(Sheet sheet, List<T> dataList, Class<T> clazz)
            throws InvocationTargetException, IllegalAccessException {

        appendValues(sheet, DEFAUlT_HEADER_ROW_NUM, dataList, clazz);
    }

    /**
     * 按目标Class字段上的{@link ExportCol}设置对应列的值
     * @param sheet
     * @param headerRowNum
     * @param dataList
     * @param clazz
     * @param <T>
     */
    public static <T> void appendValues(Sheet sheet, int headerRowNum, List<T> dataList, Class<T> clazz)
            throws InvocationTargetException, IllegalAccessException {

        if (!CollectionUtils.isEmpty(dataList)) {

            //解析表头，获得Class的字段在模板中的位置
            Map<String, Integer> headerIndexMap = getHeaderKeyList(sheet, headerRowNum, clazz);

            int newRowNum = sheet.getLastRowNum() + 1;

            for (T item : dataList) {

                Map<String, Object> itemMap = GhostBeanUtils.bean2Map(item, clazz);

                Row newRow = sheet.createRow(newRowNum);

                for (String fieldName : itemMap.keySet()) {
                    //获取字段对应的下标，模板中的列号
                    Integer j = headerIndexMap.get(fieldName);
                    if (null != j && j >= 0) {
                        Cell newCell = newRow.createCell(j);

                        Object fieldValue = itemMap.get(fieldName);
                        //根据不同的字段类型设置不同的样式
                        if (fieldValue instanceof BigDecimal) {
                            CellStyle moneyStyle = ExcelWriter.createMoneyStyle(newCell);
                            newCell.setCellStyle(moneyStyle);
                        }

                        if (fieldValue instanceof Integer || fieldValue instanceof Double) {
                            CellStyle numericStyle = ExcelWriter.createNumericStyle(newCell);
                            newCell.setCellStyle(numericStyle);
                        }

                        if (fieldValue instanceof String) {
                            CellStyle contentStyle = ExcelWriter.createContentStyle(newCell);
                            newCell.setCellStyle(contentStyle);
                        }

                        ExcelWriter.setCellValue(newCell, fieldValue);
                    }
                }

                newRowNum ++;
            }
        }
    }
}
