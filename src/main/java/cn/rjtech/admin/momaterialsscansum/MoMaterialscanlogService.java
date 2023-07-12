package cn.rjtech.admin.momaterialsscansum;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momaterialscanusedlog.MoMaterialscanusedlogmService;
import cn.rjtech.model.momdata.MoMaterialscanlog;
import cn.rjtech.model.momdata.MoMaterialsscansum;
import cn.rjtech.model.momdata.MoMojob;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.*;

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
	@Inject
	private  MoDocService moDocService;
	/**
	 * 后台管理分页查询
	 */
	public Page<MoMaterialscanlog> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId", "DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
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
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids, true);
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return deleteById(id, true);
	}

	/**
	 * 删除数据后执行的回调
	 *
	 * @param moMaterialscanlog 要删除的model
	 * @param kv                携带额外参数一般用不上
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
	 */
	@Override
	public String checkCanDelete(MoMaterialscanlog moMaterialscanlog, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialscanlog, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 未扫描/已扫码
	 */
	public Page<Record> getMoMaterialNotScanLogList(int pageNumber, int pageSize, Kv kv) {
		Page<Record> recordPage=dbTemplate("momaterialsscansum.getMaterialScanLogN", kv).paginate(pageNumber, pageSize);
		return dbTemplate("momaterialsscansum.getMaterialScanLogN", kv).paginate(pageNumber, pageSize);
	}

	public Ret addBarcode(String barcode, Long imodocid) {
		Record record = dbTemplate("momaterialsscansum.getMaterialScanLogByBarcode",Kv.by("cBarcode",barcode)).findFirst();

		if(record==null){
			return  fail("当前现品票已扫码");
		}
		Map<String,String> map=new HashMap<>();

			tx(() -> {
				MoMaterialscanlog moMaterialscanlog = new MoMaterialscanlog();

					moMaterialscanlog.setIAutoId(record.get("iautoid"));
					moMaterialscanlog.setIsScanned(true);
					moMaterialscanlog.setScanTime(new Date());
					moMaterialscanlog.update();
				Record jobN = dbTemplate("momaterialsscansum.getByBarcodeF",Kv.by("imodocid",imodocid)).findFirst();
				Record jobY = dbTemplate("momaterialsscansum.getByBarcodeN",Kv.by("imodocid",imodocid)).findFirst();
				MoMojob moMojob=new MoMojob();
				moMojob.setIMoDocId(imodocid);
				moMojob.setCMoJobSn("1");
				moMojob.setIPlanQty(jobN.getInt("iplanqty"));
				moMojob.setIRealQty(jobY.getInt("irealqty"));
				if(Objects.equals(jobN.getInt("iplanqty"), jobY.getInt("irealqty"))){
					moMojob.setIStatus(2);
				}else{
					moMojob.setIStatus(1);
				}
				moMojob.setDUpdateTime(new Date());
				moMojob.save();
				return  true;
			});
		return  successWithData(map);
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
				//moMaterialsscansum.setIScannedQty(moMaterialsscansum.getIScannedQty().add(BigDecimal.ONE));
				//moMaterialsscansum.update();
			}
		}
	}

	/**
	 * 获取子件计划数
	 */
    public Record getPlanQty(Long imodocid, Long iinventoryid) {
        Kv kv = Kv.create()
                .setIfNotNull("imodocid", imodocid)
                .setIfNotNull("iinventoryid", iinventoryid);

        return dbTemplate("momaterialsscansum.findInvCodeUseNum", kv).findFirst();
    }

    public Page<Record> getBarcodeAll(int pageNumber, int pageSize, Kv kv) {
        Page<Record> page = dbTemplate("momaterialsscansum.getBarcodeAll", kv).paginate(pageNumber, pageSize);

        List<Record> record1 = dbTemplate("momaterialsscansum.getAllById", kv).find();

        for (Record record : page.getList()) {
            record.set("iqty2", ZERO_STR);
            for (Record record2 : record1) {
                if (record.getLong("iautoid").longValue() == record2.getLong("iautoid").longValue()) {
                    record.set("iqty2", record2.get("iqty"));
                }
            }
        }

        return page;
    }
	public Map<String, Object> getApiBarcodeAll(int pageNumber, int pageSize, Kv kv) {
		Page<Record> page = dbTemplate("momaterialsscansum.getBarcodeAll", kv).paginate(pageNumber, pageSize);

		List<Record> record1 = dbTemplate("momaterialsscansum.getAllById", kv).find();

		for (Record record : page.getList()) {
			record.set("iqty2", ZERO_STR);
			for (Record record2 : record1) {
				if (record.getLong("iautoid").longValue() == record2.getLong("iautoid").longValue()) {
					record.set("iqty2", record2.get("iqty"));
				}
			}
		}
		String isScanned = moDocService.findByisScanned(kv.getLong("imodocid"));
		Map<String,Object> map =new HashMap<>();
		map.put("isScanned",isScanned);
		map.put("pageInfo",page);
		return map;
	}


}