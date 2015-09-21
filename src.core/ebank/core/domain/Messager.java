/*
 * @Id: Messager.java 10:04:13 2006-2-18
 * 
 * @author 
 * @version 1.0
 * PAYGW_CORE PROJECT
 */
package ebank.core.domain;

import java.io.Serializable;

/**
 * @author xiexh
 * Description: 
 * 
 */
public class Messager implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int type;             //客户通知类型
	private String content;       //通知内容
	private String to;            //send to who
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	

}
