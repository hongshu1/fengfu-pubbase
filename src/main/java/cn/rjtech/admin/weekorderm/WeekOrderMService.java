package cn.rjtech.admin.weekorderm;

import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.model.momdata.CustomerClass;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.kit.JBoltUserKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.WeekOrderM;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 客户订单-周间客户订单
 * @ClassName: WeekOrderMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-10 14:37
 */
public class WeekOrderMService extends BaseService<WeekOrderM> {
	private final WeekOrderM dao=new WeekOrderM().dao();
	@Override
	protected WeekOrderM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.WEEK_ORDER.getValue();
    }

	@Inject
	private CustomerService customerService;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @return
	 */
	public Object getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("weekorderm.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 * @param
	 * @return
	 */
	public Ret save(JBoltTable jBoltTable) {
		//参数判空
		if (jBoltTable == null || jBoltTable.isBlank()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}
		WeekOrderM weekOrderM = jBoltTable.getFormModel(WeekOrderM.class,"weekOrderM");
		if(weekOrderM==null || isOk(weekOrderM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//主表保存
		saveContant(weekOrderM);

		if (null!=jBoltTable.getSave()) {
			return updateWeekOrderDs("save", jBoltTable, weekOrderM);
		}
		return SUCCESS;
	}

	/**
	 * 主表保存
	 * @param weekOrderM
	 */
	private void saveContant(WeekOrderM weekOrderM) {
		//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
		weekOrderM.setIAuditStatus(0);
		//订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
		weekOrderM.setIOrderStatus(1);
		//组织信息
		weekOrderM.setIOrgId(getOrgId());
		weekOrderM.setCOrgCode(getOrgCode());
		weekOrderM.setCOrgName(getOrgName());
		//创建信息
		weekOrderM.setCCreateName(JBoltUserKit.getUserName());
		weekOrderM.setDCreateTime(new Date());
		weekOrderM.setICreateBy(JBoltUserKit.getUserId());
		//更新信息
		weekOrderM.setCUpdateName(JBoltUserKit.getUserName());
		weekOrderM.setDUpdateTime(new Date());
		weekOrderM.setIUpdateBy(JBoltUserKit.getUserId());
		boolean success=weekOrderM.save();
		if(success) {
			//添加日志
			addSaveSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
		}
	}

	/**
	 * 更新
	 * @param
	 * @return
	 */
	public Ret update(JBoltTable jBoltTable) {
		WeekOrderM weekOrderM = jBoltTable.getFormModel(WeekOrderM.class,"weekOrderM");
		if(weekOrderM==null || null==weekOrderM.getIAutoId()) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		WeekOrderM dbWeekOrderM=findById(weekOrderM.getIAutoId());
		if(dbWeekOrderM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(weekOrderM.getName(), weekOrderM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}

		//更新信息
		weekOrderM.setCUpdateName(JBoltUserKit.getUserName());
		weekOrderM.setDUpdateTime(new Date());
		weekOrderM.setIUpdateBy(JBoltUserKit.getUserId());
		boolean success=weekOrderM.update();
		if(success) {
			//添加日志
			addUpdateSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
		}

		if (null!=jBoltTable.getSave()) {
			return updateWeekOrderDs("save",jBoltTable,weekOrderM);
		} else if (null!=jBoltTable.getUpdate()) {
			return updateWeekOrderDs("update",jBoltTable,weekOrderM);
		}
		return ret(success);
	}

	/**
	 * 周间客户订单详情表处理
	 * @param mark 标记
	 * @param jBoltTable 收到的数据
	 * @param weekOrderM 主表信息
	 * @return
	 */
	private Ret updateWeekOrderDs(String mark , JBoltTable jBoltTable,WeekOrderM weekOrderM) {
		List<WeekOrderD> weekOrderDs = null;
		switch (mark) {
			//修改
			case "edit":
				weekOrderDs = jBoltTable.getUpdateModelList(WeekOrderD.class);
				for (WeekOrderD weekOrderD : weekOrderDs) {
					weekOrderD.update();
				}
			//保存
			case "save":
				weekOrderDs = jBoltTable.getSaveModelList(WeekOrderD.class);
				for (WeekOrderD weekOrderD : weekOrderDs) {
					if(weekOrderD==null || isOk(weekOrderD.getIAutoId())) {
						return fail(JBoltMsg.PARAM_ERROR);
					}
					//主表id
					weekOrderD.setIWeekOrderMid(weekOrderM.getIAutoId());
					weekOrderD.setIsDeleted(false);
					boolean success=weekOrderD.save();
					if(success) {
						//添加日志
						addSaveSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
					}
				}
		}

		return SUCCESS;
	}

	/**
	 * 删除数据后执行的回调
	 * @param weekOrderM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(WeekOrderM weekOrderM, Kv kv) {
		addDeleteSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(),weekOrderM.getIAutoId().toString());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param weekOrderM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(WeekOrderM weekOrderM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 新增前查询
	 */
    public WeekOrderM findAddData() {
		return findFirst(selectSql().orderBy("dCreateTime", "desc"));
    }

	public Ret delete(Long id) {
    	return toggleBoolean(id, "IsDeleted");
	}

	public Object weekOrderMData(Kv kv) {
		ValidationUtils.notNull(kv.getLong("iWeekOrderMid"), JBoltMsg.PARAM_ERROR);
		return dbTemplate("weekorderm.weekOrderMData",kv).find();
	}

	public List<Record> findByIdToShow(Long id) {
		Kv kv= Kv.create();
		kv.set("iAutoId", id);
//		Sql sql = selectSql().from(table(), "this")
//				.select("this.*, c.cCusName")
//				.leftJoin(customerService.table(), "c", "c.iAutoId = this.iCustomerId")
//				.eq("this.IsDeleted", ZERO_STR)
//				.eq("c.isDeleted", ZERO_STR)
//				.orderBySortRank();
//		return findFirst(sql);
		return dbTemplate("weekorderm.paginateAdminDatas",kv).find();
	}

	/**
	 * 审批
	 * @param iAutoId
	 * @return
	 */
	public Ret approve(String iAutoId,Integer mark) {
		WeekOrderM weekOrderM = new WeekOrderM();
		boolean success = false;
		if (mark == 1) {
			weekOrderM = findById(iAutoId);
			if (weekOrderM == null || isOk(weekOrderM.getIAutoId())) {
				return fail(JBoltMsg.PARAM_ERROR);
			}
			//订单状态：2. 待审批
			weekOrderM.setIOrderStatus(2);
			//审核状态： 1. 待审核
			weekOrderM.setIAuditStatus(1);
		} else {
				List<WeekOrderM> listByIds = getListByIds(iAutoId);
				//TODO 由于审批流程暂未开发 先审批提审即通过
				if (listByIds.size() > 0) {
					for (WeekOrderM orderM : listByIds) {
						if (orderM.getIOrderStatus() == 3 || orderM.getIOrderStatus() > 4) {
							return warn("当前订单状态不支持审批操作！");
						}
						//订单状态：3. 已审批
						orderM.setIOrderStatus(3);
						//审核状态：2. 审核通过
						orderM.setIAuditStatus(2);
						 success= orderM.update();
						if(success) {
							//添加日志
							addUpdateSystemLog(orderM.getIAutoId(), JBoltUserKit.getUserId(), orderM.getIAutoId().toString());
						}
					}
				}
				return ret(success);
//			}
		}
		success = weekOrderM.update();
		if(success) {
			//添加日志
			addUpdateSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
		}
		return ret(success);
	}
}