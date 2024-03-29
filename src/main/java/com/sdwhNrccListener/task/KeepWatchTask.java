package com.sdwhNrccListener.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.sdwhNrccListener.util.APIUtil;
import com.sdwhNrccListener.util.Constant;

public class KeepWatchTask extends Thread {

	/**
	 * 是否运行
	 */
	private boolean active;
	private boolean checked;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long millis=0;
		int systemFlag = APIUtil.getSystemFlag();
		int epVersion = APIUtil.getEpVersion();
		if(epVersion==Constant.VERSION_1_3) {
			if(systemFlag==Constant.WFRZJXHYXGS) {
				millis=35000;
			}
			else {
				millis=60000;
			}
			
			while (true) {
				try {
					JSONObject ildJO = APIUtil.insertLocationData();
					System.out.println("ildJO==="+ildJO.toString());
					System.out.println("巡回位置更新........"+ildJO.getBoolean("success"));
					if(!ildJO.getBoolean("success")) {
						//https://blog.csdn.net/qq_41548233/article/details/116566144
						//The CATALINA_HOME environment variable is not defined correctly解决方案:配置环境变量,新增CATALINA_HOME:E:\tomcat8.5.57
						String message = ildJO.getString("message");
						System.out.println("message==="+message);
						/*
						if("Read timed out".equals(message)) {//读取时间超时，说明Tomcat端已经宕机了，就得关闭Tomcat进程重启服务
							runBatFile("cmd /c taskkill /f /im java.exe");
							runBatFile("cmd /c "+Constant.TOMCAT_STARTUP_DIR);
						}
						else if("Connection refused: connect".equals(message)||//拒绝连接时，说明Tomcat没开启，就得开启
								"Can't pass in null Dispatch object".equals(message)||//没有batch环境就会报这个异常
								"Can't map name to dispid: GetItem".equals(message)) {//batch服务没开启就会报这个异常，要重启下tomcat，重新检测batch服务是否开启
							runBatFile("cmd /c "+Constant.TOMCAT_SHUTDOWN_DIR);
							runBatFile("cmd /c "+Constant.TOMCAT_STARTUP_DIR);
						}
						*/
					}
					
					JSONObject iwrdJO = APIUtil.insertWarnRecordData();
					System.out.println("iwrdJO==="+iwrdJO.toString());
					
					JSONObject delJO = APIUtil.dataEmployeeLocations();
					String delCode = delJO.getString("code");
					System.out.println("delCode="+delCode);
					if("200".equals(delCode)) {
						JSONObject deaJO = APIUtil.dataEmployeeAlarm();
						System.out.println("deaJO==="+deaJO.toString());
					}
					Thread.sleep(millis);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(epVersion==Constant.VERSION_3_1) {
			/*
			if(systemFlag==Constant.WFPXHGYXGS||
			   systemFlag==Constant.SDFLXCLKJYXGS||
			   systemFlag==Constant.SDXJYJXHXPYXGS
			   )
				millis=70000;
			else if(systemFlag==Constant.WFRZJXHYXGS)
				millis=35000;
			else
				millis=60000;
				*/
			millis=180000;
			APIUtil.receiveMessage();
			while (true) {
				try {
					JSONObject delJO = APIUtil.dataEmployeeLocations();
					String delCode = delJO.getString("code");
					System.out.println("delCode="+delCode);
					
					JSONObject deaJO = APIUtil.dataEmployeeAlarm();
					System.out.println("deaJO==="+deaJO.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					try {
						systemFlag = APIUtil.getSystemFlag();//当前系统标识会不断切换，必须覆盖一下，否则默认为最初的系统标识
						if(systemFlag==Constant.WFPXHGYXGS||//当前系统标识是普鑫、福林、新家园任何一家，就切换为下一个系统
						   systemFlag==Constant.SDFLXCLKJYXGS
						   ||systemFlag==Constant.SDXJYJXHXPYXGS
						   ) {
							APIUtil.switchNextSysFlagByCurSysFlag();
						}
						Thread.sleep(millis);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void runBatFile(String fileUrl) {
		System.out.println("fileUrl==="+fileUrl);
		StringBuilder sb = new StringBuilder();
	    try {
	        Process child = Runtime.getRuntime().exec(fileUrl);
	        InputStream in = child.getInputStream();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
	        String line;
	        while ((line = bufferedReader.readLine()) != null) {
	        	System.out.println(line);
	            sb.append(line + "\n");
	        }
	        in.close();
	        try {
	            child.waitFor();
	            System.out.println("call cmd process finished");
	        } catch (InterruptedException e) {
	        	System.out.println("faild to call cmd process cmd because " + e.getMessage());
	        }
	    } catch (IOException e) {
	    	System.out.println(e.getMessage());
	    }
	}
}
