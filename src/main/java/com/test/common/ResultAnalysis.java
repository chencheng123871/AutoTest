package com.test.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

public class ResultAnalysis {
	/*
	 * 对于测试用例执行的结果进行统计分析
	 * 
	 */
	//总的执行用例数
	private int tatalNum = 0;
	//总的用例pass数
	private int tatalPassNum = 0;
	private int groupNum = 0;
	private int groupPassNum = 0;
	//测试的通过率
	float passrate = 100f;
	private ReadExcel read;
	

	/**
	 * 构造函数时创建需要读取的执行结果用例的实例
	 * 
	 * @param resultPath
	 */
	public ResultAnalysis(String resultPath) {

		read = new ReadExcel(resultPath);

	}
	/**
	 * 对于用例执行完的文件进行统计分析，返回一个二维的链表
	 * @return
	 */
	public List<List<String>> stats(){
		List<List<String>> statsList = new ArrayList<List<String>>();
		List<String> groupList = new ArrayList<String>();
		String groupName = "";
		for(int sheetNum=0;sheetNum<read.getTotalSheets();sheetNum++) {
			read.useSheetByIndex(sheetNum);
			String sheetName = read.getSheetName(sheetNum);
			for(int rowNum=1;rowNum<read.rows;rowNum++) {
				List<String> line = read.getRow(rowNum);
				//判断第一行或者第二行有一个不为空，不是测试用例行
				if(!((line.get(0) == null || line.get(0).length() == 0 || line.get(0).trim().equals(""))
						&& (line.get(1) == null || line.get(1).length() == 0
						|| line.get(1).trim().equals("")))) {
					//第一列不为空的情况
					if(!(line.get(0)==null||line.get(0).length()<2)) {
						//找第二个分组
						
					
						if(rowNum>2) {
							//sheet页的名称加分组名称
							groupList.add(sheetName+"-"+groupName);
							groupList.add(groupNum+"");
							groupList.add(groupPassNum+"");
							//判断该分组测试结果成功还是失败
							if(groupPassNum<groupNum) {
								groupList.add("失败");
							}else {
								groupList.add("成功");
							}
							//把结果添加到统计的结果链表中
							statsList.add(groupList);
							//还原配置
							groupNum = 0;
							groupPassNum = 0;
							groupList = new ArrayList<String>();
						}
						//写入分组的信息
						groupName = line.get(0);
					}
					//第一列和第二列都为空且第三列不为空
				}else {
					//用例列不为空且执行结果也不为空，用例执行数就+1
					if(line.get(2).length()>0&&line.get(10).length()>0) {
						tatalNum++;
						//用例执行结果为pass分组执行pass数+1，分组执行数+1，用例执行pass数+1
						if(line.get(10).equals("Pass")) {
							tatalPassNum++;
							groupNum++;
							groupPassNum++;
							//执行结果失败，分组执行数也+1
						}else {
							groupNum++;
						}
					}
					//该sheet页执行到最后一行时
					if(rowNum==read.rows-1) {
						groupList.add(sheetName+"-"+groupName);
						groupList.add(groupNum+"");
						groupList.add(groupPassNum+"");
						//判断该分组测试结果成功还是失败
						if(groupPassNum<groupNum) {
							groupList.add("失败");
						}else {
							groupList.add("成功");
						}
						//把结果添加到统计的结果链表中
						statsList.add(groupList);
						//还原配置
						groupNum = 0;
						groupPassNum = 0;
						groupList = new ArrayList<String>();
					}
					
				}
			}
		}
		if(tatalPassNum<tatalNum) {
			groupList.add("失败");
		}else {
			groupList.add("成功");
		}
		groupList.add(tatalNum+"");
		groupList.add(tatalPassNum+"");
		//计算总的通过率
		passrate = ((int) (((float) tatalPassNum /tatalNum ) * 10000)) / 100f;
		groupList.add(passrate+"");
		statsList.add(groupList);
		return statsList;
	}
}
