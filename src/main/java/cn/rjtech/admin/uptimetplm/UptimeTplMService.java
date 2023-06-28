package cn.rjtech.admin.uptimetplm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.uptimetplcategory.UptimeTplCategoryService;
import cn.rjtech.admin.uptimetpltable.UptimeTplTableService;
import cn.rjtech.model.momdata.UptimeTplCategory;
import cn.rjtech.model.momdata.UptimeTplM;
import cn.rjtech.model.momdata.UptimeTplTable;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 稼动时间建模-稼动时间模板主表
 * @ClassName: UptimeTplMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-27 19:20
 */
public class UptimeTplMService extends BaseService<UptimeTplM> {
	private final UptimeTplM dao=new UptimeTplM().dao();
	@Override
	protected UptimeTplM dao() {
		return dao;
	}
	@Inject
	private UptimeTplCategoryService uptimeTplCategoryService;
	@Inject
	private UptimeTplTableService uptimeTplTableService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param kv  查询参数
	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("uptimetplm.getAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param uptimeTplM
	 * @return
	 */
	public Ret save(UptimeTplM uptimeTplM) {
		if(uptimeTplM==null || isOk(uptimeTplM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=uptimeTplM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param uptimeTplM
	 * @return
	 */
	public Ret update(UptimeTplM uptimeTplM) {
		if(uptimeTplM==null || notOk(uptimeTplM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		UptimeTplM dbUptimeTplM=findById(uptimeTplM.getIAutoId());
		if(dbUptimeTplM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=uptimeTplM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(), uptimeTplM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param uptimeTplM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(UptimeTplM uptimeTplM, Kv kv) {
		//addDeleteSystemLog(uptimeTplM.getIAutoId(), JBoltUserKit.getUserId(),uptimeTplM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param uptimeTplM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(UptimeTplM uptimeTplM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 保存
	 * @param kv
	 * @return
	 */
	public Ret submitAll(Kv kv) {
		tx(() -> {
			UptimeTplM uptimeTplM = new UptimeTplM();
			uptimeTplM.setIsEnabled(kv.getBoolean("uptimeTplM.isEnabled"));
			uptimeTplM.setIUpdateBy(JBoltUserKit.getUserId());
			uptimeTplM.setCUpdateName(JBoltUserKit.getUserName());
			uptimeTplM.setDUpdateTime(new Date());
			uptimeTplM.setIWorkRegionMid(kv.getLong("uptimeTplM.iWorkRegionMid"));
			uptimeTplM.setIWorkShiftMid(kv.getLong("uptimeTplM.iWorkShiftMid"));
			uptimeTplM.setIBaseMins(kv.getInt("uptimeTplM.iBaseMins"));
			uptimeTplM.setIStopMins(kv.getInt("uptimeTplM.iStopMins"));
			uptimeTplM.setISwitchMins(kv.getInt("uptimeTplM.iSwitchMins"));
			uptimeTplM.setIOtMins(kv.getInt("uptimeTplM.iOtMins"));
			uptimeTplM.setIWorkMins(kv.getInt("uptimeTplM.iWorkMins"));
			if (isNull(kv.getLong("uptimeTplM.iAutoId"))) {
				uptimeTplM.setIOrgId(getOrgId());
				uptimeTplM.setCOrgCode(getOrgCode());
				uptimeTplM.setCOrgName(getOrgName());
				uptimeTplM.setICreateBy(JBoltUserKit.getUserId());
				uptimeTplM.setCCreateName(JBoltUserKit.getUserName());
				uptimeTplM.setDCreateTime(new Date());
				uptimeTplM.setIsDeleted(false);
				uptimeTplM.save();
			} else {
				uptimeTplM.setIAutoId(kv.getLong("uptimeTplM.iAutoId"));
			}
			uptimeTplM.update();

			// 解析表格分类
			uptimeTplCategoryService.delectByUptimeTplMid(uptimeTplM.getIAutoId());
			List<UptimeTplCategory> uptimeTplCategoryList = JSONArray.parseArray(kv.getStr("uptimetplcategorydatas"), UptimeTplCategory.class);
			if (!uptimeTplCategoryList.isEmpty()) {
				Integer iSeq = 1;
				for (UptimeTplCategory uptimeTplCategory : uptimeTplCategoryList) {
					if (isOk(uptimeTplCategory.getIAutoId()) && isNull(uptimeTplCategory.getIUptimeCategoryId())) {
						uptimeTplCategory.setIUptimeCategoryId(uptimeTplCategory.getIAutoId());
					}
					uptimeTplCategory.setIUptimeTplMid(uptimeTplM.getIAutoId());
					uptimeTplCategory.setIAutoId(null);
					uptimeTplCategory.setISeq(iSeq);
					iSeq++;
				}
				uptimeTplCategoryService.batchSave(uptimeTplCategoryList);
			}

			// 解析表格内容
			uptimeTplTableService.deleteByUptimeTplMid(uptimeTplM.getIAutoId());
			List<UptimeTplTable> uptimeTplTables = JSONArray.parseArray(kv.getStr("uptimetpltabledatas"), UptimeTplTable.class);
			if (!uptimeTplTables.isEmpty()) {
				Integer iSeq = 1;
				for (UptimeTplTable uptimeTplTable : uptimeTplTables) {
					uptimeTplTable.setISeq(iSeq);
					uptimeTplTable.setIUptimeTplMid(uptimeTplM.getIAutoId());
					iSeq++;
				}
				uptimeTplTableService.batchSave(uptimeTplTables);
			}
			return true;
		});

		return SUCCESS;
	}

	/**
	 * 删除
	 * @param iuptimecategoryid 稼动时间模板主键
	 * @return
	 */
	public Ret delete(Long iuptimecategoryid) {
		tx(() -> {
			updateColumn(iuptimecategoryid, "isDeleted", TRUE);
			return true;
		});
		return SUCCESS;
	}
}