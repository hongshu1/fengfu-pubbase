package cn.rjtech.admin.stockoutdefect;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.instockqcformm.InStockQcFormMService;
import cn.rjtech.admin.stockoutqcformm.StockoutQcFormMService;
import cn.rjtech.model.momdata.InStockQcFormM;
import cn.rjtech.model.momdata.RcvDocDefect;
import cn.rjtech.model.momdata.StockoutDefect;
import cn.rjtech.model.momdata.StockoutQcFormM;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 出库异常记录 Service
 * @ClassName: StockoutDefectService
 * @author: RJ
 * @date: 2023-04-25 09:26
 */
public class StockoutDefectService extends BaseService<StockoutDefect> {

	private final StockoutDefect dao = new StockoutDefect().dao();

	@Override
	protected StockoutDefect dao() {
		return dao;
	}
	@Inject
	private StockoutQcFormMService stockoutQcFormMService;      ////质量管理-在库检

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageSize, int pageNumber, Okv kv) {
		return dbTemplate(dao()._getDataSourceConfigName(), "stockoutdefect.paginateAdminDatas", kv).paginate(pageNumber, pageSize);
	}


	/**
	 * 保存
	 * @param stockoutDefect
	 * @return
	 */
	public Ret save(StockoutDefect stockoutDefect) {
		if(stockoutDefect==null || isOk(stockoutDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockoutDefect.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutDefect.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockoutDefect.getIautoid(), JBoltUserKit.getUserId(), stockoutDefect.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockoutDefect
	 * @return
	 */
	public Ret update(StockoutDefect stockoutDefect) {
		if(stockoutDefect==null || notOk(stockoutDefect.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockoutDefect dbStockoutDefect=findById(stockoutDefect.getIAutoId());
		if(dbStockoutDefect==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockoutDefect.getName(), stockoutDefect.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockoutDefect.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockoutDefect.getIautoid(), JBoltUserKit.getUserId(), stockoutDefect.getName());
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
	 * @param stockoutDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockoutDefect stockoutDefect, Kv kv) {
		//addDeleteSystemLog(stockoutDefect.getIautoid(), JBoltUserKit.getUserId(),stockoutDefect.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutDefect 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(StockoutDefect stockoutDefect, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(stockoutDefect, kv);
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
	 * @param stockoutDefect 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(StockoutDefect stockoutDefect,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(StockoutDefect stockoutDefect, String column, Kv kv) {
		//addUpdateSystemLog(stockoutDefect.getIautoid(), JBoltUserKit.getUserId(), stockoutDefect.getName(),"的字段["+column+"]值:"+stockoutDefect.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockoutDefect model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockoutDefect stockoutDefect, Kv kv) {
		//这里用来覆盖 检测StockoutDefect是否被其它表引用
		return null;
	}



	//更新状态并保存数据方法
	public Ret updateEditTable(Kv formRecord) {
		Date now = new Date();

		tx(() -> {
			//判断是否有主键id
			if(isOk(formRecord.getStr("iautoid"))){
				StockoutDefect stockoutDefect = findById(formRecord.getLong("iautoid"));
				if (stockoutDefect.getIStatus() == 1){
					//录入数据
					stockoutDefect.setCApproach(formRecord.getStr("capproach"));
					stockoutDefect.setIStatus(2);
					//更新人和时间
					stockoutDefect.setIUpdateBy(JBoltUserKit.getUserId());
					stockoutDefect.setCUpdateName(JBoltUserKit.getUserName());
					stockoutDefect.setDUpdateTime(now);

				}
				stockoutDefect.update();
			}else{
				//保存未有主键的数据
				RcvDocfectLinesave(formRecord, now);
			}
			return true;
		});
		return SUCCESS;
	}


	//未有主键的保存方法
	public void RcvDocfectLinesave(Kv formRecord, Date now){
		System.out.println("formRecord==="+formRecord);
		System.out.println("now==="+now);
		StockoutDefect stockoutDefect = new StockoutDefect();
		stockoutDefect.setIAutoId(formRecord.getLong("iautoid"));

		//质量管理-在库检明细
		StockoutQcFormM stockoutQcFormM = stockoutQcFormMService.findById(formRecord.getLong("stockoutqcformmid"));

		stockoutDefect.setIStockoutQcFormMid(stockoutQcFormM.getIAutoId());
		stockoutDefect.setIInventoryId(stockoutQcFormM.getIInventoryId());
		stockoutDefect.setIQcUserId(stockoutQcFormM.getIUpdateBy());
		stockoutDefect.setDQcTime(stockoutQcFormM.getDUpdateTime());

		//录入填写的数据
		stockoutDefect.setIStatus(1);
		stockoutDefect.setIDqQty(formRecord.getBigDecimal("idqqty"));
		stockoutDefect.setIRespType(formRecord.getInt("iresptype"));
		stockoutDefect.setIsFirstTime(formRecord.getBoolean("isfirsttime"));
		stockoutDefect.setCBadnessSns(formRecord.getStr("cbadnesssns"));
		stockoutDefect.setCDesc(formRecord.getStr("cdesc"));

		//必录入基本数据
		stockoutDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "YCP", 5);
		stockoutDefect.setCDocNo(billNo);
		stockoutDefect.setIOrgId(getOrgId());
		stockoutDefect.setCOrgCode(getOrgCode());
		stockoutDefect.setCOrgName(getOrgName());
		stockoutDefect.setICreateBy(JBoltUserKit.getUserId());
		stockoutDefect.setCCreateName(JBoltUserKit.getUserName());
		stockoutDefect.setDCreateTime(now);
		stockoutDefect.setIUpdateBy(JBoltUserKit.getUserId());
		stockoutDefect.setCUpdateName(JBoltUserKit.getUserName());
		stockoutDefect.setDUpdateTime(now);
		stockoutDefect.save();
	}

	public void savestockoutDefectmodel(StockoutDefect stockoutDefect, StockoutQcFormM stockoutQcFormM) {
		stockoutDefect.setIAutoId(JBoltSnowflakeKit.me.nextId());
		Date date = new Date();
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		stockoutDefect.setCDocNo(stockoutQcFormM.getCStockoutQcFormNo());   //异常品单号
		stockoutDefect.setIStockoutQcFormMid(stockoutQcFormM.getIAutoId()); //出库检id
		stockoutDefect.setIInventoryId(stockoutQcFormM.getIInventoryId());  //存货ID
		stockoutDefect.setIStatus(1);                                       //状态：1. 待记录 2. 待判定 3. 已完成
		stockoutDefect.setCDesc(stockoutQcFormM.getCMemo());                //不良内容描述
		stockoutDefect.setIQcUserId(stockoutQcFormM.getIQcUserId());        //检验用户ID
		stockoutDefect.setDQcTime(stockoutQcFormM.getDUpdateTime());        //检验时间

		stockoutDefect.setICreateBy(userId);
		stockoutDefect.setDCreateTime(date);
		stockoutDefect.setCCreateName(userName);
		stockoutDefect.setIOrgId(getOrgId());
		stockoutDefect.setCOrgCode(getOrgCode());
		stockoutDefect.setCOrgName(getOrgName());
		stockoutDefect.setIUpdateBy(userId);
		stockoutDefect.setCUpdateName(userName);
		stockoutDefect.setDUpdateTime(date);
	}

	/*
	 * 根据出货检id查询异常品质单
	 */
	public StockoutDefect findStockoutDefectByiStockoutQcFormMid(Object iStockoutQcFormMid) {
		return findFirst("SELECT * FROM PL_StockoutDefect WHERE iStockoutQcFormMid = ?", iStockoutQcFormMid);
	}

	/**
	 * API在库异常品主页查询
	 */
	public Page<Record> getPageListApi(Integer pageNumber, Integer pageSize, Kv kv) {
		return dbTemplate("stockoutdefect.paginateAdminDatasapi",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 在库检明细
	 */
	public Record getstockoutQcFormMList(Long iautoid){
		return dbTemplate("stockoutdefect.getstockoutQcFormMList", Kv.by("iautoid",iautoid)).findFirst();
	}

	//API在库异常品查看与编辑
	public Map<String, Object> getstockoutDefectListApi(Long iautoid, Long stockoutqcformmid, String type){

		Map<String, Object> map = new HashMap<>();

		StockoutDefect stockoutDefect=findById(iautoid);
		Record stockoutQcFormM = getstockoutQcFormMList(stockoutqcformmid);
		map.put("stockoutDefect",stockoutDefect);
		map.put("stockoutQcFormM",stockoutQcFormM);
		map.put("type",type);
		if (stockoutDefect == null){
			return map;
		}
		if (stockoutDefect.getIStatus() == 1) {
			map.put("isfirsttime", (stockoutDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (stockoutDefect.getIRespType() == 1) ? "供应商" : "其他");
		} else if (stockoutDefect.getIStatus() == 2) {
			int getCApproach = Integer.parseInt(stockoutDefect.getCApproach());
			map.put("capproach", (getCApproach == 1) ? "特采" : "拒收");
			map.put("isfirsttime", (stockoutDefect.getIsFirstTime() == true) ? "首发" : "再发");
			map.put("iresptype", (stockoutDefect.getIRespType() == 1) ? "供应商" : "其他");
		}
		return map;
	}

}