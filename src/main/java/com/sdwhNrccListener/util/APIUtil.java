package com.sdwhNrccListener.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class APIUtil {
	
	private static int cityFlag;
	private static int systemFlag;
	private static int epVersion;
	public static int getEpVersion() {
		return epVersion;
	}

	private static int apiFlag;
	
	/**
	 * 是否正在读取接口数据
	 */
	public static boolean reading;
	
	static {
		switchSystem(Constant.CUR_SYS_FLAG);
	}
	
	public static void switchSystem(int curSysFlag) {
		switch (curSysFlag) {
		case Constant.WFRZJXHYXGS:
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.WFRZJXHYXGS;
			epVersion=Constant.VERSION_1_3;
			apiFlag=Constant.SDWH;
			break;
		case Constant.SDFLXCLKJYXGS:
			cityFlag=Constant.HE_ZE;
			systemFlag=Constant.SDFLXCLKJYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		}
	}

	//https://www.cnblogs.com/aeolian/p/7746158.html
	//https://www.cnblogs.com/bobc/p/8809761.html
	public static JSONObject doHttp(String method, Map<String, Object> params) throws IOException {
		JSONObject resultJO = null;
		try {
			// 构建请求参数  
			StringBuffer paramsSB = new StringBuffer();
			if (params != null) {  
			    for (Entry<String, Object> e : params.entrySet()) {
			    	paramsSB.append(e.getKey());  
			    	paramsSB.append("=");  
			    	paramsSB.append(e.getValue());  
			    	paramsSB.append("&");  
			    }  
			    paramsSB.substring(0, paramsSB.length() - 1);
			}  
			
			StringBuffer sbf = new StringBuffer(); 
			String strRead = null; 
			URL url = new URL(Constant.SERVICE_URL+method);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			//connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);//Read timed out
			connection.setRequestMethod("POST");//请求post方式
			connection.setDoInput(true); 
			connection.setDoOutput(true); 
			//header内的的参数在这里set    
			//connection.setRequestProperty("key", "value");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.connect(); 
			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8"); 
			//OutputStream writer = connection.getOutputStream(); 
			writer.write(paramsSB.toString());
			writer.flush();
			InputStream is = connection.getInputStream(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead); 
				sbf.append("\r\n"); 
			}
			reader.close();
			
			connection.disconnect();
			String result = sbf.toString();
			System.out.println("result==="+result);
			resultJO = new JSONObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultJO = new JSONObject();
			resultJO.put("success", "false");
			resultJO.put("message", e.getMessage());//Read timed out
		}
		return resultJO;
	}

	/**
	 * 插入人员位置信息
	 * @return
	 */
	public static JSONObject insertLocationData() {
		// TODO Auto-generated method stub
		JSONObject resultJO = null;
		reading=true;
		System.out.println("reading2==="+reading);
		try {
			int epFlag = systemFlag;
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("epFlag", epFlag);
	        resultJO = doHttp(Constant.EP_V1_3+"insertLocationData",params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if("Connection refused: connect".equals(e.getMessage())) {
				resultJO.put("success", "false");
				resultJO.put("message", e.getMessage());
			}
		}
		finally {
			reading=false;
			System.out.println("resultJO==="+resultJO.toString());
			return resultJO;
		}
	}

	/**
	 * 上传省平台位置信息
	 * @return
	 */
	public static JSONObject dataEmployeeLocations() {
		// TODO Auto-generated method stub
		JSONObject resultJO = null;
		reading=true;
		System.out.println("reading2==="+reading);
		try {
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("cityFlag", cityFlag);
			params.put("systemFlag", systemFlag);
	        resultJO = doHttp(Constant.SDWH_API+"/dataEmployeeLocations",params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if("Connection refused: connect".equals(e.getMessage())) {
				resultJO.put("success", "false");
				resultJO.put("message", e.getMessage());
			}
		}
		finally {
			reading=false;
			System.out.println("resultJO==="+resultJO.toString());
			return resultJO;
		}
	}
}
