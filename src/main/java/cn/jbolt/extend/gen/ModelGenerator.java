package cn.jbolt.extend.gen;

import cn.jbolt.core.base.JBoltIDGenMode;
import cn.jbolt.core.gen.JBoltColumnToBuildAttrNameFunction;
import cn.jbolt.core.gen.JBoltProjectGenConfig;
import cn.jbolt.core.gen.JFinalModelGenerator;

/**
 * jfinal model Generator 主要用来生成Model和BaseModel
 * @ClassName:  JFinalModelGenerator   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年4月11日09:24:23
 */
public class ModelGenerator extends JFinalModelGenerator{
    
	public static void main(String[] args) {
		//要生成的代码是在哪个项目下面 设置项目跟路径
		String projectRootPath=System.getProperty("user.dir");
		//数据源配置名称 默认主数据源是main 其他的在extend_datasource.setting里配置的
		String configName="main";
		//指定本次运行直接生成的表名 忽略其它所有表 数组为空 表示忽略此强制设定 当需要单个指定生成时才需要这个
		String[] tableNames = new String[] { /* "jb_user","jb_application" */};
		//生成Model放在哪个包下
		String modelPackage="cn.jbolt.common.model";
		//默认ID生成模式
		String idGenMode=JBoltIDGenMode.SNOWFLAKE;//auto|snowflake|sequence
		//数据库字典文件版本号 自己定义
		String dataDictionaryVersion="1.0.0";
		//数据流字典文件的简介描述信息
		String dataDictionaryDescription="JBolt极速开发平台"+(configName.equals("main")?"主数据源":"扩展数据源["+configName+"]")+"数据库字典";
		//是否生成Model和BaseModel 如果设置为false 则只判断是否生成字典文件 直接不进行model和baseModel的生成
		boolean genModel=true;
		//本次执行生成 是否生成JBolt核心库 model和baseModel 一般只生成自己的业务表 就设置为false
		//注意自己的业务表不要使用jb_开头的前缀 	
		boolean genJBoltCoreModel=true;
		//是否生成html格式数据字典
		boolean genHtmlDataDictionary=true;
		//生成的Model java类需要去掉的前缀 多个用逗号隔开 内置已经去掉了核心表的前缀jb_
		String removedTableNamePrefixes="";
		
		//下面这个默认是null就行 自定义的数据库字段转驼峰getter属性名的策略，
		//默认使用策略已经够用，如果你有特殊需求就在这里定义它
		JBoltColumnToBuildAttrNameFunction columnTobuildAttrNameFun=null;
//		JBoltColumnToBuildAttrNameFunction columnTobuildAttrNameFun = new JBoltColumnToBuildAttrNameFunction() {
//			@Override
//			public String build(String column) {
//				//这里column就是数据库里的一个字段 然后你通过处理返回一个应有的定制格式即可
//				return null;
//			}
//		};
		
		//初始化项目配置
		JBoltProjectGenConfig.init(projectRootPath,modelPackage,genModel,idGenMode,genHtmlDataDictionary,genJBoltCoreModel,removedTableNamePrefixes,columnTobuildAttrNameFun,tableNames);
		//执行Model、BaseModel、数据字典Html的生成
		new ModelGenerator().generate(configName,dataDictionaryVersion,dataDictionaryDescription);
	}
}
