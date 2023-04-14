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
	SCHOOL("学校",20006),

	EMAIL_RECEIVER("邮件接收人", 20007),
	APPLICATION("应用",20010),
	PERMISSION_BTN("菜单按钮",20011),
	BUS_OBJECT("业务对象",20013),
	DATA_PERMISSION("数据权限",20014),
	REPAIR_APPLY("维修申请",20015),
	EQUIPMENT_TEAM("设备班组",20016),
	REPAIR_APLY_ASSIGN("维修派工",20017),
	REPAIR_APPLY_ISSUE("维修申请委外",20018),
	REPAIR_RECORD("设备维修记录",20019),
    APP_VERSION("应用版本", 20020),
	WEEK_ORDER("周间客户订单", 20021),
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