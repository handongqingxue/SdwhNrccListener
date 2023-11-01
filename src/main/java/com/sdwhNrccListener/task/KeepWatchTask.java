package com.sdwhNrccListener.task;

import org.json.JSONObject;

import com.sdwhNrccListener.util.APIUtil;

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
		try {
			while (true) {
				Thread.sleep(35000);
				JSONObject ildJO = APIUtil.insertLocationData();
				JSONObject delJO = APIUtil.dataEmployeeLocations();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
