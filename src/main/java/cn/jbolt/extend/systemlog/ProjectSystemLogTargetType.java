package cn.jbolt.extend.systemlog;

import cn.jbolt.core.enumutil.JBoltEnum;
/**
 * 项目中systemLog关联类型
 * @ClassName:  ProjectSystemLogTargetType   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2022年2月24日   
 */
public enum ProjectSystemLogTargetType {
	NONE("NONE",0),
	GRADE("年级",20005),
	SCHOOL("学校",20006)
	;
	private String text;
	private int value;
	private ProjectSystemLogTargetType(String text,int value) {
		this.text = text;
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	//加入到JBolt Enum管理器
	static {
		JBoltEnum.addToTvBeanMap(ProjectSystemLogTargetType.class);
	}
}