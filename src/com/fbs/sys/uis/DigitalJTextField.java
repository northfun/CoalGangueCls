package com.fbs.sys.uis;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

enum JTextFieldType{
    PINTEGER,INTEGER,PFLOAT,FLOAT,ALL
};
    
public class DigitalJTextField extends JTextField {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DigitalJTextField(final JTextFieldType type) {
    	super("");
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char input=e.getKeyChar();
                switch(type){
                case PINTEGER:
                	if (!Character.isDigit(input) ||
                			!checkPInteger()){
                		deleteInputChar(e);
                	}
                	break;
                case INTEGER:
                	if (!Character.isDigit(input) ||
                			!checkInteger()){
                		deleteInputChar(e);
                	}
                	break;
                case PFLOAT:
                	if(!Character.isDigit(input) ||
                			(input == '.' && hasPoint()) || 
                				!checkPFloat()){
                		deleteInputChar(e);
                	}
                	break;
                case FLOAT:
                	if((!Character.isDigit(input) ||
                			input == '.' && hasPoint()) || 
                				!checkFloat()){
                		deleteInputChar(e);
                	}
                	break;
                case ALL:
                	break;
                }
            }

            private void deleteInputChar(KeyEvent source) {
                source.setKeyChar((char) KeyEvent.VK_CLEAR);
            }
        });
    }
    
    public double getFloat(){
    	if(this.getText().endsWith("."))
    		return Double.parseDouble(this.getText().trim().substring(
    					0, this.getText().length()-2));
    	else
    		return this.getText().equals("")?0:Double.parseDouble(this.getText().trim());
    }
    public int getInt(){
    	return this.getText().equals("")?0:Integer.parseInt(this.getText().trim());
    }
    private boolean checkPInteger(){
    	long i=this.getText().equals("")?0:Long.parseLong(this.getText());
    	if(i >= 0 && i<Integer.MAX_VALUE)
    		return true;
    	return false;
    }
    private boolean checkInteger(){
    	long i=this.getText().equals("")?0:Long.parseLong(this.getText());
    	if(i >= Integer.MIN_VALUE && i<Integer.MAX_VALUE)
    		return true;
    	return false;
    }
    private boolean checkPFloat(){
    	double d=this.getFloat();
    	if(d >= 0 && d<Float.MAX_VALUE)
    		return true;
    	return false;
    }
    private boolean checkFloat(){
    	double d=this.getFloat();
    	if(d >= Float.MIN_VALUE && d<Float.MAX_VALUE)
    		return true;
    	return false;
    }
    private boolean hasPoint(){
    	return (this.getText().indexOf(".") != -1);
    }
}
