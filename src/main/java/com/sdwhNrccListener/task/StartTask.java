package com.sdwhNrccListener.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

public class StartTask {

	static KeepWatchTask keepWatchTask;
	static ListenerTask listenerTask;

	public static void main(String[] args) {
		keepWatchTask=new KeepWatchTask();
		keepWatchTask.setActive(true);

		listenerTask=new ListenerTask();
		listenerTask.initMainJFrame();
		
		keepWatchTask.start();
		listenerTask.start();
	}
}
