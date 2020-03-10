package com.test.inter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.test.common.AutoLog;
import com.test.common.WriteExcel;

public class KeyWordOfInter {

	// 用来调用HttpClientKW的方法
	public HttpClientKW client;
	// 用来存参数实现参数的传递
	public Map<String, String> parmMap;
	// 每次发包之后的返回结果,在下一次调用请求方法之前都不会发生变化，但是会从中进行一些提取。
	public String tmpResponse;
	private WriteExcel write;
	//写到第几行
	private int line=0;

	/**
	 * 构造函数创建HttpClientKW的对象
	 */
	public KeyWordOfInter(WriteExcel excelFile) {
		client = new HttpClientKW();
		parmMap = new HashMap<String, String>();
		write = excelFile;
		
	}
	
	public void setLine(int rowNum) {
		line = rowNum;
	}

	/**
	 * 通过jsonpath提取来存参数
	 * 
	 * @param key
	 * @param jsonPath
	 */
	public void saveParmJson(String key, String jsonPath) {
		String value = "";
		try {
			value = JSONPath.read(tmpResponse, jsonPath).toString();
			parmMap.put(key, value);
			String res = "添加参数" + key + "成功" + "key的值" + value;
			AutoLog.log.info(res);
			write.writeDefCell(line,11,res);
			write.writeDefCell(line,10,"Pass");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}

	}

	/**
	 * 通过正则提取来存储参数
	 * 
	 * @param key
	 * @param rex 格式为 左边界(.*?)右边界取匹配到的中间内容为key的值
	 */
	public void saveParmRex(String key, String rex) {
		String value = "";
		try {
			Pattern Rex = Pattern.compile(rex);
			Matcher resl = Rex.matcher(tmpResponse);
			if (resl.find()) {
				value = resl.group(1);
			}
			String res = "添加参数" + key + "成功" + "key的值" + value;
			AutoLog.log.info(res);
			write.writeDefCell(line,11,res);
			write.writeDefCell(line,10,"Pass");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}
	}

	/**
	 * 通过和parmMap存的参数进行转换，把传进来的字符串parms中标记为{key} 转成对应的value
	 * 
	 * @param parms
	 */
	private String toParm(String parms) {
		String result = parms;
		for (String key : parmMap.keySet()) {
			result = result.replaceAll("\\{" + key + "\\}", parmMap.get(key).toString());
		}
		return result;

	}

	/**
	 * 
	 * @param url
	 * @param parms
	 */
	public void get(String url, String parms) {
		url = toParm(url);
		parms = toParm(parms);
		tmpResponse = client.doGet(url, parms);
		write.writeDefCell(line,11,tmpResponse);
		AutoLog.log.info(tmpResponse);
		
	}

	/**
	 * 
	 * @param url
	 * @param parms
	 */
	public void postWithUrlencoded(String url, String parms) {
		url = toParm(url);
		parms = toParm(parms);
		tmpResponse = client.doPostUrlencoded(url, parms);
		write.writeDefCell(line,11,tmpResponse);
		AutoLog.log.info(tmpResponse);
	}

	/**
	 * 
	 * @param url
	 * @param parms
	 */
	public void postWithJson(String url, String parms) {
		url = toParm(url);
		parms = toParm(parms);
		tmpResponse = client.doPostJson(url, parms);
		write.writeDefCell(line,11,tmpResponse);
		AutoLog.log.info(tmpResponse);
	}

	/**
	 * 正则表达式读取返回xml中的json内容，return这个元素，是wsdl文档中定义，根据返回实际情况编写。
	 * 
	 * @param url
	 * @param parms
	 */
	public void postWithXml(String url, String parms) {
		url = toParm(url);
		parms = toParm(parms);
		tmpResponse = client.doXmlPost(url, parms);
		Pattern returnPattern = Pattern.compile("<return>(.*?)</return>");
		Matcher returnM = returnPattern.matcher(tmpResponse);
		if (returnM.find()) {
			tmpResponse = returnM.group(1);
		}
		write.writeDefCell(line,11,tmpResponse);
		AutoLog.log.info(tmpResponse);

	}

