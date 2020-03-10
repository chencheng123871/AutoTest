package com.test.executeCases;

import java.util.List;

import com.test.common.ReadExcel;
import com.test.common.WriteExcel;
import com.test.inter.KeyWordOfInter;

public class ExecuteInterCases {

	private ReadExcel read;
	private WriteExcel write;
	private KeyWordOfInter inter;

	/**
	 * 
	 * @param interCasesPath
	 * @param resultPath
	 */
	public ExecuteInterCases(String interCasesPath, String resultPath) {

		read = new ReadExcel(interCasesPath);
		write = new WriteExcel(interCasesPath, resultPath);
		inter = new KeyWordOfInter(write);

	}

	public void runInter() {
		for (int sheetNum = 0; sheetNum < read.getTotalSheets(); sheetNum++) {
			read.useSheetByIndex(sheetNum);
			write.useSheetByIndex(sheetNum);
			for (int rowNum = 1; rowNum < read.rows; rowNum++) {
				inter.setLine(rowNum);
				List<String> list = read.getRow(rowNum);
				// 第一列或者第二列有一个不为空
				if ((list.get(0) != null || list.get(1) != null)
						&& (!list.get(0).equals("null") || !list.get(1).equals("null"))
						&& (list.get(0).length() > 0 || list.get(1).length() > 0)) {
					
				} else {
					switch (list.get(3)) {
					// post使用www-formdata-urlencoded
					case "post":
						inter.postWithUrlencoded(list.get(4), list.get(5));
						break;
					case "get":
						inter.get(list.get(4), list.get(5));
						break;
					// application/json的传参的post
					case "postjson":
						inter.postWithJson(list.get(4), list.get(5));
						break;
					// webservice的接口
					case "xmlpost":
						inter.postWithXml(list.get(4), list.get(5));
					case "addhead":
						inter.addHeader(list.get(4));
						break;
					case "clearhead":
						inter.clearHeader();
						//通过json保存参数
						break;
					case "savejson":
						inter.saveParmJson(list.get(4),list.get(5));
						break;
						//通过正则保存参数
					case "saveRex":
						inter.saveParmRex(list.get(4),list.get(5));
						break;
					case "savecookie":
						inter.saveCookie();
						break;
					case "clearcookie":
						inter.clearCookie();
						break;
					case "notcookie":
						inter.notCookie();
						break;
						default:
							write.writeFailCell(rowNum,11,"关键字不存在请检查");
							break;
					}
					switch(list.get(7)) {
					case "jsonequal":
						inter.assertSameJson(list.get(8), list.get(9));
						break;
					case "rexequal":
						inter.assertSameRex(list.get(8), list.get(9));
						break;
						default:
							break;
						
					}
				}
			}
		}
	}
	public void saveExcel() {
		write.save();
	}
}
