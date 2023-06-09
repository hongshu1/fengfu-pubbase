package cn.rjtech.admin.momaterialsscansum;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.admin.momaterialscanusedlog.MoMaterialscanusedlogmService;
import cn.rjtech.model.momdata.MoMaterialscanusedlogd;
import cn.rjtech.model.momdata.MoMaterialsscansum;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.MoMaterialscanlog;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 制造工单-齐料明细 Service
 * @ClassName: MoMaterialscanlogService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-26 09:50
 */
public class MoMaterialscanlogService extends BaseService<MoMaterialscanlog> {

	private final MoMaterialscanlog dao = new MoMaterialscanlog().dao();

	@Override
	protected MoMaterialscanlog dao() {
		return dao;
	}

	@Inject
	private MoMaterialscanusedlogmService moMaterialscanusedlogmService;

	@Inject
	private  MoMaterialsscansumService moMaterialsscansumService; //齐料汇总

	/**
	 * 后台管理分页查询
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMaterialscanlog> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 *
	 * @param moMaterialscanlog
	 * @return
	 */
	public Ret save(MoMaterialscanlog moMaterialscanlog) {
		if (moMaterialscanlog == null || isOk(moMaterialscanlog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialscanlog.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success = moMaterialscanlog.save();
		if (success) {
			//添加日志
			//addSaveSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanlog.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 *
	 * @param moMaterialscanlog
	 * @return
	 */
	public Ret update(MoMaterialscanlog moMaterialscanlog) {
		if (moMaterialscanlog == null || notOk(moMaterialscanlog.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialscanlog dbMoMaterialscanlog = findById(moMaterialscanlog.getIAutoId());
		if (dbMoMaterialscanlog == null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);
		}
		//if(existsName(moMaterialscanlog.getName(), moMaterialscanlog.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success = moMaterialscanlog.update();
		if (success) {
			//添加日志
			//addUpdateSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanlog.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 *
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids, true);
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id, true);
	}

	/**
	 * 删除数据后执行的回调
	 *
	 * @param moMaterialscanlog 要删除的model
	 * @param kv                携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialscanlog moMaterialscanlog, Kv kv) {
		//addDeleteSystemLog(moMaterialscanlog.getIautoid(), JBoltUserKit.getUserId(),moMaterialscanlog.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 *
	 * @param moMaterialscanlog 要删除的model
	 * @param kv                携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialscanlog moMaterialscanlog, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialscanlog, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 *
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 未扫描
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getMoMaterialNotScanLogList(int pageNumber, int pageSize, Kv kv) {

		return dbTemplate("momaterialsscansum.getMoMaterialNotScanLogList", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 扫描
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<Record> getMoMaterialScanLogList(int pageNumber, int pageSize, Kv kv) {
		return dbTemplate("momaterialsscansum.getMoMaterialScanLogList", kv).paginate(pageNumber, pageSize);
	}

	public Ret addBarcode(String barcode, Long imodocid) {
		Record record = moMaterialsscansumService.getBarcode(barcode,imodocid);
		if(record==null){
			return  fail("未找到当前现品票");
		}

		if (record != null) {
			tx(() -> {
				MoMaterialscanlog moMaterialscanlog = findFirst(Okv.create().
						setIfNotNull(MoMaterialscanlog.IMATERIALSPREPAIRDID, record.get("imaterialsprepairdid")).
						setIfNotNull(MoMaterialscanlog.CBARCODE, record.get("barcode")).
						setIfNotNull(MoMaterialscanlog.IMODOCID, imodocid), MoMaterialscanlog.IAUTOID, "desc");
				if (moMaterialscanlog == null) {
					moMaterialscanlog.setIMoDocId(imodocid);
					moMaterialscanlog.setIMaterialsPrepairDid(record.get("imaterialsprepairdid"));
					moMaterialscanlog.setIInventoryId(record.getLong("iinventoryid")); //存货ID
					moMaterialscanlog.setCBarcode(record.getStr("barcode"));
					//moMaterialscanlog.setIQty(new BigDecimal(1));
					moMaterialscanlog.setIQty(record.getBigDecimal("qty"));
					moMaterialscanlog.setICreateBy(JBoltUserKit.getUserId());
					moMaterialscanlog.setCCreateName(JBoltUserKit.getUserName());
					moMaterialscanlog.setDCreateTime(new Date());
					moMaterialscanlog.save();

				} else {
					//moMaterialscanlog.setIQty(moMaterialscanlog.getIQty().add(new BigDecimal(1)));
					//moMaterialscanlog.update();
					fail("已加入");
				}
				addMoMaterialsscansum(record);
				return  true;
			});
		}

		return  SUCCESS;
	}

	public void  addMoMaterialsscansum(Record record){

		if(isOk(record.getLong("iinventoryid"))) {
			MoMaterialsscansum moMaterialsscansum=moMaterialsscansumService.findFirst(Okv.create().
							set(MoMaterialsscansum.IINVENTORYID,record.getLong("iinventoryid"))
							.setIfNotNull(MoMaterialsscansum.IMODOCID,record.getLong("imodocid"))
					,MoMaterialsscansum.IAUTOID,"DESC");
			if(moMaterialsscansum!=null) {
				moMaterialsscansum = new MoMaterialsscansum();
				moMaterialsscansum.setIInventoryId(record.getLong("iinventoryid"));
				moMaterialsscansum.setIMoDocId(record.get("imodocid"));
				//获取工单子件计划数
				Record r=getPlanQty(record.get("imodocid"),record.getLong("iinventoryid"));
				if(r!=null){
					moMaterialsscansum.setIPlanQty(r.getBigDecimal("planiqty")); //计划数
				}

				moMaterialsscansum.setIScannedQty(record.getBigDecimal("qty"));
				moMaterialsscansum.save();
			}else{
				//moMaterialsscansum.setIScannedQty(moMaterialsscansum.getIScannedQty().add(new BigDecimal(1)));
				//moMaterialsscansum.update();
			}
		}

	}

	/**
	 * 获取子件计划数
	 * @param imodocid
	 * @param iinventoryid
	 * @return
	 */
	public Record getPlanQty(Long imodocid,Long iinventoryid){
		Kv kv=Kv.create().setIfNotNull("imodocid",imodocid).
				setIfNotNull("iinventoryid",iinventoryid);
		Record record=dbTemplate("momaterialsscansum.findInvCodeUseNum",kv).findFirst();
		return record;		}

}