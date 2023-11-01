package com.sdwhNrccListener.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.sdwhNrccListener.util.Constant;

public class ListenerTask extends Thread implements ActionListener {
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 初始化主窗口
	 */
	public void initMainJFrame() {
		//https://mbd.baidu.com/ug_share/mbox/4a83aa9e65/share?product=smartapp&tk=06559b750ffdd64b94081e8e3c1fc3d6&share_url=https%3A%2F%2Fsa93g4.smartapps.baidu.com%2Fpages%2Fsquestion%2Fsquestion%3Fqid%3D562121822%26rid%3D1410602677%26_swebfr%3D1%26_swebFromHost%3Dbaiduboxapp&domain=mbd.baidu.com
		JFrame jf=new JFrame(Constant.MAIN_JFRAME_TITLE);
		jf.setBounds(Constant.MAIN_JFRAME_X, Constant.MAIN_JFRAME_Y, Constant.MAIN_JFRAME_WIDTH, Constant.MAIN_JFRAME_HEIGHT);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//jf.add(initMainJPanel());
		
		jf.setVisible(true);//一切都加载完之后才显示主窗口
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
