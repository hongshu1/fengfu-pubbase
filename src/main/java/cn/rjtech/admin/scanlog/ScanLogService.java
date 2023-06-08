package cn.rjtech.admin.scanlog;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.common.model.Barcodedetail;
import cn.rjtech.model.momdata.ScanLog;
import com.google.gson.Gson;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
/**
 * 扫描日志 Service
 * @ClassName: ScanLogService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-05 10:00
 */
public class ScanLogService extends BaseService<ScanLog> {

	private final ScanLog dao = new ScanLog().dao();

	@Override
	protected ScanLog dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<ScanLog> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("ModifyDate","DESC", pageNumber, pageSize, keywords, "AutoID");
	}

	/**
	 * 保存
	 * @param scanLog
	 * @return
	 */
	public Ret save(ScanLog scanLog) {
		if(scanLog==null || isOk(scanLog.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(scanLog.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=scanLog.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(scanLog.getAutoID(), JBoltUserKit.getUserId(), scanLog.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param scanLog
	 * @return
	 */
	public Ret update(ScanLog scanLog) {
		if(scanLog==null || notOk(scanLog.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ScanLog dbScanLog=findById(scanLog.getAutoID());
		if(dbScanLog==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(scanLog.getName(), scanLog.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=scanLog.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(scanLog.getAutoID(), JBoltUserKit.getUserId(), scanLog.getName());
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
	public Ret delete(Integer id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param scanLog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ScanLog scanLog, Kv kv) {
		//addDeleteSystemLog(scanLog.getAutoID(), JBoltUserKit.getUserId(),scanLog.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param scanLog 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(ScanLog scanLog, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(scanLog, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	public void saveScanLogModel(ScanLog log, Date now, Barcodedetail detail){
		log.setOrganizeCode(getOrgCode());
		//log.setLogNo();
		log.setProcessType("InitStock");
		//log.setSourceBillType();
		//log.setSourceBillNo();
		//log.setSourceBillNoRow();
		//log.setSourceBillRowNo();
		//log.setTagBillType();
		//log.setTagBillNo();
		//log.setTagBillNoRow();
		//log.setTagBillRowNo();
		//log.setIWhCode();
		//log.setOWhCode();
		//log.setIPosCode();
		//log.setOPosCode();
		//log.setPBarcode();
		log.setBarcode(new Gson().toJson(detail));
		log.setInvCode(detail.getInvcode());
		//log.setCurQty();
		log.setQty(detail.getQty());
		//log.setNum();
		log.setBatch(detail.getBatch());
		//log.setWeight();
		//log.setPackRate();
		//log.setLength();
		//log.setVolume();
		//log.setColor();
		//log.setMark();
		log.setMemo("新增期初库存");//新增期初库存、新增期初条码
		//log.setDeleteDate();
		log.setCreatePerson(JBoltUserKit.getUserName());
		log.setCreateDate(now);
		log.setModifyPerson(JBoltUserKit.getUserName());
		log.setModifyDate(now);
	}

}