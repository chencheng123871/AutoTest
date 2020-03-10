package com.test.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	// xlsx格式的工作簿
	private XSSFWorkbook Workbook;
	// 工作的sheet页
	private Sheet sheet;
	// 当前sheet页的最大行数
	public int rows = 0;

	/**
	 * 通过excel的路径获取excel
	 * 
	 * @param excelPath
	 */
	public ReadExcel(String excelPath) {

		// 通过文件流打开excel文件
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(excelPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			return;
		}
		// 通过文件流在文件创建工作簿
		try {
			Workbook = new XSSFWorkbook(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
		}
		// 获取第一个sheet页
		sheet = Workbook.getSheetAt(0);
		// 获取该sheet页最大行数实际使用的
		rows = sheet.getPhysicalNumberOfRows();
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sheet == null) {
			AutoLog.log.info("excel读取失败");
		}
	}

	/**
	 * 获取sheet页的个数
	 * 
	 * @return
	 */
	public int getTotalSheets() {
		int sheetNum = 0;
		sheetNum = Workbook.getNumberOfSheets();
		return sheetNum;
	}

	/**
	 * 通过index使用sheet并更新该页最大行数
	 * 
	 * @param index
	 */
	public void useSheetByIndex(int index) {
		try {
			sheet = Workbook.getSheetAt(index);
			rows = sheet.getPhysicalNumberOfRows();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("该sheet不存在");
		}

	}

	/**
	 * 通过sheet名字使用sheet,并更新该页的最大行数
	 * 
	 * @param name
	 */
	public void useSheetByName(String name) {
		try {
			sheet = Workbook.getSheet(name);
			rows = sheet.getPhysicalNumberOfRows();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("该"+name+"的sheet不存在");
		}
	}

	/**
	 * 根据sheet的index获取sheet的名字
	 * 
	 * @param index
	 * @return
	 */
	public String getSheetName(int index) {
		String name = "";
		name = Workbook.getSheetName(index);
		return name;
	}

	/**
	 * 获取指定的行内容，返回该行的链表
	 * 
	 * @param lineNum
	 * @return
	 */
	public List<String> getRow(int rowNum) {
		List<String> rowList = new ArrayList<String>();
		// 根据行号获取行
		Row row = sheet.getRow(rowNum);
		// 获取该行有多少单元格个数
		int num = row.getPhysicalNumberOfCells();
		for (int i = 0; i < num; i++) {
			rowList.add(getCellValue(row.getCell(i)));
		}
		return rowList;
	}

	/**
	 * 获取指定列的内容，返回该列的链表
	 * 
	 * @param colNum
	 * @return
	 */
	public List<String> getColumn(int colNum) {
		List<String> colList = new ArrayList<String>();
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			colList.add(getCellValue(row.getCell(colNum)));
		}
		return colList;
	}
	/**
	 * 读取指定单元格的值
	 * @param rowNum
	 * @param cloNum
	 * @return
	 */
	public String getCell(int rowNum,int cloNum) {
		String value = "";
		Row row = sheet.getRow(rowNum);
		value = getCellValue(row.getCell(cloNum));
		return value;
	}
	
	
	/**
	 * 读取cell的值
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return "";
		}
		try {
			CellType cellType = cell.getCellType();
			switch (cellType) {
			case STRING: // 文本
				cellValue = cell.getStringCellValue();
				break;
			case NUMERIC: // 数字、日期
				if (DateUtil.isCellDateFormatted(cell)) {
					// 日期型以年-月-日格式存储
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
					cellValue = fmt.format(cell.getDateCellValue());
				} else {
					// 数字保留两位小数
					Double d = cell.getNumericCellValue();
					DecimalFormat df = new DecimalFormat("#.##");
					cellValue = df.format(d);
				}
				break;
			case BOOLEAN: // 布尔型
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case BLANK: // 空白
				cellValue = cell.getStringCellValue();
				break;
			case ERROR: // 错误
				cellValue = "错误";
				break;
			case _NONE:
				cellValue = "";
			default:
				cellValue = "错误";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cellValue;
	}
	
	public void closeExcel() {
		try {
			Workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
		}
	}

}
