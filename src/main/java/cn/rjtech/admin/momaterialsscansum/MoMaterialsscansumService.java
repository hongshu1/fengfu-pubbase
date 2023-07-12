package cn.rjtech.admin.momaterialsscansum;


import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.moroutingcinve.MoMoroutinginvcService;
import cn.rjtech.model.momdata.MoMaterialsscansum;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * 制造工单-齐料汇总  Service
 * @ClassName: MoMaterialsscansumService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-22 15:46
 */
public class MoMaterialsscansumService extends BaseService<MoMaterialsscansum> {

	private final MoMaterialsscansum dao = new MoMaterialsscansum().dao();

	@Override
	protected MoMaterialsscansum dao() {
		return dao;
	}
	@Inject
	private MoDocService moDocService; //制造工单
	@Inject
	MoMoroutinginvcService moMoroutinginvcService; //制造工单物料
	@Inject
	InventoryService inventoryService; //存货档案

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return  dbTemplate("momaterialsscansum.findAll",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param moMaterialsscansum
	 * @return
	 */
	public Ret save(MoMaterialsscansum moMaterialsscansum) {
		if(moMaterialsscansum==null || isOk(moMaterialsscansum.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialsscansum.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsscansum.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialsscansum.getIautoid(), JBoltUserKit.getUserId(), moMaterialsscansum.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialsscansum
	 * @return
	 */
	public Ret update(MoMaterialsscansum moMaterialsscansum) {
		if(moMaterialsscansum==null || notOk(moMaterialsscansum.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialsscansum dbMoMaterialsscansum=findById(moMaterialsscansum.getIAutoId());
		if(dbMoMaterialsscansum==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialsscansum.getName(), moMaterialsscansum.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsscansum.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialsscansum.getIautoid(), JBoltUserKit.getUserId(), moMaterialsscansum.getName());
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
	 * @param moMaterialsscansum 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialsscansum moMaterialsscansum, Kv kv) {
		//addDeleteSystemLog(moMaterialsscansum.getIautoid(), JBoltUserKit.getUserId(),moMaterialsscansum.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialsscansum 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialsscansum moMaterialsscansum, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialsscansum, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}


	public List<Record> dataList(Kv kv) {
		/*Sql sql = selectSql().select().from(table(),"a").
				leftJoin(moDocService.table(), "b","a.iMoDocId=b.iautoId")
				.leftJoin();*/
		return  dbTemplate("momaterialsscansum.findAll",kv).find();
	}

	/**
	 * 通过现票获取存货信息
	 * @param barcode
	 * @return
	 */
	public Record  getBarcode(String barcode){
		return  dbTemplate("momaterialsscansum.findByBarcode",
				Kv.create().set("barcode",barcode)).findFirst();
	}

	public Ret add(String barcode){
		Record record=getBarcode(barcode);
		if(record!=null){
			if(isOk(record.getLong("iinventoryid"))) {
				MoMaterialsscansum moMaterialsscansum=findFirst(Okv.create().
						set(MoMaterialsscansum.IINVENTORYID,record.getLong("iinventoryid")),MoMaterialsscansum.IAUTOID,"DESC");
				if(moMaterialsscansum!=null) {
					moMaterialsscansum = new MoMaterialsscansum();
					moMaterialsscansum.setIInventoryId(record.getLong("iinventoryid"));
					moMaterialsscansum.setIMoDocId(record.get("imdocid"));
					moMaterialsscansum.setIPlanQty(record.getBigDecimal("qty"));//计划数 iScannedQty
					moMaterialsscansum.setIScannedQty(BigDecimal.ONE);
					moMaterialsscansum.save();
				}else{
					moMaterialsscansum.setIScannedQty(moMaterialsscansum.getIScannedQty().add(BigDecimal.ONE));
					moMaterialsscansum.update();
				}
			}

		}
		return  SUCCESS;
	}
	/**
	 * 通过现票获取存货信息
	 * @param barcode
	 * @return
	 */
	public Record  getBarcode(String barcode,Long imodocid){
		return  dbTemplate("momaterialsscansum.findByBarcode",
				Kv.create().set("barcode",barcode)
						.set("imodocid",imodocid)
		).findFirst();
	}

	public Ret add(String barcode,Long imodoid){
		Record record=getBarcode(barcode,imodoid);
		if(record!=null){
			if(isOk(record.getLong("iinventoryid"))) {
				MoMaterialsscansum moMaterialsscansum=findFirst(Okv.create().
						set(MoMaterialsscansum.IINVENTORYID,record.getLong("iinventoryid")),MoMaterialsscansum.IAUTOID,"DESC");
				if(moMaterialsscansum!=null) {
					moMaterialsscansum = new MoMaterialsscansum();
					moMaterialsscansum.setIInventoryId(record.getLong("iinventoryid"));
					moMaterialsscansum.setIMoDocId(record.get("imdocid"));
					moMaterialsscansum.setIPlanQty(record.getBigDecimal("qty"));//计划数 iScannedQty
					moMaterialsscansum.setIScannedQty(BigDecimal.ONE);
					moMaterialsscansum.save();
				}else{
					moMaterialsscansum.setIScannedQty(moMaterialsscansum.getIScannedQty().add(BigDecimal.ONE));
					moMaterialsscansum.update();
				}
			}

		}
		return  SUCCESS;
	}

}