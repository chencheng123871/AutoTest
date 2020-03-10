package com.test.Test;

import com.test.common.ReadExcel;
import com.test.common.WriteExcel;
import com.test.executeCases.ExecuteInterCases;

public class TestRunInterCases {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String resultPath = "testcases\\result.xlsx";
		String casesPath = "testcases\\HTTPLogin.xlsx";
		ReadExcel read = new ReadExcel(casesPath);
		WriteExcel write = new WriteExcel(casesPath, resultPath);
		ExecuteInterCases cases = new ExecuteInterCases(casesPath, resultPath);
		cases.runInter();
		read.closeExcel();
		cases.saveExcel();
		
	}

}
