package com.test.inter;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.common.AutoLog;

public class HttpClientKW {

	//
	private CookieStore cookieStore = new BasicCookieStore();
	//cookie的标志，通过判断来是否使用cookie 默认是true
	private Boolean useCookieFlag = true;
	
	//使用headerMap管理额外添加的header
	private Map<String,String> headersMap = new HashMap<String, String>();
	//使用useheader标志位来决定是否添加
	private boolean useHeaderFlag = false;
	
	/**
	 * 使用cookieStore
	 */
	
	private static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

	/**
	 * 查找字符串中的unicode编码并转换为中文。
	 * 
	 * @param u
	 * @return
	 */
	public static String DeCode(String u) {
		try {
			Matcher m = reUnicode.matcher(u);
			StringBuffer sb = new StringBuffer(u.length());
			while (m.find()) {
				m.appendReplacement(sb, Character.toString((char) Integer.parseInt(m.group(1), 16)));
			}
			m.appendTail(sb);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return u;
		}
	}
	
	/**
	 * 
	 */
	public void saveCookie() {
		useCookieFlag = true;
		
	}
	/**
	 * 继续使用该cookie并清理掉cookie中的内容
	 */
	public void clearCookie() {
		useCookieFlag = true;
		cookieStore.clear();
	}
	
	public void useNewCookie() {
		useCookieFlag = false;
		cookieStore = new BasicCookieStore();
	}
	
	/**
	 * 添加头域并使用
	 * @param headMap
	 */
	public void addHeader(Map<String,String>headMap) {
		headersMap = headMap;
		useHeaderFlag = true;
	}
	/**
	 * 清理头域不使用
	 */
	public void clearHead(){
		useHeaderFlag = false;
		headersMap = new HashMap<String, String>();
		
		
	}
	/**
	 * 通过useCookie来决定创建的client是否使用cookie
	 * @return
	 */
	private CloseableHttpClient creatClient() {
		CloseableHttpClient client = null;
		if(useCookieFlag) {
			client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		}
		else {
			client = HttpClients.createDefault();
		}
		return client;
		
	}
	/**
	 * get的方法返回的body的内容
	 * @param url
	 * @param parms
	 * @return
	 */
	public String doGet(String url,String parms){
		String result = "";
		CloseableHttpClient client = creatClient();
		String urlAndParms = "";
		try {
			if(parms.length()>0) {
				urlAndParms = url +"?" + parms;
			}else {
				urlAndParms = url;
			}
			HttpGet get = new HttpGet(urlAndParms);
			//判断是否添加其他头域信息
			if(useHeaderFlag) {
				Set<String> headerKey = headersMap.keySet();
				for(String key :headerKey) {
					get.setHeader(key,headersMap.get(key));
				}
			}
			CloseableHttpResponse response = client.execute(get);
			HttpEntity resEntity = response.getEntity();
			if(resEntity!=null) {
				result = EntityUtils.toString(resEntity,"UTF-8");
			}
			result = DeCode(result);
			EntityUtils.consume(resEntity);
			response.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AutoLog.log.error(e, e.fillInStackTrace());
			}
		}
		return result;
	}
	/**
	 * 以x-www-form-urlencoded格式传参的post方法
	 * @param url
	 * @param parms
	 * @return
	 */
	public String doPostUrlencoded(String url,String parms){
		String result = "";
		CloseableHttpClient client = creatClient();
		try {
			HttpPost post = new HttpPost(url);
			//创建参数的实体
			StringEntity entity = new StringEntity(parms);
			entity.setContentType("application/x-www-form-urlencoded");
			entity.setContentEncoding("UTF-8");
			post.setEntity(entity);
			//判断是否添加其他头域
			if(useHeaderFlag) {
				for(String key : headersMap.keySet()) {
					post.setHeader(key,headersMap.get(key).toString());
				}
			}
			//发送请求
			CloseableHttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity!=null) {
				result = EntityUtils.toString(httpEntity);
			}
			result = DeCode(result);
			EntityUtils.consume(httpEntity);
			response.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AutoLog.log.error(e,e.fillInStackTrace());
			}
		}
		return result;
		
	}
	/**
	 * 以application/json格式传参的post方法
	 * @param url
	 * @param parms
	 * @return
	 */
	public String doPostJson(String url,String parms) {
		String result = "";
		CloseableHttpClient client = creatClient();
		try {
			HttpPost post = new HttpPost(url);
			//创建参数的实体
			StringEntity entity = new StringEntity(parms);
			entity.setContentType("application/json");
			entity.setContentEncoding("UTF-8");
			post.setEntity(entity);
			//判断是否添加其他头域
			if(useHeaderFlag) {
				for(String key : headersMap.keySet()) {
					post.setHeader(key,headersMap.get(key).toString());
				}
			}
			//发送请求
			CloseableHttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity!=null) {
				result = EntityUtils.toString(httpEntity);
			}
			result = DeCode(result);
			EntityUtils.consume(httpEntity);
			response.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AutoLog.log.error(e,e.fillInStackTrace());
			}
		}
		return result;
	}
	
	/**
	 * 文件上传的post方法，使用json的字符串来传参
	 * @return
	 */
	public String doPostUpload(String url,String jsonFile,String... textParam){
		CloseableHttpClient client = creatClient();
		String result = "";
		try {
			HttpPost post = new HttpPost(url);
			MultipartEntityBuilder metb = MultipartEntityBuilder.create();
			if(jsonFile.length()>4&&jsonFile.startsWith("{")&&jsonFile.endsWith("}")) {
				JSONObject json = JSON.parseObject(jsonFile);
				for(String key:json.keySet()) {
					metb.addBinaryBody(key, new File(json.get(key).toString()));
				}
			}
			if (textParam.length > 0) {
				// 解析textParam为json对象格式
				JSONObject textJson = JSON.parseObject(textParam[0]);
				// 遍历json对象中的键值对，作为textbody参数添加
				for (String key : textJson.keySet()) {
					metb.addTextBody(key, textJson.get(key).toString());
				}
			}
			post.setEntity(metb.build());
			CloseableHttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if(entity!=null) {
				result = EntityUtils.toString(entity);
			}
			result = DeCode(result);
			EntityUtils.consume(entity);
			response.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = e.fillInStackTrace().toString();
			AutoLog.log.error(e,e.fillInStackTrace());
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AutoLog.log.error(e,e.fillInStackTrace());
			}
		}
		return result;
	}
	
	/**
	 * xml格式的post请求
	 * @param url
	 * @param parms
	 * @return
	 */
	public String doXmlPost(String url,String parms) {
		String result = "";
		CloseableHttpClient client = creatClient();
		try {
			HttpPost post = new HttpPost(url);
			//创建参数的实体
			StringEntity entity = new StringEntity(parms);
			entity.setContentType("text/xml");
			entity.setContentEncoding("UTF-8");
			post.setEntity(entity);
			//判断是否添加其他头域
			if(useHeaderFlag) {
				for(String key : headersMap.keySet()) {
					post.setHeader(key,headersMap.get(key).toString());
				}
			}
			//发送请求
			CloseableHttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity!=null) {
				result = EntityUtils.toString(httpEntity);
			}
			result = DeCode(result);
			EntityUtils.consume(httpEntity);
			response.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLog.log.error(e,e.fillInStackTrace());
			result = e.fillInStackTrace().toString();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AutoLog.log.error(e,e.fillInStackTrace());
			}
		}
		return result;
	}

}

