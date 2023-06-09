package cn.rjtech.admin.momaterialsreturn;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.model.momdata.Inventory;
import cn.rjtech.model.momdata.MoMaterialsreturnd;
import cn.rjtech.model.momdata.MoMaterialsreturnm;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 制造工单-生产退料主表  Service
 * @ClassName: MoMaterialsreturnmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 16:32
 */
public class MoMaterialsreturnmService extends BaseService<MoMaterialsreturnm> {

	private final MoMaterialsreturnm dao = new MoMaterialsreturnm().dao();

	@Override
	protected MoMaterialsreturnm dao() {
		return dao;
	}

	@Inject
	private InventoryService inventoryService;

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv kv) {
		return  dbTemplate("momaterialsreturn.paginateAdminDatas",kv).paginate(pageNumber,pageSize);
	}

	/**
	 * 保存
	 * @param moMaterialsreturnm
	 * @return
	 */
	public Ret save(MoMaterialsreturnm moMaterialsreturnm) {
		if(moMaterialsreturnm==null || isOk(moMaterialsreturnm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialsreturnm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsreturnm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialsreturnm.getIautoid(), JBoltUserKit.getUserId(), moMaterialsreturnm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialsreturnm
	 * @return
	 */
	public Ret update(MoMaterialsreturnm moMaterialsreturnm) {
		if(moMaterialsreturnm==null || notOk(moMaterialsreturnm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialsreturnm dbMoMaterialsreturnm=findById(moMaterialsreturnm.getIAutoId());
		if(dbMoMaterialsreturnm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialsreturnm.getName(), moMaterialsreturnm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialsreturnm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialsreturnm.getIautoid(), JBoltUserKit.getUserId(), moMaterialsreturnm.getName());
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
	 * @param moMaterialsreturnm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialsreturnm moMaterialsreturnm, Kv kv) {
		//addDeleteSystemLog(moMaterialsreturnm.getIautoid(), JBoltUserKit.getUserId(),moMaterialsreturnm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialsreturnm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialsreturnm moMaterialsreturnm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialsreturnm, kv);
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
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param moMaterialsreturnm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMaterialsreturnm moMaterialsreturnm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMaterialsreturnm moMaterialsreturnm, String column, Kv kv) {
		//addUpdateSystemLog(moMaterialsreturnm.getIautoid(), JBoltUserKit.getUserId(), moMaterialsreturnm.getName(),"的字段["+column+"]值:"+moMaterialsreturnm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialsreturnm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMaterialsreturnm moMaterialsreturnm, Kv kv) {
		//这里用来覆盖 检测MoMaterialsreturnm是否被其它表引用
		return null;
	}
	/**
	 * 新增退货
	 * @param imodocid
	 * @param jBoltTable
	 * @return
	 */
	public Ret addMoMaterialsreturn(Long imodocid, JBoltTable jBoltTable) {
		List<Record> rows=jBoltTable.getSaveRecordList();
		if(rows.isEmpty()){
			return  fail("缺少数据");
		}
		Date now=new Date();
		tx(() -> {
			MoMaterialsreturnm moMaterialsreturnm = new MoMaterialsreturnm();
			moMaterialsreturnm.setIOrgId(getOrgId());
			moMaterialsreturnm.setCOrgCode(getOrgCode());
			moMaterialsreturnm.setIMoDocId(imodocid);
			//moMaterialsreturnm.setIAuditWay();
			moMaterialsreturnm.setCMemo(jBoltTable.getFormRecord().getStr("cmemo"));
			moMaterialsreturnm.setICreateBy(JBoltUserKit.getUserId());
			moMaterialsreturnm.setCCreateName(JBoltUserKit.getUserUserName());

			//moMaterialsreturnm.setDAuditTime(now);

			moMaterialsreturnm.setDCreateTime(now);
			moMaterialsreturnm.setCUpdateName(JBoltUserKit.getUserName());
			moMaterialsreturnm.setIUpdateBy(JBoltUserKit.getUserId());
			moMaterialsreturnm.setDUpdateTime(now);
			moMaterialsreturnm.setIsDeleted(true);
			moMaterialsreturnm.save();
			MoMaterialsreturnd moMaterialsreturnd;
			for (Record record : rows) {
				String cinvcode = record.getStr("cinvcode");
				Inventory inventory = inventoryService.findBycInvCode(cinvcode);

				if (inventory != null) {
					moMaterialsreturnd = new MoMaterialsreturnd();
					moMaterialsreturnd.setIInventoryId(inventory.getIAutoId());
					moMaterialsreturnd.setIMoDocId(imodocid);
					moMaterialsreturnd.setIQty(record.getBigDecimal("iqty"));
					moMaterialsreturnd.setIMaterialsReturnMid(moMaterialsreturnm.getIAutoId());
					moMaterialsreturnd.setCbarcode(record.getStr("cbarcode"));
					moMaterialsreturnd.setIMaterialsReturnMid(moMaterialsreturnm.getIAutoId());
					moMaterialsreturnd.save();
				}
			}
			return  true;
		});
		return  SUCCESS;
	}

	/**
	 * 获取保存后的明细
	 * @return
	 */
	public Page<Record>  getMoMaterialsreturnList(int pageNumber, int pageSize,Kv kv){
		kv.set("corgcode",getOrgCode());
		Page<Record> rows=dbTemplate("momaterialsreturn.findByImodocId", kv).paginate(pageNumber,pageSize);
		return rows;
	}
}