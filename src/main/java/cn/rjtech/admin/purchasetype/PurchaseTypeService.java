package cn.rjtech.admin.purchasetype;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.PurchaseType;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购类型 Service
 * @ClassName: PurchaseTypeService
 * @author: WYX
 * @date: 2023-04-03 10:52
 */
public class PurchaseTypeService extends BaseService<PurchaseType> {

	private final PurchaseType dao = new PurchaseType().dao();

	@Override
	protected PurchaseType dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<PurchaseType> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}
	
	public List<Record> selectAll (Kv kv){
		return dbTemplate("purchasetype.selectAll",kv).find();
	}
	
	public Page<Record> selectAll (int pageNumber, int pageSize,Kv kv){
		List<Record> list = dbTemplate("purchasetype.selectAll", kv).find();

		long totalRow;
		totalRow=list.size();
		int totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		List<Record> recordArrayList = new ArrayList<>();
		int pageStart=pageNumber==1?0:(pageNumber-1)*pageSize;//截取的开始位置
		int pageEnd= Math.min((int) totalRow, pageNumber * pageSize);
		if(totalRow>pageStart){
			recordArrayList =list.subList(pageStart, pageEnd);
		}

		return  new Page<>(recordArrayList, pageNumber, pageSize, totalPage, (int) totalRow);
	}

	/**
	 * 保存
	 * @param purchaseType
	 * @return
	 */
	public Ret save(PurchaseType purchaseType) {
		if(purchaseType==null || isOk(purchaseType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		ValidationUtils.assertNull(findCPTCode(purchaseType.getCPTCode()), "采购类型编码重复");

		setPurchaseTypeClass(purchaseType);
		//if(existsName(purchaseType.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseType.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(purchaseType.getIautoid(), JBoltUserKit.getUserId(), purchaseType.getName());
		}
		return ret(success);
	}

	/**
	 * 查找采购类型编码
	 * @param cPTCode
	 * @return
	 */
	public PurchaseType findCPTCode(String cPTCode){
		return findFirst(Okv.by("cPTCode",cPTCode).set("isDeleted",false),"iautoid","asc");
	}

	/**
	 * 设置参数
	 * @param purchaseType
	 * @return
	 */
	private PurchaseType setPurchaseTypeClass(PurchaseType purchaseType){
		purchaseType.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		purchaseType.setICreateBy(userId);	//创建人ID
		purchaseType.setIUpdateBy(userId);	//更新人ID
		String userName = JBoltUserKit.getUserName();
		purchaseType.setCCreateName(userName);	//创建人名称
		purchaseType.setCUpdateName(userName);	//更新人名称
		Date date = new Date();
		purchaseType.setDCreateTime(date);	//创建时间
		purchaseType.setDUpdateTime(date);	//更新时间
		purchaseType.setISource(SourceEnum.MES.getValue());
		List<PurchaseType> cptcode = find("select cRdCode from Bd_Rd_Style where iAutoId = ?",purchaseType.getIRdStyleId());
		purchaseType.setCRdCode(cptcode.get(0).getCRdCode());	//0：下标，，第一个
		return purchaseType;
	}


	/**
	 * 更新
	 * @param purchaseType
	 * @return
	 */
	public Ret update(PurchaseType purchaseType) {
		if(purchaseType==null || notOk(purchaseType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		PurchaseType dbPurchaseType=findById(purchaseType.getIAutoId());
		if(dbPurchaseType==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(purchaseType.getName(), purchaseType.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=purchaseType.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(purchaseType.getIautoid(), JBoltUserKit.getUserId(), purchaseType.getName());
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
	 * @param purchaseType 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(PurchaseType purchaseType, Kv kv) {
		//addDeleteSystemLog(purchaseType.getIautoid(), JBoltUserKit.getUserId(),purchaseType.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseType 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(PurchaseType purchaseType, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(purchaseType, kv);
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
	 * 切换bpfdefault属性
	 */
	public Ret toggleBPFDefault(Long id) {
		return toggleBoolean(id, "bPFDefault");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 切换bptmpsmrp属性
	 */
	public Ret toggleBptmpsMrp(Long id) {
		return toggleBoolean(id, "bPTMPS_MRP");
	}

	/**
	 * 切换bdefault属性
	 */
	public Ret toggleBDefault(Long id) {
		return toggleBoolean(id, "bDefault");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param purchaseType 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(PurchaseType purchaseType,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(PurchaseType purchaseType, String column, Kv kv) {
		//addUpdateSystemLog(purchaseType.getIautoid(), JBoltUserKit.getUserId(), purchaseType.getName(),"的字段["+column+"]值:"+purchaseType.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param purchaseType model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(PurchaseType purchaseType, Kv kv) {
		//这里用来覆盖 检测PurchaseType是否被其它表引用
		return null;
	}
	



}
