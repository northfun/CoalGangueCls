package com.fbs.sys.uis;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.fbs.sys.tools.Operations;

public class SaveParasDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField imagedir;
	private DigitalJTextField threshold;
	private JRadioButton filterwidth1;
	private JRadioButton filterwidth2;
	private JRadioButton filterwidth3;
	private DigitalJTextField minmeanvalue;
	private DigitalJTextField minvariance;
	private DigitalJTextField mincount;
	private DigitalJTextField maxmeanvalue;
	private DigitalJTextField maxvariance;
	private DigitalJTextField mvdeviation;
	private DigitalJTextField vrdeviation;
	private JButton getfiledir;
	private JButton save;
	private JButton cancel;
	private JLabel saved;
	
	public SaveParasDialog(JFrame mainFrame) throws HeadlessException {
		super(mainFrame,"设置参数", true);
		
		JLabel ldir=new JLabel("*设备路径:");
		imagedir=new JTextField();
		imagedir.setEditable(false);
		getfiledir=new JButton("选择");
		getfiledir.addActionListener(this);
		
		JLabel lft=new JLabel("*中值滤波窗口大小:");
		filterwidth1=new JRadioButton("3*3");
		filterwidth2=new JRadioButton("5*5");
		filterwidth3=new JRadioButton("7*7");
		ButtonGroup group = new ButtonGroup();
		group.add(filterwidth1);
		group.add(filterwidth2);
		group.add(filterwidth3);
		
		JLabel ltsh=new JLabel("*二值化阈值:");
		threshold=new DigitalJTextField(JTextFieldType.PINTEGER);
		
		JLabel lmc=new JLabel("*最小周长(Pixel):");
		mincount=new DigitalJTextField(JTextFieldType.PINTEGER);
		
		JLabel lmv=new JLabel("最小均值:");
		minmeanvalue=new DigitalJTextField(JTextFieldType.PFLOAT);
		JLabel ldmv=new JLabel("最大均值:");
		maxmeanvalue=new DigitalJTextField(JTextFieldType.PFLOAT);
		JLabel mvde=new JLabel("均值误差:");
		mvdeviation=new DigitalJTextField(JTextFieldType.PFLOAT);
		JLabel lvr=new JLabel("最小方差:");
		minvariance=new DigitalJTextField(JTextFieldType.PFLOAT);
		JLabel ldvr=new JLabel("最大方差:");
		maxvariance=new DigitalJTextField(JTextFieldType.PFLOAT);
		JLabel vrde=new JLabel("方差误差:");
		vrdeviation=new DigitalJTextField(JTextFieldType.PFLOAT);
		
		saved=new JLabel("");
		save=new JButton("保存");
		save.addActionListener(this);
		cancel=new JButton("取消");
		cancel.addActionListener(this);
		
		Box hb1=Box.createHorizontalBox();
		hb1.add(Box.createHorizontalStrut(5));
		hb1.add(ldir); hb1.add(Box.createHorizontalStrut(5));
		hb1.add(imagedir); hb1.add(Box.createHorizontalStrut(5));
		hb1.add(getfiledir);
		hb1.add(Box.createHorizontalStrut(5));
		Box hb2=Box.createHorizontalBox();
		hb2.add(lft); hb2.add(Box.createHorizontalStrut(20));
		hb2.add(filterwidth1); hb2.add(Box.createHorizontalStrut(5));
		hb2.add(filterwidth2); hb2.add(Box.createHorizontalStrut(5));
		hb2.add(filterwidth3); hb2.add(Box.createHorizontalStrut(5));
		Box hb3=Box.createHorizontalBox();
		hb3.add(Box.createHorizontalStrut(5));
		hb3.add(ltsh); hb3.add(Box.createHorizontalStrut(5));
		hb3.add(threshold); hb3.add(Box.createHorizontalStrut(5));
		hb3.add(lmc); hb3.add(Box.createHorizontalStrut(5));
		hb3.add(mincount);
		hb3.add(Box.createHorizontalStrut(5));
		Box hb5=Box.createHorizontalBox();
		hb5.add(Box.createHorizontalStrut(5));
		hb5.add(lmv); hb5.add(Box.createHorizontalStrut(5));
		hb5.add(minmeanvalue); hb5.add(Box.createHorizontalStrut(5));
		hb5.add(ldmv); hb5.add(Box.createHorizontalStrut(5));
		hb5.add(maxmeanvalue); hb5.add(Box.createHorizontalStrut(5));
		hb5.add(mvde); hb5.add(Box.createHorizontalStrut(5));
		hb5.add(mvdeviation);
		hb5.add(Box.createHorizontalStrut(5));
		Box hb6=Box.createHorizontalBox();
		hb6.add(Box.createHorizontalStrut(5));
		hb6.add(lvr); hb6.add(Box.createHorizontalStrut(5));
		hb6.add(minvariance); hb6.add(Box.createHorizontalStrut(5));
		hb6.add(ldvr); hb6.add(Box.createHorizontalStrut(5));
		hb6.add(maxvariance); hb5.add(Box.createHorizontalStrut(5));
		hb6.add(vrde); hb6.add(Box.createHorizontalStrut(5));
		hb6.add(vrdeviation);
		hb6.add(Box.createHorizontalStrut(5));
		Box hb7=Box.createHorizontalBox();
		hb7.add(saved); hb7.add(Box.createHorizontalStrut(10));
		hb7.add(save); hb7.add(Box.createHorizontalStrut(60));
		hb7.add(cancel);
		
		Box vb=Box.createVerticalBox();
		vb.add(Box.createVerticalStrut(20));
		vb.add(hb1); vb.add(Box.createVerticalStrut(20));
		vb.add(hb2); vb.add(Box.createVerticalStrut(20));
		vb.add(hb3); vb.add(Box.createVerticalStrut(20));
		vb.add(hb3); vb.add(Box.createVerticalStrut(20));
		vb.add(hb5); vb.add(Box.createVerticalStrut(20));
		vb.add(hb6); vb.add(Box.createVerticalStrut(20));
		vb.add(hb7); vb.add(Box.createVerticalStrut(20));

		readParas();
		
		//add(new JPanel().add(vb));
		this.getContentPane().add(vb);
		setSize(600,350);
		setVisible(true);
		validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getfiledir){
			JFileChooser chooser=new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal=chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION)
				imagedir.setText(chooser.getSelectedFile().getAbsolutePath());
		}
		else if(e.getSource() == save){
			if((imagedir.getText() != null) && (threshold.getText() != null) && 
					/*(minmeanvalue != null) && (minvariance != null) && 
					(maxmeanvalue != null) && (maxvariance != null) &&
					*/ 
					(filterwidth1.isSelected() || filterwidth2.isSelected() || 
						filterwidth3.isSelected())){
				int fw,mc,tsh;
				double vr,mv,mvde,vrde,mvd,vrd;
				String imgdir;
				imgdir=imagedir.getText().trim();
				if(filterwidth1.isSelected())
					fw=3;
				else if(filterwidth2.isSelected())
					fw=5;
				else
					fw=7;
				tsh=threshold.getInt();
				mc=mincount.getInt();
				mv=minmeanvalue.getFloat();
				mvd=maxmeanvalue.getFloat();
				mvde=mvdeviation.getFloat();
				vr=minvariance.getFloat();
				vrd=maxvariance.getFloat();
				vrde=vrdeviation.getFloat();
				
				Operations.getInstance().writeParas(
						imgdir, fw, tsh, mc,
						mv, mvd, mvde, vr, vrd, vrde);
				saved.setText("成功保存！");
			}
			else
				saved.setText("*号为必填项！");
		}
		else if(e.getSource() == cancel){
			this.dispose();
		}
	}
	
	private void readParas(){
		Operations op=Operations.getInstance();
		if(!op.canUse())
			return;
		imagedir.setText(op.getImageDir());
		int fw=op.getFilterWidth();
		switch(fw){
		case(3):
			filterwidth1.setSelected(true);
			break;
		case(5):
			filterwidth2.setSelected(true);
			break;
		case(7):
			filterwidth3.setSelected(true);
			break;
		}
		threshold.setText(op.getThreshold()+"");
		mincount.setText(op.getMincount()+"");
		minmeanvalue.setText(op.getMinMeanValue()+"");
		maxmeanvalue.setText(op.getMaxMeanValue()+"");
		mvdeviation.setText(op.getVrdeviation()+"");
		minvariance.setText(op.getMinVariance()+"");
		maxvariance.setText(op.getMaxVariance()+"");
		vrdeviation.setText(op.getVrdeviation()+"");
	}
}
