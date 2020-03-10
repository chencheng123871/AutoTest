package com.test.common;

import java.util.List;

public class Report {
	/*
	 * 生成的测试的html报告的拼接
	 * 
	 */

	public String makeReport(List<List<String>> resultList) {

		String content = "<html><style type=\"text/css\"> body{ background:#ffffff; margin:0 auto; padding:0; text-align:left; font-size:12px; font-family: \"微软雅黑\",\"宋体\";}table{ font-size:12px; font-family: \"微软雅黑\",\"宋体\";}.table_c{border:1px solid #ccc;border-collapse:collapse; }.table_c td{border:1px solid #ccc; border-collapse:collapse;}.table_b{border:1px solid #666;border-collapse:collapse; }.table_b td{ border-collapse:collapse; border:1px solid #ccc;}.table_b th{color:#fff; background:#666;}a:link{ color:#3366cc; font-weight:normal; } a:visited { color: #3366cc;} a:hover{ color:#000; } a:active { color:#3366cc; }td{ line-height:20px;} </style><table width=\"650\" border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\" style=\"border: #ccc 1pxsolid;\"><tbody><tr><td><table width=\"650\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"fbfbfb\" style=\"border-bottom: #eeeeee 1px solid;border-top: #cc0000 1pxsolid;\"><tbody><tr><td width=\"249\" align=\"center\" style=\"font-size: 12px; color:#999999; padding-top: 26px;\">此信为自动化测试邮件，请不要直接回复。</td></tr></tbody></table><table width=\"592\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin: 28px 28px 10px 28px;\"><tbody><tr><td style=\"font-size: 12px; color:#666666; padding-bottom: 6px;\">尊敬的领导,您好！</td></tr></tbody></table><table width=\"592\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td style=\"font-size: 14px; color: 333333; font-weight:bold; padding-bottom:15px;\">本测试邮件来自：<font color=\"ff6600\">陈铖</font></td></tr><tr><td><table width=\"592\" border=\"0\" align=\"center\" class=\"table_c\" style=\"\"\"border-collapse:collapse;\"><tbody><tr><td width=\"100\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">测试状态</td><td width=\"80\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">执行测试用例总数</td><td width=\"100\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">执行测试用例通过数</td><td width=\"80\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">通过率</td></tr><tr><td height=\"28\" bgcolor=\"#FFFFFF\" align=\"center\" style=\"\" \"border:1px solid#ccc;\">成功</td><td height=\"28\" bgcolor=\"#FFFFFF\" align=\"center\" style=\"\" \"border:1px solid#ccc;\">全部执行用例</td><td height=\"28\" bgcolor=\"#FFFFFF\" align=\"center\" style=\"\" \"border:1px solid#ccc;\">通过的用例</td><td height=\"28\" bgcolor=\"#FFFFFF\" align=\"center\" style=\"\" \"border:1px solid#ccc;\">用例通过率</td></tr></tbody></table></td></tr><tr><td style=\"\" \"font-size: 14px; color: 333333; font-weight:bold; padding-bottom:15px;\"><br>测试详情报表</td></tr><tr><td><table width=\"592\" border=\"0\" align=\"center\" class=\"table_c\" style=\"\"\"border-collapse:collapse;\"><tbody><tr><td width=\"100\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">分组信息</td><td width=\"80\" height=\"28\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">用例总数</td><td width=\"80\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">通过数</td><td width=\"80\" align=\"center\" bgcolor=\"#FFFFFF\" style=\"\" \"border:1px solid#ccc;\">状态</td></tr>";
		List<String> themeList = resultList.get(resultList.size() - 1);
		if (themeList.get(3).equals("100.0")) {
			content = content.replace("成功", "<span style=\"color:blue\">测试成功</span>");
		} else {
			content = content.replace("成功", "<span style=\"color:red\">测试失败</span>");
		}
		content = content.replaceAll("全部执行用例", themeList.get(1));
		content = content.replaceAll("通过的用例", themeList.get(2));
		content = content.replaceAll("用例通过率", themeList.get(3));
		for (int i = 0; i < resultList.size() - 1; i++) {
			content += "<tr>";
			content += "<td>" + resultList.get(i).get(0) + "</td>";
			content += "<td>" + resultList.get(i).get(1) + "</td>";
			content += "<td>" + resultList.get(i).get(2) + "</td>";
			content += "<td>" + resultList.get(i).get(3) + "</td>";
			content += "</tr>";
		}
		content += "</tbody></table></td></tr><tr></tbody></table></body></html>";
		return content;
	}

}
