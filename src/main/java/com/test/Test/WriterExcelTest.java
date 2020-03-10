package com.test.Test;

import com.test.common.WriteExcel;

public class WriterExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String resultPath = "testcases\\result.xlsx";
		String casesPath = "testcases\\HTTPLogin.xlsx";
		WriteExcel write = new WriteExcel(casesPath, resultPath);
		write.writeDefCell(3, 11, "Pass");
		write.writeFailCell(4,11, "Fail");
		write.save();
	}

}
