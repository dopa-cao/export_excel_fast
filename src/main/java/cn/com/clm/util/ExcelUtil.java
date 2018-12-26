package cn.com.clm.util;

import cn.com.clm.error.ErrorEnum;
import cn.com.clm.exception.CommonException;
import cn.com.clm.util.annotation.RowIndex;
import cn.com.clm.util.reflection.RowParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * describe:
 *
 * @author liming
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 数据写入excel
     *
     * @param sheet         sheet
     * @param cls           class
     * @param originData    data
     * @param start         start num
     */
    public static void parse(Sheet sheet, Class<?> cls, List<?> originData, int start) throws CommonException {
        handleHeader(sheet, cls);
        CellStyle cellStyle = buildCellStyle(sheet);
        for (Object object : originData) {
            parseLineByNum(sheet, cellStyle, start++, object);
        }
    }


    /**
     * 处理头信息
     *
     * @param sheet         sheet
     * @param cls           class
     */
    private static void handleHeader(Sheet sheet, Class<?> cls) throws CommonException {
        Map<Integer, Field> fieldMap = RowParseUtil.parser(cls);
        TreeMap<Integer, Field> treeMap = new TreeMap<>(fieldMap);
        Row row = sheet.getRow(0);
        CellStyle headerCellStyle = buildHeaderCellStyle(sheet);
        try {
            if (null == row) {
                row = sheet.createRow(0);
            }
            if (treeMap.size() > 0) {
                for (int i = 0; i < treeMap.size(); i++) {
                    Cell cell = row.getCell(i);
                    if (null == cell) {
                        cell = row.createCell(i);
                    }
                    Field field = treeMap.get(i + 1);
                    RowIndex rowIndex = field.getAnnotation(RowIndex.class);
                    String header = rowIndex.header();
                    cell.setCellType(CellType.STRING);
                    cell.setCellStyle(headerCellStyle);
                    cell.setCellValue(header);
                }
            }
        }catch (Exception e){
            throw new CommonException(ErrorEnum.IMPORT_ERROR);
        }
    }

    /**
     * build style for header
     *
     * @param sheet     sheet
     * @return          cell style
     */
    private static CellStyle buildHeaderCellStyle(Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(Boolean.TRUE);
        font.setColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFont(font);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置填充颜色和方式
        cellStyle.setFillForegroundColor((short) 2);
        cellStyle.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
        return cellStyle;
    }

    /**
     * build style for cell
     *
     * @param sheet     sheet
     * @return          cell style
     */
    private static CellStyle buildCellStyle(Sheet sheet) {
        CellStyle cellStyle = buildHeaderCellStyle(sheet);
        Workbook workbook = sheet.getWorkbook();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        cellStyle.setFont(font);
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        return cellStyle;
    }


    /**
     * 单行数据处理
     * @param sheet     sheet
     * @param cellStyle cell style
     * @param rowNum    row num
     * @param object    data
     */
    private static void parseLineByNum(Sheet sheet, CellStyle cellStyle, int rowNum, Object object) throws CommonException {
        Row row = sheet.getRow(rowNum);
        if (null == row) {
            row = sheet.createRow(rowNum);
        }
        Map<Integer, Field> fieldMap = RowParseUtil.parser(object.getClass());
        TreeMap<Integer, Field> treeMap = new TreeMap<>(fieldMap);
        if (treeMap.size() > 0) {
            try {
                for (int i = 0; i < treeMap.size(); i++) {
                    Cell cell = row.getCell(i);
                    if (null == cell) {
                        cell = row.createCell(i);
                    }
                    Field field = treeMap.get(i + 1);
                    Object value = null == field.get(object) ? "" : field.get(object);
                    RowIndex rowIndex = field.getAnnotation(RowIndex.class);
                    if (rowIndex.type().equals(Date.class)){
                        value = dateFormat.format(value);
                    }
                    cell.setCellType(CellType.STRING);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(value.toString());
                }
            }catch (IllegalAccessException e) {
                throw new CommonException(ErrorEnum.IMPORT_ERROR);
            }
        }
    }

    /**
     * get workbook from excel file
     *
     *
     * @param cls       class
     * @param file      excel file
     * @return          result
     */
    public static Workbook getWorkbookByFile(Class<?> cls, MultipartFile file) throws CommonException {
        Workbook workbook = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            workbook = WorkbookFactory.create(inputStream);
            //可扩展解析多sheet
            Map<Integer, Field> fieldMap = RowParseUtil.parser(cls);
            TreeMap<Integer, Field> treeMap = new TreeMap<>(fieldMap);
            verifySheet(treeMap, workbook.getSheetAt(0));
        } catch (InvalidFormatException e) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR,"Excel格式异常");
        } catch (IOException e) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR,"Excel读取数据失败");
        } finally {
            if (null != workbook){
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage());
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        }
        return workbook;
    }

    /**
     * sheet verify
     * @param treeMap   tree map
     * @param sheet     sheet
     */
    private static void verifySheet(TreeMap<Integer, Field> treeMap, Sheet sheet) throws CommonException {
        if (null == sheet) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "Sheet页没有找到");
        }
        Row row = sheet.getRow(0);
        if (null == row) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "第一行没有找到");
        }
        for (int i = 0; i < treeMap.size(); i++) {
            Field field = treeMap.get(i + 1);
            verifyHeaderRow(field, row.getCell(i));
        }
    }

    /**
     * verify header
     *
     * @param field     field
     * @param cell      cell
     */
    private static void verifyHeaderRow(Field field, Cell cell) throws CommonException {
        RowIndex rowIndex = field.getAnnotation(RowIndex.class);
        if (null == cell) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "找不到对应的单元格：" + rowIndex.header());
        }
        String cellValue = cell.getStringCellValue();
        if (!StringUtils.equals(rowIndex.header(), cellValue)) {
            throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "解析是字段不对应：" + rowIndex.header());
        }
    }

    /**
     *
     *
     * @param sheet     sheet
     * @param cls       cls
     * @param start     start
     * @return          data
     */
    public static List<? extends Object> parse(Sheet sheet, Class<?> cls, Integer start) throws CommonException {
        List<Object> objects = new ArrayList<>();
        Map<Integer, Field> fieldMap = RowParseUtil.parser(cls);
        TreeMap<Integer, Field> treeMap = new TreeMap<>(fieldMap);
        if (treeMap.size() > 0) {
            try {
                for (int i = start; i <= sheet.getLastRowNum() ; i++) {
                    Row row = sheet.getRow(i);
                    Object object = cls.newInstance();
                    if (null == row) {
                        throw new CommonException(ErrorEnum.UNKNOWN_ERROR, "导入数据错误，行号：" + i);
                    }
                    for (int j = 0; j < treeMap.size(); j++) {
                        Field field = treeMap.get(j + 1);
                        Cell cell = row.getCell(j);
                        setValue(field, cell, object);
                    }
                    objects.add(object);
                }
            } catch (InstantiationException | IllegalAccessException | ParseException e) {
                throw new CommonException(ErrorEnum.UNKNOWN_ERROR,"创建数据对象失败");
            }
        }
        return objects;
    }

    /**
     * set value
     * @param field     field
     * @param cell      cell
     * @param object    object
     */
    private static void setValue(Field field, Cell cell, Object object) throws IllegalAccessException, ParseException {
        RowIndex rowIndex = field.getAnnotation(RowIndex.class);
        field.setAccessible(Boolean.TRUE);
        if (null == cell) {
            field.set(object, rowIndex.nullValue());
        } else {
            if (rowIndex.type().equals(String.class)) {
                cell.setCellType(CellType.STRING);
                field.set(object, cell.getStringCellValue());
            } else if (rowIndex.type().equals(Double.class)) {
                cell.setCellType(CellType.NUMERIC);
                field.setDouble(object, cell.getNumericCellValue());
            } else if (rowIndex.type().equals(Float.class)) {
                cell.setCellType(CellType.NUMERIC);
                field.setFloat(object, (float) cell.getNumericCellValue());
            } else if (rowIndex.type().equals(Integer.class)) {
                cell.setCellType(CellType.STRING);
                String temp = cell.getStringCellValue();
                field.setInt(object,  Integer.valueOf(temp));
            } else if (rowIndex.type().equals(Boolean.class)) {
                cell.setCellType(CellType.BOOLEAN);
                field.setBoolean(object, cell.getBooleanCellValue());
            } else if (rowIndex.type().equals(Date.class)) {
                cell.setCellType(CellType.NUMERIC);
                field.set(object, cell.getDateCellValue());
            }
        }
    }

}




























