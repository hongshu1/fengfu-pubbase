package cn.rjtech.admin.weekorderm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.cusordersum.CusOrderSumService;
import cn.rjtech.admin.customer.CustomerService;
import cn.rjtech.admin.weekorderd.WeekOrderDService;
import cn.rjtech.model.momdata.WeekOrderD;
import cn.rjtech.model.momdata.WeekOrderM;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

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

    @Inject
    private CusOrderSumService cusOrderSumService;

    @Inject
    private WeekOrderDService weekOrderDService;

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
	 *
	 * @param jBoltTable 前端保存数据
	 * @param
	 * @return
	 */
	public Ret save(JBoltTable jBoltTable) {
		//参数判空
		if (jBoltTable == null || jBoltTable.isBlank()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}

        WeekOrderM weekOrderM = jBoltTable.getFormModel(WeekOrderM.class, "weekOrderM");
        String approve = jBoltTable.getForm().getString("approve");
        if (weekOrderM == null || isOk(weekOrderM.getIAutoId())) {
            return fail(JBoltMsg.PARAM_ERROR);
        }
        
        //主表保存
        saveContant(weekOrderM, approve);

		if (null!=jBoltTable.getSave()) {
			return updateWeekOrderDs("save", jBoltTable, weekOrderM);
		}
		return SUCCESS;
	}

	/**
	 * 主表保存
	 * @param weekOrderM
	 */
	private void saveContant(WeekOrderM weekOrderM,String approve) {
		//主表新增页审核
		if (approve.equals("approve")) {
			//审核状态：1. 待审核
			weekOrderM.setIAuditStatus(1);
			//订单状态：2. 待审批
			weekOrderM.setIOrderStatus(2);
		} else {
			//保存
			//审核状态：0. 未审核 1. 待审核 2. 审核通过 3. 审核不通过
			weekOrderM.setIAuditStatus(0);
			//订单状态：1. 已保存 2. 待审批 3. 已审批 4. 审批不通过 5. 已发货 6. 已核对 7. 已关闭
			weekOrderM.setIOrderStatus(1);
		}
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
		//订单创建日期
		weekOrderM.setDOrderDate(new Date());
		boolean success=weekOrderM.save();
//		if(success) {
//			//添加日志
//			addSaveSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
//		}
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
		List<WeekOrderD> weekOrderDs;
		switch (mark) {
			// 修改
			case "edit":
				weekOrderDs = jBoltTable.getUpdateModelList(WeekOrderD.class);
                
                weekOrderDService.batchUpdate(weekOrderDs);
				break;
			// 保存
			case "save":
				weekOrderDs = jBoltTable.getSaveModelList(WeekOrderD.class);
				for (WeekOrderD weekOrderD : weekOrderDs) {
					// 主表id
					weekOrderD.setIWeekOrderMid(weekOrderM.getIAutoId());
					weekOrderD.setIsDeleted(false);
				}

                weekOrderDService.batchSave(weekOrderDs);
            default:
                break;
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

	public List<Record> weekOrderMData(Long iWeekOrderMid) {
		return dbTemplate("weekorderm.weekOrderMData", Okv.by("iWeekOrderMid", iWeekOrderMid)).find();
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
		boolean success = false;
		if (mark == 1) {
            WeekOrderM weekOrderM = findById(iAutoId);
			if (weekOrderM == null ||notOk(weekOrderM.getIAutoId())) {
				return fail(JBoltMsg.PARAM_ERROR);
			}
			//订单状态：2. 待审批
			weekOrderM.setIOrderStatus(2);
			//审核状态： 1. 待审核
			weekOrderM.setIAuditStatus(1);
            success = weekOrderM.update();
            if (success) {
                //添加日志
                addUpdateSystemLog(weekOrderM.getIAutoId(), JBoltUserKit.getUserId(), weekOrderM.getIAutoId().toString());
            }
		} else {
				List<WeekOrderM> listByIds = getListByIds(iAutoId);
				//TODO 由于审批流程暂未开发 先审批提审即通过
				if (listByIds.size() > 0) {
					for (WeekOrderM orderM : listByIds) {
						if (orderM.getIOrderStatus() != 2) {
							return warn("订单："+orderM.getCOrderNo()+"状态不支持审批操作！");
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
		}

        List<WeekOrderD> weekOrderDS = weekOrderDService.findByMId(iAutoId);
        if (weekOrderDS.size() > 0) cusOrderSumService.handelWeekOrder(weekOrderDS);


		return ret(success);
	}

	/**
	 * 撤回
	 * @param iAutoId
	 * @return
	 */
	public Ret recall(String iAutoId) {
		if( notOk(iAutoId)) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		WeekOrderM weekOrderM = findById(iAutoId);
		//订单状态：2. 待审批
		weekOrderM.setIOrderStatus(1);
		//审核状态： 1. 待审核
		weekOrderM.setIAuditStatus(0);
		boolean result = weekOrderM.update();
		return ret(result);
	}

	/**
	 * 关闭功能
	 * @param iAutoId
	 * @return
	 */
	public Ret closeWeekOrder(String iAutoId) {
		if( notOk(iAutoId)) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		WeekOrderM weekOrderM = findById(iAutoId);
		//订单状态：7. 已关闭
		weekOrderM.setIOrderStatus(7);
		boolean result = weekOrderM.update();
		return ret(result);

	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public Ret deleteByIdS(String ids) {
		List<WeekOrderM> listByIds = getListByIds(ids);
		for (WeekOrderM weekOrderM : listByIds) {
			//订单状态：1. 已保存
			if (weekOrderM.getIOrderStatus() != 1) {
				return warn("订单："+weekOrderM.getCOrderNo()+"状态不支持删除操作！");
			}
			weekOrderM.setIsDeleted(true);
			weekOrderM.update();
		}
		return SUCCESS;
	}

	/**
	 * 批量反审批
	 * @param ids
	 * @return
	 */
	public Ret NoApprove(String ids) {
		//TODO数据同步暂未开发 现只修改状态
		for (WeekOrderM weekOrderM :  getListByIds(ids)) {
			//订单状态： 3. 已审批
			if (weekOrderM.getIOrderStatus() != 3) {
				return warn("订单："+weekOrderM.getCOrderNo()+"状态不支持反审批操作！");
			}
			//审核状态：1. 待审核
			weekOrderM.setIAuditStatus(1);
			//订单状态： 2. 待审批
			weekOrderM.setIOrderStatus(2);
			weekOrderM.update();
		}
		return SUCCESS;
	}
}