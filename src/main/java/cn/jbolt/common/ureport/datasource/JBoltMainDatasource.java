package cn.jbolt.common.ureport.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import com.jfinal.plugin.druid.DruidPlugin;

import cn.jbolt.core.base.config.JBoltConfig;

/**
 * ureport使用的内置平台数据源
 * @ClassName:  JBoltMainDatasource   
 * @author: JFinal学院-小木 QQ：909854136 
 * @date:   2021年5月10日   
 */
public class JBoltMainDatasource implements BuildinDatasource {
	
	static DruidPlugin druidplugin;
	static{
		druidplugin=JBoltConfig.createDruidPlugin();
		druidplugin.start();
	}
	
	@Override
	public String name() {
		return "main";
	}

	@Override
	public Connection getConnection() {
		try {			
			return druidplugin.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
