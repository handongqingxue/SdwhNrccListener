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
	public static int getSystemFlag() {
		return systemFlag;
	}

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
	
	/**
	 * 根据当前系统标识选择系统
	 * @param curSysFlag
	 */
	public static void switchSystem(int curSysFlag) {
		switch (curSysFlag) {
		case Constant.WFPXHGYXGS://普鑫
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.WFPXHGYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.SDFLXCLKJYXGS://福林
			cityFlag=Constant.HE_ZE;
			systemFlag=Constant.SDFLXCLKJYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.SDXJYJXHXPYXGS://新家园
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.SDXJYJXHXPYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.SDBFXCLYXGS://宝沣
			cityFlag=Constant.TAI_AN;
			systemFlag=Constant.SDBFXCLYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.WFRZJXHYXGS://润中
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.WFRZJXHYXGS;
			epVersion=Constant.VERSION_1_3;
			apiFlag=Constant.SDWH;
			break;
		case Constant.CYSRHSWKJYXGS://瑞海
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.CYSRHSWKJYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.SDLTXDKJYXGS://蓝天
			cityFlag=Constant.WEI_FANG;
			systemFlag=Constant.SDLTXDKJYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.SDWH;
			break;
		case Constant.ZBXQHGYXGS://鑫乾
			cityFlag=Constant.ZI_BO;
			systemFlag=Constant.ZBXQHGYXGS;
			epVersion=Constant.VERSION_3_1;
			apiFlag=Constant.LZQ;
			break;
		}
	}
	
	/**
	 * 根据当前系统标识选择下一个系统(用于多个企业部署在同一台服务器情况下，按队列排队推送省平台)
	 */
	public static void switchNextSysFlagByCurSysFlag() {
		System.out.println("curSystemFlag???=="+systemFlag);
		int nextSysFlag=0;
		if(systemFlag==Constant.WFPXHGYXGS) {//当前系统是普鑫，下一个推送的系统就是福林
			//System.out.println("1111111111");
			nextSysFlag=Constant.SDFLXCLKJYXGS;
		}
		else if(systemFlag==Constant.SDFLXCLKJYXGS) {//当前系统是福林，下一个推送的系统就是新家园
			//System.out.println("22222222");
			nextSysFlag=Constant.SDXJYJXHXPYXGS;
		}
		else if(systemFlag==Constant.SDXJYJXHXPYXGS) {//当前系统是新家园，下一个推送的系统就是普鑫
			//System.out.println("333333333");
			nextSysFlag=Constant.WFPXHGYXGS;
		}
		APIUtil.switchSystem(nextSysFlag);//把当前系统标识替换为下一个系统标识
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
			connection.setReadTimeout(25000);//Read timed out
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
	 * 插入报警信息
	 * @return
	 */
	public static JSONObject insertWarnRecordData() {
		// TODO Auto-generated method stub
		JSONObject resultJO = null;
		try {
			int epFlag = systemFlag;
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("epFlag", epFlag);
	        resultJO = doHttp(Constant.EP_V1_3+"insertWarnRecordData",params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
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
		String path=null;
		reading=true;
		System.out.println("reading2==="+reading);
		try {
			if(apiFlag==Constant.SDWH)
				path=Constant.SDWH_API;
			else if(apiFlag==Constant.LZQ)
				path=Constant.LZQ_API;
			
			Map<String, Object> params=new HashMap<String, Object>();
			
			System.out.println("cityFlag="+cityFlag);
			System.out.println("systemFlag="+systemFlag);
			params.put("cityFlag", cityFlag);
			params.put("systemFlag", systemFlag);
			
	        resultJO = doHttp(path+"/dataEmployeeLocations",params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultJO.put("success", "false");
			resultJO.put("message", e.getMessage());
		}
		finally {
			reading=false;
			System.out.println("resultJO==="+resultJO.toString());
			return resultJO;
		}
	}
	
	/**
	 * 上传省平台报警信息
	 * @return
	 */
	public static JSONObject dataEmployeeAlarm() {
		// TODO Auto-generated method stub
		JSONObject resultJO = null;
		String path=null;
		try {
			if(apiFlag==Constant.SDWH)
				path=Constant.SDWH_API;
			else if(apiFlag==Constant.LZQ)
				path=Constant.LZQ_API;
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("cityFlag", cityFlag);
			params.put("systemFlag", systemFlag);
	        resultJO = doHttp(path+"/dataEmployeeAlarm",params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			System.out.println("resultJO==="+resultJO.toString());
			return resultJO;
		}
	}

	public static JSONObject receiveMessage() {
		// TODO Auto-generated method stub
		JSONObject resultJO = null;
		String url=null;
		try {
			if(systemFlag==Constant.SDXJYJXHXPYXGS)//新家园用udp协议接收推送
				url=Constant.UDP_RECEIVER+"receiveData";
			else//其他企业用队列协议接收推送
				url=Constant.SERVER_RECEIVER+"receiveMessage";
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("systemFlag", systemFlag);
	        resultJO = doHttp(url,params);
	        
	        if(systemFlag==Constant.WFPXHGYXGS) {//当前系统标识是普鑫，则下一个系统标识是福林，为注册福林的推送队列而再调用一次这个方法
	        	switchSystem(Constant.SDFLXCLKJYXGS);
	        	receiveMessage();
	        }
	        else if(systemFlag==Constant.SDFLXCLKJYXGS) {
	        	switchSystem(Constant.SDXJYJXHXPYXGS);
	        	receiveMessage();
	        }
	        else if(systemFlag==Constant.SDXJYJXHXPYXGS) {//当前系统标识是新家园，说明是最后一个注册对象，就把普鑫当作下一个系统标识，为后面的推送位置和报警信息做准备
	        	switchSystem(Constant.WFPXHGYXGS);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultJO;
		}
	}
}
