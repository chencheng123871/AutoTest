package com.test.Test;



import com.test.common.ReadExcel;
import com.test.common.Report;
import com.test.common.ResultAnalysis;
import com.test.common.SendMail;
import com.test.common.WriteExcel;
import com.test.executeCases.ExecuteInterCases;

public class AutoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String resultPath = "testcases\\result.xlsx";
		String casesPath = "testcases\\HTTPLogin.xlsx";
		ReadExcel read = new ReadExcel(casesPath);
		ExecuteInterCases cases = new ExecuteInterCases(casesPath, resultPath);
		cases.runInter();
		read.closeExcel();
		cases.saveExcel();
		ResultAnalysis stats = new ResultAnalysis(resultPath);
		Report report = new Report();
		String content = report.makeReport(stats.stats());
		SendMail send = new SendMail();
		send.initMail();
		try {
			send.send(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
