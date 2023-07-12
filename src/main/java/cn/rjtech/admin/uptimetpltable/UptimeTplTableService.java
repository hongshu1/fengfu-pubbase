package cn.rjtech.admin.uptimetpltable;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.UptimeTplTable;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 稼动时间建模-参数表格
 * @ClassName: UptimeTplTableService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:15
 */
public class UptimeTplTableService extends BaseService<UptimeTplTable> {
	private final UptimeTplTable dao=new UptimeTplTable().dao();
	@Override
	protected UptimeTplTable dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Page<UptimeTplTable> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param uptimeTplTable
	 * @return
	 */
	public Ret save(UptimeTplTable uptimeTplTable) {
		if(uptimeTplTable==null || isOk(uptimeTplTable.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeTplTable.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeTplTable.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplTable.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeTplTable
	 * @return
	 */
	public Ret update(UptimeTplTable uptimeTplTable) {
		if(uptimeTplTable==null || notOk(uptimeTplTable.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeTplTable dbUptimeTplTable=findById(uptimeTplTable.getIAutoId());
		if(dbUptimeTplTable==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeTplTable.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeTplTable.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplTable.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeTplTable 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeTplTable uptimeTplTable, Kv kv) {
		//addDeleteSystemLog(uptimeTplTable.getIAutoId(), JBoltUserKit.getUserId(),uptimeTplTable.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeTplTable model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeTplTable uptimeTplTable, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public Boolean deleteByUptimeTplMid(Long iuptimecategoryid) {
		delete(deleteSql().eq("iUptimeTplMid", iuptimecategoryid));
		return true;
	}

	/**
	 *
	 * @param kv
	 * @return
	 */
	public List<Record> uptimeTplTableDatas(Kv kv) {
		if (isNull(kv.get("iuptimetplmid"))) {
			return null;
		}
		return dbTemplate("uptimetpltable.uptimeTplTableDatas", kv).find();
	}
}