package cn.rjtech.admin.rcvdocdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.rcvdocqcformm.RcvDocQcFormMService;
import cn.rjtech.admin.syspuinstore.SysPuinstoredetailService;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.RcvDocQcFormM;
import cn.rjtech.model.momdata.SysPuinstore;
import cn.rjtech.model.momdata.SysPuinstoredetail;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

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

	@Inject
	private SysPuinstoredetailService syspuinstoredetailservice; ////采购入库从表

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
					if (rcvDocDefect.getIStatus() == 2){
						//录入数据
						rcvDocDefect.setCApproach(formRecord.getStr("capproach"));
						rcvDocDefect.setIStatus(3);
						//更新人和时间
						rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
						rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
						rcvDocDefect.setDUpdateTime(now);
						rcvDocDefect.update();
						String cApproach = rcvDocDefect.getCApproach();
						System.out.println("==="+cApproach.equals("1"));
						System.out.println("==="+cApproach=="1");
						rcvDocDefect.setIQcUserId(JBoltUserKit.getUserId());        //检验用户ID
						rcvDocDefect.setDQcTime(now); 								//检验时间
						if (cApproach.equals("1")){
							RcvDocQcFormM docQcFormM=rcvDocQcFormMService.findById(rcvDocDefect.getIRcvDocQcFormMid());

							SysPuinstore sysPuinstore = new SysPuinstore();
							List<SysPuinstoredetail> sysPuinstoredetails = new ArrayList<>();
							rcvDocQcFormMService.saveSysPuinstoreModel(sysPuinstore, docQcFormM, sysPuinstoredetails);
							//保存采购入库单主表数据
							ValidationUtils.isTrue(sysPuinstore.save(), "创建采购入库单据失败");
							//保存采购入库单从表数据
							syspuinstoredetailservice.batchSave(sysPuinstoredetails);
						}
					}else {
						//待记录状态
						RcvDocfectLinesave(formRecord, now);
					}
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


		//录入填写的数据
		if (formRecord.getStr("param")!=null){
			rcvDocDefect.setIStatus(1);
		}else {
			rcvDocDefect.setIStatus(2);
		}
		//更新人和时间
		rcvDocDefect.setIUpdateBy(JBoltUserKit.getUserId());
		rcvDocDefect.setCUpdateName(JBoltUserKit.getUserName());
		rcvDocDefect.setDUpdateTime(now);
		rcvDocDefect.setIQcUserId(JBoltUserKit.getUserId());        //检验用户ID
		rcvDocDefect.setDQcTime(now);        						//检验时间
		rcvDocDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		rcvDocDefect.setIRespType(formRecord.getInt("iresptype"));
		rcvDocDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
		rcvDocDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
		rcvDocDefect.setCDesc(formRecord.getStr("cdesc"));
		rcvDocDefect.update();
	}

	public void saveRcvDocDefectModel(RcvDocDefect rcvDocDefect, RcvDocQcFormM docQcFormM) {
		//rcvDocDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		Date date = new Date();
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		rcvDocDefect.setCDocNo(JBoltSnowflakeKit.me.nextIdStr());     //异常品单号
		rcvDocDefect.setIRcvDocQcFormMid(docQcFormM.getIAutoId());   //来料检id
		rcvDocDefect.setIInventoryId(docQcFormM.getIInventoryId());  //存货ID
		rcvDocDefect.setIVendorId(docQcFormM.getIVendorId());        //供应商id
		rcvDocDefect.setIStatus(1);                                  //状态：1. 待记录 2. 待判定 3. 已完成
		rcvDocDefect.setIDqQty(new BigDecimal(docQcFormM.getIQty()));//不合格数量
		rcvDocDefect.setCDesc(docQcFormM.getCMemo());                //不良内容描述
		rcvDocDefect.setICreateBy(userId);
		rcvDocDefect.setDCreateTime(date);
		rcvDocDefect.setCCreateName(userName);
		rcvDocDefect.setIOrgId(getOrgId());
		rcvDocDefect.setCOrgCode(getOrgCode());
		rcvDocDefect.setCOrgName(getOrgName());
		rcvDocDefect.setCUpdateName(userName);
		rcvDocDefect.setDUpdateTime(date);
		rcvDocDefect.setIUpdateBy(userId);
		rcvDocDefect.setIQcUserId(JBoltUserKit.getUserId());        //检验用户ID
		rcvDocDefect.setDQcTime(date);        						//检验时间
//		rcvDocDefect.setCBadnessSns("不良项目");
	}

	/*
	 * 根据来料检id查询异常品质单
	 */
	public RcvDocDefect findStockoutDefectByiRcvDocQcFormMid(Object iRcvDocQcFormMid) {
		return findFirst("SELECT * FROM PL_RcvDocDefect WHERE iRcvDocQcFormMid = ?", iRcvDocQcFormMid);
	}


	public Record getrcvDocQcFormList(Long iautoid){
		return dbTemplate("rcvdocdefect.getrcvDocQcFormList", Kv.by("iautoid",iautoid)).findFirst();
	}

	//API来料异常品主页查询
	public Page<Record> getPageListApi(Integer pageNumber, Integer pageSize, Kv kv) {
		return dbTemplate("rcvdocdefect.paginateAdminDatasapi",kv).paginate(pageNumber,pageSize);
	}


	//API来料异常品查看与编辑
	public Map<String, Object> getRcvDocDefectListApi(Long iautoid, Long ircvdocqcformmid,String type){

		Map<String, Object> map = new HashMap<>();

		RcvDocDefect rcvDocDefect=findById(iautoid);
		Record rcvDocQcFormM = getrcvDocQcFormList(ircvdocqcformmid);
		map.put("rcvDocDefect",rcvDocDefect);
		map.put("rcvDocQcFormM",rcvDocQcFormM);
		map.put("type",type);
		if (rcvDocDefect == null){
			return map;
		}
		if (rcvDocDefect.getIStatus() == 2) {
			map.put("isfirsttime", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
		} else if (rcvDocDefect.getIStatus() == 3) {
			int getCApproach = Integer.parseInt(rcvDocDefect.getCApproach());
			map.put("capproach", (getCApproach == 1) ? "特采" : "拒收");
			map.put("isfirsttime", (rcvDocDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (rcvDocDefect.getIRespType() == 1) ? "供应商" : "其他");
		}
		return map;
	}


	/**
	 * 打印数据
	 * @param kv 参数
	 * @return
	 */
	public Object getQRCodeCheck(Kv kv) {
		return dbTemplate("rcvdocdefect.containerPrintData",kv).find();
	}


}