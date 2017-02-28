package com.fbs.sys.uis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.fbs.sys.Controlor;
import com.fbs.sys.tools.Operations;

public class MainFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton run,stop,clear,test;
	private JMenuItem setPars;
	private static JTextArea text;
	
	//private final static int MAXLINESIZE=30;
	
	private Controlor control=Controlor.getInstance();
	
	public MainFrame() {
		super("Coal & Gangue");
		run=new JButton("RUN");
		run.addActionListener(this);
		stop=new JButton("STOP");
		stop.addActionListener(this);
		clear=new JButton("CLR");
		clear.addActionListener(this);
		test=new JButton("TEST");
		test.addActionListener(this);
		
		text=new JTextArea();
		text.setEditable(false);
		
		Box box=Box.createVerticalBox();
		box.add(Box.createVerticalStrut(50));
		box.add(run);
		box.add(Box.createVerticalStrut(50));
		box.add(stop);
		box.add(Box.createVerticalStrut(50));
		box.add(clear);
		box.add(Box.createVerticalStrut(100));
		box.add(test);
		
		JPanel showP=new JPanel();
		showP.add(text);
		JScrollPane scroll=new JScrollPane(showP);
		JPanel leftP=new JPanel();
		leftP.add(box);
		setLayout(new BorderLayout());
		add(scroll,BorderLayout.CENTER);
		add(leftP,BorderLayout.EAST);
		
		JMenuBar bar=new JMenuBar();
		JMenu m1=new JMenu("菜单");
		setPars=new JMenuItem("参数设置");
		setPars.addActionListener(this);
		m1.add(setPars);
		bar.add(m1);
		setJMenuBar(bar);
		
		setVisible(true);
		setBounds(10,10,600,500);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
              int n=JOptionPane.showConfirmDialog(null,"确认退出吗?","确认对话框",
                                       JOptionPane.YES_NO_OPTION );
              if(n==JOptionPane.YES_OPTION)  
                 System.exit(0);
            }});
		
		validate();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == run){
			if(control.isRun()){
				text.append("Is runing!\n");
				return;
			}
			if(control.testRun()){
				int n=JOptionPane.showConfirmDialog(
						this,"正在测试数据，是否停止?","STOP",
						JOptionPane.YES_NO_OPTION );
				if(n==JOptionPane.YES_OPTION)
					control.testStop();
				return;
			}
			System.out.println("Initializing...");
			text.setText("");
			if(!Operations.getInstance().canUse()){
				text.append("系统未初始化，请先初始化！\n");
				new SaveParasDialog(this);
				return;
			}
			control.startMachine();
		}
		else if(e.getSource() == stop){
			if(control.isRun()){
				int n=JOptionPane.showConfirmDialog(
						this,"停止后所有正在执行任务将被清空，是否停止?","STOP",
						JOptionPane.YES_NO_OPTION );
				if(n==JOptionPane.YES_OPTION)
					control.stopMachine();
				return;
			}
			else if(control.testRun()){
				int n=JOptionPane.showConfirmDialog(
						this,"正在测试数据，是否停止?","STOP",
						JOptionPane.YES_NO_OPTION );
				if(n==JOptionPane.YES_OPTION)
					control.testStop();
				return;
			}
			text.append("Is stop!");
		}
		else if(e.getSource() == clear){
			text.setText("");
		}
		else if(e.getSource() == test){
			 if(control.isRun()){
					int n=JOptionPane.showConfirmDialog(
							this,"正在运行，是否停止?","STOP",
							JOptionPane.YES_NO_OPTION );
					if(n==JOptionPane.YES_OPTION)
						control.stopMachine();
					return;
			}
			if(control.testRun()){
				showMessage("Is Testing!");
				return;
			}
			text.setText("");
			if(!Operations.getInstance().canUse()){
				text.append("系统未初始化，请先初始化！\n");
				new SaveParasDialog(this);
				return;
			}
			control.testData();
		}
		else if(e.getSource() == setPars){
			if(control.isRun()){
				JOptionPane.showMessageDialog(this,
						 "系统正在运行，请先关闭系统！",
						 "!",JOptionPane.INFORMATION_MESSAGE);
			}
			else
				new SaveParasDialog(this);
		}
	}
	synchronized public static void showMessage(String msg){
		text.append(msg+"\n");
		//if(text.getLineCount() > MAXLINESIZE)
			//text.setText("");
	}
	
	public static void main(String args[]){
		new MainFrame();
	}
}
