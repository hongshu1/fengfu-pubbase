package cn.rjtech.admin.rcvdocdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 来料异常品记录 Service
 * @ClassName: RcvDocDefectService
 * @author: RJ
 * @date: 2023-04-18 16:36
 */
public class RcvDocDefectService extends BaseService<RcvDocDefect> {

	private final RcvDocDefect dao = new RcvDocDefect().dao();

	@Inject
	private RcvDocQcFormMService rcvDocQcFormMService;      ////质量管理-来料表

	@Override
	protected RcvDocDefect dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Kv kv) {
		return dbTemplate("rcvdocdefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 保存
	 * @param rcvDocDefect
	 * @return
	 */
	public Ret save(RcvDocDefect rcvDocDefect) {
		if(rcvDocDefect==null || isOk(rcvDocDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(rcvDocDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvDocDefect.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param rcvDocDefect
	 * @return
	 */
	public Ret update(RcvDocDefect rcvDocDefect) {
		if(rcvDocDefect==null || notOk(rcvDocDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		RcvDocDefect dbRcvDocDefect=findById(rcvDocDefect.getIAutoId());
		if(dbRcvDocDefect==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(rcvDocDefect.getName(), rcvDocDefect.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rcvDocDefect.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param rcvDocDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(RcvDocDefect rcvDocDefect, Kv kv) {
		//addDeleteSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(),rcvDocDefect.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvDocDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(RcvDocDefect rcvDocDefect, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(rcvDocDefect, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isfirsttime属性
	 */
	public Ret toggleIsFirstTime(Long id) {
		return toggleBoolean(id, "isFirstTime");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param rcvDocDefect 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(RcvDocDefect rcvDocDefect,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(RcvDocDefect rcvDocDefect, String column, Kv kv) {
		//addUpdateSystemLog(rcvDocDefect.getIautoid(), JBoltUserKit.getUserId(), rcvDocDefect.getName(),"的字段["+column+"]值:"+rcvDocDefect.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rcvDocDefect model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(RcvDocDefect rcvDocDefect, Kv kv) {
		//这里用来覆盖 检测RcvDocDefect是否被其它表引用
		return null;
	}


	/**
	 * 更新状态并保存数据方法
	 * @param formRecord 参数
	 * @return
	 */
	public Ret updateEditTable(Kv formRecord) {
		Date now = new Date();
		tx(() -> {
				//判断是否有主键id
				if(isOk(formRecord.getStr("iautoid"))){
					RcvDocDefect rcvDocDefect = findById(formRecord.getLong("iautoid"));
					if (rcvDocDefect.getIStatus() == 1){
						//录入数据
						rcvDocDefect.setCApproach(formRecord.getStr("capproach"));
						rcvDocDefect.setIStatus(2);
						//更新人和时间
						rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
						rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
						rcvDocDefect.setDUpdateTime(now);
					}
					rcvDocDefect.update();
				}else{
					//保存未有主键的数据
					RcvDocfectLinesave(formRecord, now);
				}
				return true;
			});
		return SUCCESS;
	}


	/**
	 * 未有主键的保存方法
	 * @param formRecord 参数
	 * @param now 时间
	 * @return
	 */
	public void RcvDocfectLinesave(Kv formRecord, Date now){
		System.out.println("formRecord==="+formRecord);
		System.out.println("now==="+now);
		RcvDocDefect rcvDocDefect = new RcvDocDefect();
		rcvDocDefect.setIAutoId(formRecord.getLong("iautoid"));

		//质量管理-来料检明细
		RcvDocQcFormM rcvDocQcFormM = rcvDocQcFormMService.findById(formRecord.getLong("ircvdocqcformmid"));
		rcvDocDefect.setIRcvDocQcFormMid(rcvDocQcFormM.getIAutoId());
		rcvDocDefect.setIVendorId(rcvDocQcFormM.getIInventoryId());
		rcvDocDefect.setIInventoryId(rcvDocQcFormM.getIVendorId());
		rcvDocDefect.setIQcUserId(rcvDocQcFormM.getIUpdateBy());
		rcvDocDefect.setDQcTime(rcvDocQcFormM.getDUpdateTime());

		//录入填写的数据
		rcvDocDefect.setIStatus(1);
		rcvDocDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		rcvDocDefect.setIRespType(formRecord.getInt("iresptype"));
		rcvDocDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
		rcvDocDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
		rcvDocDefect.setCDesc(formRecord.getStr("cdesc"));

		//必录入基本数据
		rcvDocDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
		rcvDocDefect.setCDocNo(billNo);
		rcvDocDefect.setIOrgId(getOrgId());
		rcvDocDefect.setCOrgCode(getOrgCode());
		rcvDocDefect.setCOrgName(getOrgName());
		rcvDocDefect.setICreateBy(JBoltUserKit.getUserId());
		rcvDocDefect.setCCreateName(JBoltUserKit.getUserName());
		rcvDocDefect.setDCreateTime(now);
		rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
		rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
		rcvDocDefect.setDUpdateTime(now);
		rcvDocDefect.save();
	}

	public void saveRcvDocDefectModel(RcvDocDefect rcvDocDefect, RcvDocQcFormM docQcFormM) {
		rcvDocDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		Date date = new Date();
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		rcvDocDefect.setCDocNo(docQcFormM.getCRcvDocQcFormNo());     //异常品单号
		rcvDocDefect.setIRcvDocQcFormMid(docQcFormM.getIAutoId());   //出库检id
		rcvDocDefect.setIInventoryId(docQcFormM.getIInventoryId());  //存货ID
		rcvDocDefect.setIVendorId(docQcFormM.getIVendorId());        //供应商id
		rcvDocDefect.setIStatus(1);                                  //状态：1. 待记录 2. 待判定 3. 已完成
		rcvDocDefect.setIDqQty(new BigDecimal(0));                   //不合格数量
		rcvDocDefect.setCDesc(docQcFormM.getCMemo());                //不良内容描述
		rcvDocDefect.setIQcUserId(docQcFormM.getIQcUserId());        //检验用户ID
		rcvDocDefect.setDQcTime(docQcFormM.getDUpdateTime());        //检验时间

		rcvDocDefect.setICreateBy(userId);
		rcvDocDefect.setDCreateTime(date);
		rcvDocDefect.setCCreateName(userName);
		rcvDocDefect.setIOrgId(getOrgId());
		rcvDocDefect.setCOrgCode(getOrgCode());
		rcvDocDefect.setCOrgName(getOrgName());
		rcvDocDefect.setIUpdateBy(userId);
		rcvDocDefect.setCUpdateName(userName);
		rcvDocDefect.setDUpdateTime(date);
	}

	/*
	 * 根据来料检id查询异常品质单
	 */
	public RcvDocDefect findStockoutDefectByiRcvDocQcFormMid(Object iRcvDocQcFormMid) {
		return findFirst("SELECT * FROM PL_RcvDocDefect WHERE iRcvDocQcFormMid = ?", iRcvDocQcFormMid);
	}


}