package com.test.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {

	// 用于存放结果表内容的xlsx格式的工作簿
	private XSSFWorkbook Workbook = null;
	// 工作的sheet页
	private Sheet sheet;
	// 用于读取用例表内容复制到结果标的文件输出流
	private FileOutputStream stream = null;
	// 用于存储结果表的路径的成员变量，便于在保存结果时进行判断
	private String path = null;
	// 单元格格式
	private CellStyle style = null;
	// 表的总行数
	public int rows = 0;

	public WriteExcel(String casesPath, String resultPath) {
		XSSFWorkbook WorkbookRead = null;
		try {
			WorkbookRead = new XSSFWorkbook(new File(casesPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e, e.fillInStackTrace());
		}
		if (WorkbookRead == null) {
			AutoLog.log.info("打开excel文件失败");
			return;
		}
		File file = new File(resultPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("文件路径非法，请检查！");
			AutoLog.log.error(e, e.fillInStackTrace());
		}
		try {
			stream = new FileOutputStream(file);
			WorkbookRead.write(stream);
			stream.close();
			FileInputStream in = new FileInputStream(file);
			Workbook = new XSSFWorkbook(in);
			sheet = Workbook.getSheetAt(0);
			rows = sheet.getPhysicalNumberOfRows();
			in.close();
			path = resultPath;
			setStyle(0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置样式为Excel中指定单元格的样式
	 * 
	 * @param rowNo
	 * @param column
	 */
	public void setStyle(int rowNo, int column) {
		Row row = null;
		Cell cell = null;
		try {
			// 获取指定行
			row = sheet.getRow(rowNo);
			// 获取指定列
			cell = row.getCell(column);
			// System.out.println(cell.getStringCellValue());
			// 保存指定单元格样式
			style = cell.getCellStyle();
		} catch (Exception e) {
			e.printStackTrace();
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
			AutoLog.log.error("该" + name + "的sheet不存在");
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
	 * 向指定单元个写入默认格式的值
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param value
	 */
	public void writeDefCell(int rowNum, int colNum, String value) {
		// 获取要写入的行
		Row row = null;
		try {
			row = sheet.getRow(rowNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 如果该行是不存在的,使用该行数创建一个新的行
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		// 在该行指定到该列
		Cell cell = row.createCell(colNum);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	/**
	 * 用例执行错误，使用红色写入
	 * 
	 * @param rowNum
	 * @param colNum
	 * @param value
	 */
	public void writeFailCell(int rowNum, int colNum, String value) {
		Row row = null;

		// 获取要写入的行
		try {
			row = sheet.getRow(rowNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 如果该行是不存在的,使用该行数创建一个新的行
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		Cell cell = row.createCell(colNum);
		cell.setCellValue(value);
		CellStyle failStyle = null;
		Font font = null;
		font = Workbook.createFont();
		failStyle = Workbook.createCellStyle();
		font.setColor(IndexedColors.RED.index);
		failStyle.setFont(font);
		cell.setCellStyle(failStyle);
	}

	/**
	 * 在指定行写入一行的内容
	 * 
	 * @param rowNum
	 * @param rowList
	 */
	public void writeRowValue(int rowNum, List<String> rowList) {
		Row row = null;
		try {
			row = sheet.getRow(rowNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (row == null) {
			row = sheet.createRow(rowNum);
		}

		for (int i = 0; i < rowList.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(rowList.get(i));
			cell.setCellStyle(style);
		}

	}

	// 将结果表在内存中的工作簿内容保存到磁盘文件中
	public void save() {
		// 如果结果表文件未创建，则不保存
		if (path != null) {
			try {
				// 基于结果表路径创建文件输出流
				stream = new FileOutputStream(new File(path));
				// 将结果表的workbook工作簿的内容写入输出流中，即写入文件
				if (Workbook != null) {
					Workbook.write(stream);
					Workbook.close();
				}
				// 关闭输出流，完成将内存中workbook写入文件的过程，保存文件。
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
