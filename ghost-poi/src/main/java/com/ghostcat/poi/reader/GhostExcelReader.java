package com.ghostcat.poi.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ghostcat.common.util.GhostBeanUtils;
import com.ghostcat.poi.annotations.ExportCol;
import com.ghostcat.poi.bean.ExportBean;
import com.ghostcat.poi.util.ExportColParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel模板导入工具
 * @author AssGhost
 */
public class GhostExcelReader {

    private static int DEFAULT_HEADER_NUM = 0;

    public static List<Map<String, String>> readSheet(Sheet sheet0, int headerNum) {
        List<List<String>> rowList = ExcelReader.readSheet(sheet0);
        return convertExcelToMap(rowList, headerNum);
    }

    public static List<Map<String, String>> readExcel(Workbook workbook, int headerNum) {
        List<List<String>> rowList = ExcelReader.readExcel(workbook);
        return convertExcelToMap(rowList, headerNum);
    }

    public static List<Map<String, String>> readXlsx(InputStream is, int headerNum) throws IOException {
        List<List<String>> rowList = ExcelReader.readXlsx(is);
        return convertExcelToMap(rowList, headerNum);
    }

    public static List<Map<String, String>> readXlsx(File file, int headerNum) throws IOException, InvalidFormatException {
        List<List<String>> rowList = ExcelReader.readXlsx(file);
        return convertExcelToMap(rowList, headerNum);
    }

    /**
     * 将二维数组读取结果转换成ListMap
     * 需要制定从哪行开始是表头，每列的表头名会作为Map的key
     * @param rowList
     * @param headerNum
     * @return
     */
    private static List<Map<String, String>> convertExcelToMap(List<List<String>> rowList, int headerNum) {

        Map<String, Integer> headerIndexMap = null;
        List<Map<String, String>> resultList = new ArrayList<>();

        for (int r = 0; r < rowList.size(); r++) {
            if (r < headerNum) {
                //表头之前的行不处理
                continue;
            }
            List<String> colList = rowList.get(r);
            if (r == headerNum) {
                //解析表头行
                headerIndexMap = parseHeaderIndex(colList);
                continue;
            }

            if (null != headerIndexMap) {
                //行List转Map
                Map<String, String> rowMap = convertColList(colList, headerIndexMap);
                resultList.add(rowMap);
            }
        }

        return resultList;
    }

    /**
     * 按表头对应对的小标读取每行对应列
     * @param colList 行数据
     * @param headerIndexMap 表头对应的下标
     * @return
     */
    private static Map<String, String> convertColList(List<String> colList, Map<String, Integer> headerIndexMap) {
        Map<String, String> rowMap = new LinkedHashMap<>();

        for (String headerName : headerIndexMap.keySet()) {
            Integer headerIndex = headerIndexMap.get(headerName);

            if (headerIndex < colList.size()) {
                String value = colList.get(headerIndex);
                rowMap.put(headerName, value);
            } else {
                rowMap.put(headerName, null);
            }
        }

        return rowMap;
    }

    /**
     * 解析表头行，返回每个表头对应列的下标
     * @param headerList
     * @return
     */
    private static Map<String, Integer> parseHeaderIndex(List<String> headerList) {
        if (null != headerList) {
            Map<String, Integer> headerIndexMap = new LinkedHashMap<>(headerList.size());
            for (int c = 0; c < headerList.size(); c++) {
                String headerName = headerList.get(c);
                if (!StringUtils.isEmpty(headerName)) {
                    headerIndexMap.put(headerName, c);
                }
            }

            return headerIndexMap;
        }
        return null;
    }

    /**
     * 先解析表头行获得对应的下标，
     * 再根据{@link ExportCol}将表头转化成对应实体类的字段名
     * @param headerList
     * @param clazz
     * @return
     */
    private static Map<String, Integer> parseHeaderIndex(List<String> headerList, Class clazz) {
        Map<String, Integer> headerIndexMap = parseHeaderIndex(headerList);

        //表头名与字段名的对应关系
        Map<String, String> colFieldMap = ExportColParser.parseAntExportCol(clazz);

        Map<String, Integer> fieldIndexMap = new LinkedHashMap<>(headerIndexMap.size());

        for (String headerName : headerIndexMap.keySet()) {
            Integer headerIndex = headerIndexMap.get(headerName);

            String fieldName = colFieldMap.get(headerName);
            if (!StringUtils.isEmpty(fieldName)) {
                fieldIndexMap.put(fieldName, headerIndex);
            }
        }

        return fieldIndexMap;
    }

    /**
     * 从Excel读取的二维数组转Bean
     * @param rowList
     * @param headerNum
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    private static <T> List<T> convertExcelToBean(List<List<String>> rowList, int headerNum, Class<T> clazz)
            throws JsonProcessingException {

        Map<String, Integer> headerIndexMap = null;
        List<T> resultList = new ArrayList<>();

        for (int r = 0; r < rowList.size(); r++) {
            //表头前的行不处理
            if (r < headerNum) {
                continue;
            }
            List<String> colList = rowList.get(r);
            if (r == headerNum) {

                //字段名与表头下标的映射关系
                headerIndexMap = parseHeaderIndex(colList, clazz);
                continue;
            }

            if (null != headerIndexMap) {
                //行数据转Map，key值为目标类的字段名
                Map<String, String> rowMap = convertColList(colList, headerIndexMap);

                T bean = GhostBeanUtils.map2Bean(rowMap, clazz);

                resultList.add(bean);
            }
        }

        return resultList;
    }

    public static <T> List<T> readSheet(Sheet sheet0, int headerNum, Class<T> clazz)
            throws JsonProcessingException {

        List<List<String>> rowList = ExcelReader.readSheet(sheet0);
        return convertExcelToBean(rowList, headerNum, clazz);
    }

    public static <T> List<T> readExcel(Workbook workbook, int headerNum, Class<T> clazz)
            throws JsonProcessingException {

        List<List<String>> rowList = ExcelReader.readExcel(workbook);
        return convertExcelToBean(rowList, headerNum, clazz);
    }

    public static <T> List<T> readXlsx(InputStream is, int headerNum, Class<T> clazz)
            throws IOException {

        List<List<String>> rowList = ExcelReader.readXlsx(is);
        return convertExcelToBean(rowList, headerNum, clazz);
    }

    public static <T> List<T> readXlsx(File file, int headerNum, Class<T> clazz)
            throws IOException, InvalidFormatException {

        List<List<String>> rowList = ExcelReader.readXlsx(file);
        return convertExcelToBean(rowList, headerNum, clazz);
    }

    /**
     * {@link Workbook} 转 byte
     * @param workbook
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(Workbook workbook) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public static void main(String[] args) {

        try {
            File file = new File(ExcelReader.class.getClassLoader().getResource("excel-tmp/simple-tmp.xlsx").getPath());
            List<Map<String, String>> maps = readXlsx(file, 1);
            System.out.println(maps);

            List<ExportBean> simpleBeanList = readXlsx(file, 1, ExportBean.class);
            System.out.println(simpleBeanList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