	/**
	 * 
	 * @param url
	 * @param jsonFile
	 * @param textParam
	 */
	public void Upload(String url, String jsonFile, String... textParam) {
		url = toParm(url);
		jsonFile = toParm(jsonFile);
		tmpResponse = client.doPostUpload(url, jsonFile, textParam);
		write.writeDefCell(line,11,tmpResponse);
		AutoLog.log.info(tmpResponse);

	}
	/**
	 * 添加头域操作
	 * @param jsonString
	 */
	public void addHeader(String jsonString) {
		jsonString = toParm(jsonString);
		try {
			Map<String, String> headMap = new HashMap<String, String>();
			if (jsonString.length() > 0 && jsonString.startsWith("{") && jsonString.endsWith("}")) {
				JSONObject json = JSON.parseObject(jsonString);
				for (String key : json.keySet()) {
					headMap.put(key, json.getString(key));
				}
			}
			client.addHeader(headMap);
			write.writeDefCell(line, 10,"Pass");
			write.writeDefCell(line,11,headMap.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("添加头域失败");
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
			
		}
		
	}
	public void clearHeader() {
		try {
			client.clearHead();
			write.writeDefCell(line,11,"清理头域成功");
			write.writeDefCell(line,10,"Pass");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("清理头域失败");
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}
	}
	
	
	public void saveCookie() {
		try {
			client.saveCookie();
			write.writeDefCell(line,10,"Pass");
			write.writeDefCell(line,11,"成功保存cookie");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("cookie保存失败");
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}
	}
	
	public void clearCookie() {
		try {
			AutoLog.log.info("清理cookie");
			client.clearCookie();
			write.writeDefCell(line,10,"Pass");
			write.writeDefCell(line,11,"清理成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("cookie清理失败");
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}
	}
	public void notCookie() {
		try {
			AutoLog.log.info("不使用cookie并清空掉cookie");
			client.useNewCookie();
			write.writeDefCell(line,10,"Pass");
			write.writeDefCell(line,11,"停掉成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("不使用cookie失败");
			AutoLog.log.error(e, e.fillInStackTrace());
			write.writeFailCell(line,10,"Fail");
			write.writeFailCell(line,11,e.fillInStackTrace().toString());
		}
	}
	/**
	 * 通过josnpath来断言是否一样的
	 * @param epx
	 * @param jsonPath
	 */
	public void assertSameJson(String jsonPath,String epx) {
		try {
			String act = JSONPath.read(tmpResponse,jsonPath).toString();
			if(epx.equals(act)&&act!=null) {
				AutoLog.log.info("通过");
				write.writeDefCell(line,10,"Pass");
			}else {
				AutoLog.log.info("失败");
				write.writeFailCell(line,10,"Fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error("解析失败，请检查jsonPath表达式");
			AutoLog.log.error(e,e.fillInStackTrace());
			write.writeFailCell(line, 10, "FAIL");
			write.writeFailCell(line, 11,"解析失败，请检查jsonPath表达式");
		}
	}
	/**
	 * 通过正则提取出来的结果判断是否相同，
	 * @param epx
	 * @param regex 左边界（.*?）右边界
	 */
	public void assertSameRex(String epx,String regex) {
		
		try {
			Pattern pat = Pattern.compile(regex);
			Matcher mat = pat.matcher(tmpResponse);
			String act = "";
			if(mat.find()) {
				act = mat.group(1);
			}
			if(act!=null&&act.length()>0&&act.equals(epx)) {
				AutoLog.log.info("通过");
				write.writeDefCell(line,10,"Pass");
			}else {
				AutoLog.log.info("失败");
				write.writeFailCell(line,10,"Fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			write.writeFailCell(line, 10, "FAIL");
			write.writeFailCell(line, 11,e.fillInStackTrace().toString());
		}
		
		
	}

}
