package cn.rjtech.admin.cusordersum;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.annualorderd.AnnualOrderDService;
import cn.rjtech.admin.annualorderdqty.AnnualorderdQtyService;
import cn.rjtech.admin.annualorderm.AnnualOrderMService;
import cn.rjtech.admin.customerworkdays.CustomerWorkDaysService;
import cn.rjtech.admin.monthorderd.MonthorderdService;
import cn.rjtech.model.momdata.*;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.DbTemplate;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import com.jfinal.plugin.activerecord.Record;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 客户订单-客户计划汇总
 * @ClassName: CusOrderSumService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-14 16:20
 */
public class CusOrderSumService extends BaseService<CusOrderSum> {
	private final CusOrderSum dao=new CusOrderSum().dao();
	@Override
	protected CusOrderSum dao() {
		return dao;
	}

	@Inject
	private AnnualOrderMService annualOrderMService;
	@Inject
	private AnnualOrderDService annualOrderDService;
	@Inject
	private AnnualorderdQtyService annualorderdQtyService;

	//月度计划Service
	@Inject
	private MonthorderdService monthorderdService;
	@Inject
	private CustomerWorkDaysService workDaysService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param iType 类型：1. 客户计划 2. 客户订单 3. 计划使用
	 * @return
	 */
	public Page<CusOrderSum> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        //关键词模糊查询
        sql.likeMulti(keywords,"iInventoryId", "dCreateTime");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param cusOrderSum
	 * @return
	 */
	public Ret save(CusOrderSum cusOrderSum) {
		if(cusOrderSum==null || isOk(cusOrderSum.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(cusOrderSum.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=cusOrderSum.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(), cusOrderSum.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param cusOrderSum
	 * @return
	 */
	public Ret update(CusOrderSum cusOrderSum) {
		if(cusOrderSum==null || notOk(cusOrderSum.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		CusOrderSum dbCusOrderSum=findById(cusOrderSum.getIAutoId());
		if(dbCusOrderSum==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(cusOrderSum.getName(), cusOrderSum.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=cusOrderSum.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(), cusOrderSum.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param cusOrderSum 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(CusOrderSum cusOrderSum, Kv kv) {
		//addDeleteSystemLog(cusOrderSum.getIAutoId(), JBoltUserKit.getUserId(),cusOrderSum.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param cusOrderSum model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(CusOrderSum cusOrderSum, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * 审批
	 * @param annualOrderM
	 * @return
	 */
	public Ret approve(AnnualOrderM annualOrderM) {
		CusOrderSum cusOrderSum = new CusOrderSum();
		//创建信息
		cusOrderSum.setICreateBy(JBoltUserKit.getUserId());
		cusOrderSum.setCCreateName(JBoltUserKit.getUserName());
		cusOrderSum.setDCreateTime(new Date());

		//更新信息
		cusOrderSum.setIUpdateBy(JBoltUserKit.getUserId());
		cusOrderSum.setCUpdateName(JBoltUserKit.getUserName());
		cusOrderSum.setDUpdateTime(new Date());

		//组织信息
		cusOrderSum.setCOrgCode(getOrgCode());
		cusOrderSum.setCOrgName(getOrgName());
		cusOrderSum.setIOrgId(getOrgId());

		List<AnnualOrderD> iAnnualOrderDList = annualOrderDService.findByMid(annualOrderM.getIAutoId());
		//年
		cusOrderSum.setIYear(annualOrderM.getIYear());

		//存货编码集合
		Long[] inventoryIds = iAnnualOrderDList.stream().map(AnnualOrderD::getIInventoryId).toArray(Long[]::new);
		//查询参数
		Okv para = Okv.by("inventoryIdList", ArrayUtil.join(inventoryIds, COMMA)).set("iYear", annualOrderM.getIYear());

		//遍历年度计划
		for (AnnualOrderD annualOrderD : iAnnualOrderDList) {
			//存货ID
			cusOrderSum.setIInventoryId(annualOrderD.getIInventoryId());
			//月度计划
			List<Record> records = dbTemplate("monthorderd.cusOrderSumIncCusPlan", para).find();
			//年度计划订单年月金额
			List<AnnualorderdQty> iAnnualOrderDtyList = annualorderdQtyService.findAnnualorderdQty(annualOrderD.getIAutoId(),annualOrderM);
			if (records.size() > 0) {
				for (Record record : records) {
					//验证月度计划是否存在此存货编码  如有 优先按月度计划存储
						for (AnnualorderdQty annualorderdQty : iAnnualOrderDtyList) {
							//月
							cusOrderSum.setIMonth(annualorderdQty.getIMonth());
							if (annualorderdQty.getIMonth().equals(record.get("iMonth"))) {
								/*List<CusOrderSum> cusOrderSums = find(selectSql().eq("iYear", record.get("iYear")).
										eq("iMonth", record.get("iMonth")).
										in("iInventoryId", ArrayUtil.join(inventoryIds, COMMA)));
								saveCusOrderSum(record);
								for (int i = 1; i <= 31; i++) {
									cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
									cusOrderSum.setIDate(i);
									cusOrderSum.setIQty1(record.get("iQty" + i));
									cusOrderSum.save();
								}*/
							} else {
								CustomerWorkDays customerWorkDays = workDaysService.findByICustomerId(annualOrderM.getICustomerId());
								Integer workDay = 31;
									if (null != customerWorkDays) {
										workDay  = customerWorkDays.get("iMonth"+annualorderdQty.getIMonth()+"Days");
									}
								BigDecimal divide = annualorderdQty.getIQty().divide(new BigDecimal(workDay), BigDecimal.ROUND_HALF_UP);
								for (int i = 1; i <= 31; i++) {
									cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
									cusOrderSum.setIDate(i);
									cusOrderSum.setIQty1(divide);
									cusOrderSum.save();
								}
							}

						}

				}
			} else {
			//若没有月度计划  按年度计划计算每日计划值
			for (AnnualorderdQty annualorderdQty : iAnnualOrderDtyList) {
				CustomerWorkDays customerWorkDays = workDaysService.findByICustomerId(annualOrderM.getICustomerId());
				if (null == customerWorkDays) {
					//3. 审核不通过
					annualOrderM.setIAuditStatus(3);
					//  2. 待审核
					annualOrderM.setIOrderStatus(2);
					annualOrderM.update();
					return fail("请配置客户档案工作天数！");
				}
				//月
				cusOrderSum.setIMonth(annualorderdQty.getIMonth());
				Integer workDay  = customerWorkDays.get("iMonth"+annualorderdQty.getIMonth()+"Days");
				BigDecimal divide = annualorderdQty.getIQty().divide(new BigDecimal(workDay), BigDecimal.ROUND_HALF_UP);
				for (int i = 1; i <= 31; i++) {
					cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
					cusOrderSum.setIDate(i);
					cusOrderSum.setIQty1(divide);
					cusOrderSum.save();
				}
			}
		}

		}
		return SUCCESS;
	}

	private void saveCusOrderSum(Record record) {

	}

	public Ret approveByMonth(Monthorderm monthorderm) {
		List<Monthorderd> monthorderds = monthorderdService.findByMid(monthorderm.getIAutoId());
		Long[] inventoryIds = monthorderds.stream().map(Monthorderd::getIInventoryId).toArray(Long[]::new);

		CusOrderSum cusOrderSum = new CusOrderSum();
		//创建信息
		cusOrderSum.setICreateBy(JBoltUserKit.getUserId());
		cusOrderSum.setCCreateName(JBoltUserKit.getUserName());
		cusOrderSum.setDCreateTime(new Date());

		//更新信息
		cusOrderSum.setIUpdateBy(JBoltUserKit.getUserId());
		cusOrderSum.setCUpdateName(JBoltUserKit.getUserName());
		cusOrderSum.setDUpdateTime(new Date());

		//组织信息
		cusOrderSum.setCOrgCode(getOrgCode());
		cusOrderSum.setCOrgName(getOrgName());
		cusOrderSum.setIOrgId(getOrgId());

		cusOrderSum.setIYear(monthorderm.getIYear());
		cusOrderSum.setIMonth(monthorderm.getIMonth());

		List<CusOrderSum> cusOrderSums = find(selectSql().eq("iYear", monthorderm.getIYear()).
				eq("iMonth", monthorderm.getIMonth()).
				in("iInventoryId", ArrayUtil.join(inventoryIds, COMMA)));
		for (Monthorderd monthorderd : monthorderds) {
			if (cusOrderSums.size() > 0) {
				for (CusOrderSum orderSum : cusOrderSums) {
					if (orderSum.getIInventoryId().equals(monthorderd.getIInventoryId())) {
						orderSum.setIQty1(monthorderd.get("iQty" + orderSum.getIDate()));
						orderSum.setIUpdateBy(JBoltUserKit.getUserId());
						orderSum.setCUpdateName(JBoltUserKit.getUserName());
						orderSum.setDUpdateTime(new Date());
						orderSum.update();
					} else {
						cusOrderSum.setIInventoryId(monthorderd.getIInventoryId());
						for (int i = 1; i <= 31; i++) {
							cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
							cusOrderSum.setIDate(i);
							cusOrderSum.setIQty1(monthorderd.get("iQty" + i));
							cusOrderSum.save();
						}
					}
				}
			} else {
				cusOrderSum.setIInventoryId(monthorderd.getIInventoryId());
				for (int i = 1; i <= 31; i++) {
					cusOrderSum.setIAutoId(JBoltSnowflakeKit.me.nextId());
					cusOrderSum.setIDate(i);
					cusOrderSum.setIQty1(monthorderd.get("iQty" + i));
					cusOrderSum.save();
				}
			}

		}
		return SUCCESS;
	}

	public Object findCusOrderSum(Integer pageNumber, Integer pageSize,  Kv kv) {
		JSONObject jsonObject = new JSONObject();
		Page<Record> pageData = dbTemplate("cusordersum.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
//		List<Record> records = dbTemplate("cusordersum.getYearMouth", kv).find();
//		kv.set("iMonth", 1);
//		kv.set("iYear", 2023);
//		List<Record> record3 = dbTemplate("cusordersum.getIDate", kv).find();
		String str = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31";

		kv.set("roleArray", str);
		if (pageData.getTotalRow() > 0) {
			for (Record record : pageData.getList()) {
				String iinventoryid = record.getStr("iinventoryid");
				kv.set("iInventoryId", iinventoryid);
				List<Record> record2 = dbTemplate("cusordersum.getiQty1", kv).find();
				record.set("record2", record2);
				System.out.println("数据iinventoryid："+iinventoryid);
			}
		}
		/*if (pageData.getTotalRow() > 0) {
			pageData.getList().forEach(data -> {
		//	data.set("records", records);
				data.set("record2", record2);
			});
		}*/
		return pageData;
	}
}