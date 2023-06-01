package cn.rjtech.admin.momoinvbatch;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.sysmaterialsprepare.SysMaterialsprepareService;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.MoDoc;
import cn.rjtech.model.momdata.SysMaterialsprepare;
import cn.rjtech.util.BillNoUtils;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;

import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMoinvbatch;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单现品票 Service
 * @ClassName: MoMoinvbatchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-31 15:35
 */
public class MoMoinvbatchService extends BaseService<MoMoinvbatch> {

	private final MoMoinvbatch dao = new MoMoinvbatch().dao();

	@Override
	protected MoMoinvbatch dao() {
		return dao;
	}

	@Inject
	private MoDocService moDocService;
	@Inject
	private SysMaterialsprepareService sysMaterialsprepareService; //备料单
	@Inject
	private InventoryService inventoryService; //存货档案


	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
	   return  dbTemplate("momoinvbatch.findByImdocId",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param moMoinvbatch
	 * @return
	 */
	public Ret save(MoMoinvbatch moMoinvbatch) {
		if(moMoinvbatch==null || isOk(moMoinvbatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMoinvbatch.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoinvbatch.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMoinvbatch.getIautoid(), JBoltUserKit.getUserId(), moMoinvbatch.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMoinvbatch
	 * @return
	 */
	public Ret update(MoMoinvbatch moMoinvbatch) {
		if(moMoinvbatch==null || notOk(moMoinvbatch.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMoinvbatch dbMoMoinvbatch=findById(moMoinvbatch.getIAutoId());
		if(dbMoMoinvbatch==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMoinvbatch.getName(), moMoinvbatch.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMoinvbatch.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMoinvbatch.getIautoid(), JBoltUserKit.getUserId(), moMoinvbatch.getName());
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
	 * @param moMoinvbatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMoinvbatch moMoinvbatch, Kv kv) {
		//addDeleteSystemLog(moMoinvbatch.getIautoid(), JBoltUserKit.getUserId(),moMoinvbatch.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoinvbatch 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMoinvbatch moMoinvbatch, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMoinvbatch, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param moMoinvbatch 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMoinvbatch moMoinvbatch,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMoinvbatch moMoinvbatch, String column, Kv kv) {
		//addUpdateSystemLog(moMoinvbatch.getIautoid(), JBoltUserKit.getUserId(), moMoinvbatch.getName(),"的字段["+column+"]值:"+moMoinvbatch.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMoinvbatch model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMoinvbatch moMoinvbatch, Kv kv) {
		//这里用来覆盖 检测MoMoinvbatch是否被其它表引用
		return null;
	}
	public Ret createBarcode(Long imodocid) {
		if (notOk(imodocid)) {
			return fail("缺少工单ID");
		}
		MoDoc moDoc = moDocService.findById(imodocid);
		if (moDoc == null) {
			return fail("工单信息不存在");
		}

		List<Record> rows=findRecord("select iAutoId from   Mo_MoInvBatch where iMoDocId="+moDoc.getIAutoId());
		if(!rows.isEmpty()){
			return  fail("已生成现品票");
		}

		//通过工单找备料单号
		SysMaterialsprepare sysMaterialsprepare = sysMaterialsprepareService.findFirst(Okv.create()
				.setIfNotNull(SysMaterialsprepare.SOURCEBILLID, moDoc.getIAutoId()).
				setIfNotNull(SysMaterialsprepare.SOURCEBILLNO, moDoc.getCMoDocNo()), SysMaterialsprepare.AUTOID, "desc");
		if (sysMaterialsprepare == null) {
			return fail("还未生产备料单,请稍后重试");
		}
		Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
		if (inventory == null) {
			return fail("存货信息不存在");
		}
		//包装数量
		if (isOk(moDoc.getIQty()) && isOk(inventory.getIPkgQty())) {
			int a = moDoc.getIQty().intValue();
			int b = inventory.getIPkgQty().intValue();
			int c = a % b;
			MoMoinvbatch moMoinvbatch;
			int seq=0;
			Date now=new Date();
			if (c > 0) {
				moMoinvbatch = new MoMoinvbatch();
				moMoinvbatch.setIOrgId(getOrgId());
				moMoinvbatch.setCOrgCode(getOrgCode());
				moMoinvbatch.setCOrgName(getOrgName());
				moMoinvbatch.setIMoDocId(moDoc.getIAutoId());
				//moMoinvbatch.seti
				String barcode = BillNoUtils.genCassiGnOrderNo(getOrgId(), "CP", 7);
				moMoinvbatch.setISeq(1); //数量
				seq=1;
				moMoinvbatch.setIQty(new BigDecimal(c));
				moMoinvbatch.setCBarcode(barcode);
				moMoinvbatch.setIPrintStatus(1);
				moMoinvbatch.setIStatus(0);
				moMoinvbatch.setICreateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCCreateName(JBoltUserKit.getUserUserName());
				moMoinvbatch.setDCreateTime(now);
				moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
				moMoinvbatch.setDUpdateTime(now);
				moMoinvbatch.save();
			}

			c = a / b;

			for (int i = 0; i < c; i++) {
				moMoinvbatch = new MoMoinvbatch();
				moMoinvbatch.setIOrgId(getOrgId());
				moMoinvbatch.setCOrgCode(getOrgCode());
				moMoinvbatch.setCOrgName(getOrgName());
				moMoinvbatch.setIMoDocId(moDoc.getIAutoId());
				String barcode = BillNoUtils.genCassiGnOrderNo(getOrgId(), "CP", 7);
				moMoinvbatch.setCBarcode(barcode);
				moMoinvbatch.setIQty(new BigDecimal(b));
				seq=seq+i;
				moMoinvbatch.setISeq(Integer.valueOf(seq));
				moMoinvbatch.setIPrintStatus(1);
				moMoinvbatch.setIStatus(0);
				moMoinvbatch.setICreateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCCreateName(JBoltUserKit.getUserUserName());
				moMoinvbatch.setDCreateTime(now);
				moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
				moMoinvbatch.setDUpdateTime(now);
				moMoinvbatch.save();

			}


		}
		return SUCCESS;
	}

}