package cn.rjtech.admin.momaterialscanusedlog;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.department.DepartmentService;
import cn.rjtech.admin.materialsout.MaterialsOutService;
import cn.rjtech.admin.materialsoutdetail.MaterialsOutDetailService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.sysmaterialspreparedetail.SysMaterialspreparedetailService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.admin.warehousearea.WarehouseAreaService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

/**
 * 制造工单-材料耗用主表 Service
 * @ClassName: MoMaterialscanusedlogmService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-25 18:06
 */
public class MoMaterialscanusedlogmService extends BaseService<MoMaterialscanusedlogm> {

	private final MoMaterialscanusedlogm dao = new MoMaterialscanusedlogm().dao();

	@Override
	protected MoMaterialscanusedlogm dao() {
		return dao;
	}

	@Inject
	private  MoMaterialscanusedlogdService moMaterialscanusedlogdService;
	@Inject
	private MoDocService moDocService;

	@Inject
	private DepartmentService departmentService; //部门
	@Inject
	private WarehouseService warehouseService; //仓库
	@Inject
	private WorkregionmService workregionmService;//产线

	@Inject
	private MaterialsOutService materialsOutService;//出库单

	@Inject
	private MaterialsOutDetailService materialsOutDetailService;//出库单明细

	@Inject
	private SysMaterialspreparedetailService sysMaterialspreparedetailService;

	@Inject
	private WarehouseAreaService warehouseAreaService; //库区




	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<MoMaterialscanusedlogm> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param moMaterialscanusedlogm
	 * @return
	 */
	public Ret save(MoMaterialscanusedlogm moMaterialscanusedlogm) {
		if(moMaterialscanusedlogm==null || isOk(moMaterialscanusedlogm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(moMaterialscanusedlogm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanusedlogm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(moMaterialscanusedlogm.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanusedlogm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param moMaterialscanusedlogm
	 * @return
	 */
	public Ret update(MoMaterialscanusedlogm moMaterialscanusedlogm) {
		if(moMaterialscanusedlogm==null || notOk(moMaterialscanusedlogm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		MoMaterialscanusedlogm dbMoMaterialscanusedlogm=findById(moMaterialscanusedlogm.getIAutoId());
		if(dbMoMaterialscanusedlogm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(moMaterialscanusedlogm.getName(), moMaterialscanusedlogm.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=moMaterialscanusedlogm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(moMaterialscanusedlogm.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanusedlogm.getName());
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
	 * @param moMaterialscanusedlogm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(MoMaterialscanusedlogm moMaterialscanusedlogm, Kv kv) {
		//addDeleteSystemLog(moMaterialscanusedlogm.getIautoid(), JBoltUserKit.getUserId(),moMaterialscanusedlogm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialscanusedlogm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(MoMaterialscanusedlogm moMaterialscanusedlogm, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(moMaterialscanusedlogm, kv);
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
	 * @param moMaterialscanusedlogm 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(MoMaterialscanusedlogm moMaterialscanusedlogm,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(MoMaterialscanusedlogm moMaterialscanusedlogm, String column, Kv kv) {
		//addUpdateSystemLog(moMaterialscanusedlogm.getIautoid(), JBoltUserKit.getUserId(), moMaterialscanusedlogm.getName(),"的字段["+column+"]值:"+moMaterialscanusedlogm.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param moMaterialscanusedlogm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(MoMaterialscanusedlogm moMaterialscanusedlogm, Kv kv) {
		//这里用来覆盖 检测MoMaterialscanusedlogm是否被其它表引用
		return null;
	}

    public List<Record> getMaterialscanList(Kv kv) {
		return  dbTemplate("momaterialscanusedlog.getMaterialscanList",kv).find();
    }

	public Page<Record> getMaterialsPrepareList(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("momaterialscanusedlog.getMaterialsPrepareList",kv).paginate(pageNumber,pageSize);
	}

	public Page<Record> getMoMaterialscanusedlogList(int pageNumber, int pageSize, Kv kv){
		if(notOk(kv.getLong("imodocid"))){
			return  new Page<Record>();
		}
		return dbTemplate("momaterialscanusedlog.getMoMaterialscanusedlogList",kv).paginate(pageNumber,pageSize);
	}
	/**
	 * 通过现票获取存货信息
	 * @param barcode
	 * @return
	 */
	public Record  getBarcode(String barcode){
		return  dbTemplate("momaterialscanusedlog.findByBarcode",
				Kv.create().set("barcode",barcode)).findFirst();
	}

	public Ret addBarcode(String barcode,Long iMaterialScanUsedLogMid){
		Record record=getBarcode(barcode);
		if(record!=null){
			if(isOk(record.getLong("iinventoryid"))) {
				MoMaterialscanusedlogd moMaterialscanusedlogd=moMaterialscanusedlogdService.findFirst(Okv.create().
						set(MoMaterialscanusedlogd.IINVENTORYID,record.getLong("iinventoryid")).
						set(MoMaterialscanusedlogd.IMATERIALSCANUSEDLOGMID,iMaterialScanUsedLogMid),
						MoMaterialsscansum.IAUTOID,"DESC");
				if(moMaterialscanusedlogd!=null) {
					moMaterialscanusedlogd = new MoMaterialscanusedlogd();
					moMaterialscanusedlogd.setIInventoryId(record.getLong("iinventoryid"));
					moMaterialscanusedlogd.setIAutoId(iMaterialScanUsedLogMid);
					moMaterialscanusedlogd.setCBarcode(barcode);
					moMaterialscanusedlogd.setIQty(record.getBigDecimal("qty"));
					moMaterialscanusedlogd.setIScannedQty(BigDecimal.ONE);
				}else{
					moMaterialscanusedlogd.setIScannedQty(moMaterialscanusedlogd.getIScannedQty().add(BigDecimal.ONE));
					moMaterialscanusedlogd.update();
				}
			}

		}
		return  SUCCESS;
	}

	public MoMaterialscanusedlogm addDoc(Long imodocid) {
		if(notOk(imodocid)){
			ValidationUtils.notNull(imodocid,"缺少工单信息");
		}
		//先去了
	/*	MoDoc m=moDocService.findById(imodocid);
		if(m==null){
			ValidationUtils.notNull(imodocid,"缺少工单信息");
		}*/
		Date now=new Date();
		MoMaterialscanusedlogm moMaterialscanusedlogm=new MoMaterialscanusedlogm();
		moMaterialscanusedlogm.setIOrgId(getOrgId());
		moMaterialscanusedlogm.setCOrgCode(getOrgCode());
		moMaterialscanusedlogm.setCOrgName(getOrgName());
		moMaterialscanusedlogm.setIMoDocId(imodocid);
		String billNo = BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5);
		moMaterialscanusedlogm.setCDocNo(billNo); //材料出库单号
		moMaterialscanusedlogm.setICreateBy(JBoltUserKit.getUserId());
		moMaterialscanusedlogm.setCCreateName(JBoltUserKit.getUserUserName());
		moMaterialscanusedlogm.setDCreateTime(now);
		moMaterialscanusedlogm.setIUpdateBy(JBoltUserKit.getUserId());
		moMaterialscanusedlogm.setCUpdateName(JBoltUserKit.getUserName());
		moMaterialscanusedlogm.setDUpdateTime(now);
		moMaterialscanusedlogm.save();
		return  moMaterialscanusedlogm;
	}
	public  Ret createOutDoc1(String cdocno,Long imaterialscanusedlogmid){
	   //OtherOutService
		Date now=new Date();
		MaterialsOut materialsOut=new MaterialsOut();
		materialsOut.setSourceBillType("生产工单");
		MoMaterialscanusedlogm moMaterialscanusedlogm=findById(imaterialscanusedlogmid);
		if(moMaterialscanusedlogm!=null) {
			materialsOut.setSourceBillDid(String.valueOf(moMaterialscanusedlogm.getIMoDocId()));
			materialsOut.setOrganizeCode(getOrgCode());
		}
		materialsOut.setRdCode("");
		materialsOut.setBillNo(cdocno);//出库单号
		materialsOut.setBillType("材料出库单");
		materialsOut.setBillDate(now);
		materialsOut.setWhcode("");//仓库
		//工单
		MoDoc moDoc=moDocService.findById(moMaterialscanusedlogm.getIMoDocId());
		if(moDoc!=null){
			//取部门
			Department department=departmentService.findById(moDoc.getIDepartmentId());
			if(department!=null) {
				materialsOut.setDeptCode(department.getCDepCode());
			}
		}
		materialsOut.setIcreateBy(JBoltUserKit.getUserId());
		materialsOut.setCcreateName(JBoltUserKit.getUserName());
		materialsOut.setDcreateTime(now);
		materialsOut.setState(2);//待审核
		//materialsOut.save();
		//处理明细
		List<MoMaterialscanusedlogd> rows=moMaterialscanusedlogdService.find("SELECT * FROM Mo_MaterialScanUsedLogD where iMaterialScanUsedLogMid= ?",imaterialscanusedlogmid);
		if(!rows.isEmpty()) {
			List<MaterialsOutDetail> materialsOutDetails = new ArrayList<>();
			MaterialsOutDetail materialsOutDetail;
			for (MoMaterialscanusedlogd moMaterialscanusedlogd : rows) {
				SysMaterialspreparedetail sysMaterialspreparedetail =
						sysMaterialspreparedetailService.findFirst(Okv.create().set(SysMaterialspreparedetail.SOURCEBILLNO, moDoc.getCMoDocNo()).
										set(SysMaterialspreparedetail.SOURCEBILLID, String.valueOf(moDoc.getIAutoId())).set
												(SysMaterialspreparedetail.BARCODE, moMaterialscanusedlogd.getCBarcode()),
								SysMaterialspreparedetail.AUTOID, "desc");

				materialsOutDetail = new MaterialsOutDetail();
				materialsOutDetail.setMasID(materialsOut.getAutoID());
				materialsOutDetail.setPosCode(sysMaterialspreparedetail.getPosCode());
				materialsOutDetail.setBarcode(moMaterialscanusedlogd.getCBarcode());
				materialsOutDetail.setNum(BigDecimal.ZERO);//备料件数
				materialsOutDetail.setQty(moMaterialscanusedlogd.getIScannedQty());//取耗费数量
				materialsOutDetail.setSourceBillType("制造工单");
				materialsOutDetail.setSourceBillNo(moDoc.getCMoDocNo());//来源单号
				materialsOutDetail.setSourceBillID(String.valueOf(moDoc.getIAutoId()));
				materialsOutDetail.setCcreatename(JBoltUserKit.getUserName());
				materialsOutDetail.setDcreatetime(now);
				materialsOutDetails.add(materialsOutDetail);
			}
		}








      return  SUCCESS;
	}
	public   Ret createOutDoc(String cdocno,Long imaterialscanusedlogmid){
		Date now=new Date();
		MoMaterialscanusedlogm moMaterialscanusedlogm=findById(imaterialscanusedlogmid);


		//工单
		MoDoc moDoc=moDocService.findById(moMaterialscanusedlogm.getIMoDocId());
		String deptcode="";
		Department department=departmentService.findById(moDoc.getIDepartmentId());
		if(department!=null) {
			//otherOut.setDeptCode(department.getCDepCode());
			deptcode=department.getCDepCode();
		}

		//materialsOut.save();
		//处理明细
		List<MoMaterialscanusedlogd> rows=moMaterialscanusedlogdService.find("SELECT * FROM Mo_MaterialScanUsedLogD where iMaterialScanUsedLogMid= ?",imaterialscanusedlogmid);
		if(!rows.isEmpty()) {
			List<MaterialsOutDetail> materialsOutDetails = new ArrayList<>();
			MaterialsOutDetail materialsOutDetail;
			for (MoMaterialscanusedlogd moMaterialscanusedlogd : rows) {
				SysMaterialspreparedetail sysMaterialspreparedetail =
						sysMaterialspreparedetailService.findFirst(Okv.create().set(SysMaterialspreparedetail.SOURCEBILLNO, moDoc.getCMoDocNo()).
										set(SysMaterialspreparedetail.SOURCEBILLID, String.valueOf(moDoc.getIAutoId())).set
												(SysMaterialspreparedetail.BARCODE, moMaterialscanusedlogd.getCBarcode()),
								SysMaterialspreparedetail.AUTOID, "desc");

				materialsOutDetail = new MaterialsOutDetail();

				materialsOutDetail.setPosCode(sysMaterialspreparedetail.getPosCode());
				materialsOutDetail.setBarcode(moMaterialscanusedlogd.getCBarcode());
				materialsOutDetail.setNum(BigDecimal.ZERO);//备料件数
				materialsOutDetail.setQty(moMaterialscanusedlogd.getIScannedQty());//取耗费数量
				materialsOutDetail.setSourceBillType("制造工单");
				materialsOutDetail.setSourceBillNo(moDoc.getCMoDocNo());//来源单号
				materialsOutDetail.setSourceBillID(String.valueOf(moDoc.getIAutoId()));
				materialsOutDetail.setCcreatename(JBoltUserKit.getUserName());
				materialsOutDetail.setDcreatetime(now);
				materialsOutDetails.add(materialsOutDetail);
			}
			Map<Long,List<MaterialsOutDetail>>  map=datas(materialsOutDetails);
			MaterialsOut materialsOut;
			for (Map.Entry<Long, List<MaterialsOutDetail>> entry : map.entrySet()) {
				  Warehouse warehouse=warehouseService.findById(entry.getKey());
				   materialsOut=new MaterialsOut();
				   materialsOut.setSourceBillType("制造工单");
				   materialsOut.setRdCode("");
				   materialsOut.setOrganizeCode(getOrgCode());
				   String billNo = BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5);
				   materialsOut.setBillNo(billNo);
				   materialsOut.setBillType("材料出库单");
				   materialsOut.setBillDate(now);
				   materialsOut.setDeptCode(deptcode);
				   materialsOut.setIcreateBy(JBoltUserKit.getUserId());
				   materialsOut.setCcreateName(JBoltUserKit.getUserName());
				   materialsOut.setDcreateTime(now);
				   materialsOut.setState(2);
				  materialsOut.save();
				MaterialsOut finalMaterialsOut = materialsOut;
				List<MaterialsOutDetail> materials=entry.getValue();
				materials.stream().forEach(obj -> obj.setMasID(finalMaterialsOut.getAutoID()));
				materialsOutDetailService.batchSave(materials);

			}
		}
		return  SUCCESS;

	}
	public Map<Long,List<MaterialsOutDetail>> datas(List<MaterialsOutDetail> materialsOutDetails){
		List<List<MaterialsOutDetail>> result = new ArrayList<>();
		Map<Long,List<MaterialsOutDetail>> map = new HashMap<>();
		//userList是要操作的list集合
		for (MaterialsOutDetail materialsOutDetail:materialsOutDetails) {
			String posCode =materialsOutDetail.getPosCode();
			//仓库
			WarehouseArea warehouseArea=warehouseAreaService.findByWhAreaCode(posCode);
			List<MaterialsOutDetail> detailList;
			if (map.containsKey(warehouseArea.getIwarehouseid())) {
				detailList= map.get(warehouseArea.getIwarehouseid());
			}else{
				detailList= new ArrayList<>();
			}
			detailList.add(materialsOutDetail);
			map.put(warehouseArea.getIwarehouseid(),detailList);
		}
		return  map;

	}
	public void test(){
		Date now=new Date();
		OtherOut otherOut=new OtherOut();
		MoMaterialscanusedlogm moMaterialscanusedlogm=findById(0);
		otherOut.setSourceBillDid(String.valueOf(moMaterialscanusedlogm.getIAutoId()));
		otherOut.setOrganizeCode(moMaterialscanusedlogm.getCOrgCode());
		otherOut.setBillNo("");
		otherOut.setBillType("材料出库单"); //sn=11
		otherOut.setBillDate((now));
		//读取工单信息
		MoDoc moDoc=moDocService.findById(moMaterialscanusedlogm.getIMoDocId());
		//读取工单信息
		//MoDoc moDoc=moDocService.findById(0);
		if(moDoc!=null) {
			//取部门
			Department department=departmentService.findById(moDoc.getIDepartmentId());
			if(department!=null) {
				//otherOut.setDeptCode(department.getCDepCode());
			}
			//获取产线
			Workregionm workregionm=workregionmService.findById(moDoc.getIWorkRegionMid());
			if(workregionm!=null){
				//仓库
				Warehouse warehouse=warehouseService.findById(workregionm.getIWarehouseId());
			}
		}
	}


	public Page<Record>  getBarcodeAllBycBarcode(int pageNumber, int pageSize, Kv keywords) {
		Page<Record> page=dbTemplate("momaterialscanusedlog.getBarcodeAllBycBarcode",keywords).paginate(pageNumber,pageSize);
		return page;
	}

	public Record  getMaterialScanUsedLog(Kv keywords) {
		Record record=dbTemplate("momaterialscanusedlog.getMaterialScanUsedLog",keywords).findFirst();
		return record;
	}

	public Ret saveMoLogM(JBoltTable jBoltTable){
		List<Record> saveRecordList = jBoltTable.getSaveRecordList();
		Record record=jBoltTable.getFormRecord();
		tx(() -> {
			//新增材料耗用主表
			ValidationUtils.notNull(record.getLong("imodocid"),"主键为空!");
			MoMaterialscanusedlogm moMaterialscanusedlogm = new MoMaterialscanusedlogm();
			moMaterialscanusedlogm.setIOrgId(getOrgId());
			moMaterialscanusedlogm.setCOrgCode(getOrgCode());
			moMaterialscanusedlogm.setCOrgName(getOrgName());
			moMaterialscanusedlogm.setIMoDocId(record.getLong("imodocid"));
			moMaterialscanusedlogm.setCDocNo(BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5));
			moMaterialscanusedlogm.setICreateBy(JBoltUserKit.getUserId());
			moMaterialscanusedlogm.setCCreateName(JBoltUserKit.getUserName());
			moMaterialscanusedlogm.setDCreateTime(new Date());
			moMaterialscanusedlogm.setIsDeleted(true);
			moMaterialscanusedlogm.setIUpdateBy(JBoltUserKit.getUserId());
			moMaterialscanusedlogm.setCUpdateName(JBoltUserKit.getUserName());
			moMaterialscanusedlogm.setDUpdateTime(new Date());

			ValidationUtils.isTrue(moMaterialscanusedlogm.save(), "主表保存失败!");
			Long iAutoId = moMaterialscanusedlogm.getIAutoId();

			List<MoMaterialscanusedlogd> moMaterialscanusedlogds = new ArrayList<>();
			//新增材料出库单主表
			Record modoc =dbTemplate("momaterialscanusedlog.getModocDateLogs",Kv.by("imodocid",record.getLong("imodocid"))).findFirst();
			String billNo = BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5);
			MaterialsOut materialsOut=new MaterialsOut();
			materialsOut.setSourceBillType("材料耗用");
			materialsOut.setSourceBillDid(iAutoId.toString());
			materialsOut.setOrganizeCode(getOrgCode());
			materialsOut.setBillNo(billNo);
			materialsOut.setBillDate(new Date());
			materialsOut.setDeptCode(modoc.get("cdepcode"));
			materialsOut.setWhcode(modoc.get("cwhcode"));
			materialsOut.setState(2);
			materialsOut.setIAuditWay(2);
			materialsOut.setDSubmitTime(new Date());
			materialsOut.setIAuditStatus(1);
			materialsOut.setIsDeleted(false);
			materialsOut.setIcreateBy(JBoltUserKit.getUserId());
			materialsOut.setCcreateName(JBoltUserKit.getUserName());
			materialsOut.setDcreateTime(new Date());
			ValidationUtils.isTrue(materialsOut.save(), "保存材料出库单失败!");

			Long outAutoID = materialsOut.getAutoID();
			List<MaterialsOutDetail> materialsOutDetails =new ArrayList<>();

			for (Record record1 : saveRecordList) {
				//新增材料耗用明细
				MoMaterialscanusedlogd moMaterialscanusedlogd = new MoMaterialscanusedlogd();
				moMaterialscanusedlogd.setIMaterialScanUsedLogMid(iAutoId);
				moMaterialscanusedlogd.setIInventoryId(record1.getLong("iinventoryid"));
				moMaterialscanusedlogd.setCBarcode(record1.get("cbarcode"));
				moMaterialscanusedlogd.setIQty(record1.getBigDecimal("iqty"));
				moMaterialscanusedlogd.setIScannedQty(record1.getBigDecimal("iscannedqty"));
				moMaterialscanusedlogd.setIsFinished(false);
				moMaterialscanusedlogd.setIRemainQty(record1.getBigDecimal("iremainqty"));
				moMaterialscanusedlogds.add(moMaterialscanusedlogd);
				//新增材料出库单明细
				MaterialsOutDetail materialsOutDetail=new MaterialsOutDetail();
				materialsOutDetail.setMasID(outAutoID);
				materialsOutDetail.setBarcode(record1.get("cbarcode"));
				materialsOutDetail.setInvCode(record1.get("cinvcode"));
				materialsOutDetail.setQty(record1.getBigDecimal("iscannedqty"));
				materialsOutDetail.setIsDeleted(false);
				materialsOutDetail.setIcreateby(JBoltUserKit.getUserId());
				materialsOutDetail.setCcreatename(JBoltUserKit.getUserName());
				materialsOutDetail.setDcreatetime(new Date());
				materialsOutDetails.add(materialsOutDetail);
			}
			moMaterialscanusedlogdService.batchSave(moMaterialscanusedlogds);
			materialsOutDetailService.batchSave(materialsOutDetails);
			return true;
		});
		return null;
	}

	/**
	 * 新增Api接口
	 * @param
	 * @return
	 */
	public Ret saveMoLogMApi(Long  imodocid, JSONArray jBoltTableList){

		tx(() -> {
			//新增材料耗用主表
			ValidationUtils.notNull(imodocid,"主键为空!");
			MoMaterialscanusedlogm moMaterialscanusedlogm = new MoMaterialscanusedlogm();
			moMaterialscanusedlogm.setIOrgId(getOrgId());
			moMaterialscanusedlogm.setCOrgCode(getOrgCode());
			moMaterialscanusedlogm.setCOrgName(getOrgName());
			moMaterialscanusedlogm.setIMoDocId(imodocid);
			moMaterialscanusedlogm.setCDocNo(BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5));
			moMaterialscanusedlogm.setICreateBy(JBoltUserKit.getUserId());
			moMaterialscanusedlogm.setCCreateName(JBoltUserKit.getUserName());
			moMaterialscanusedlogm.setDCreateTime(new Date());
			moMaterialscanusedlogm.setIsDeleted(true);
			moMaterialscanusedlogm.setIUpdateBy(JBoltUserKit.getUserId());
			moMaterialscanusedlogm.setCUpdateName(JBoltUserKit.getUserName());
			moMaterialscanusedlogm.setDUpdateTime(new Date());

			ValidationUtils.isTrue(moMaterialscanusedlogm.save(), "主表保存失败!");
			Long iAutoId = moMaterialscanusedlogm.getIAutoId();

			List<MoMaterialscanusedlogd> moMaterialscanusedlogds = new ArrayList<>();
			//新增材料出库单主表
			Record modoc =dbTemplate("momaterialscanusedlog.getModocDateLogs",Kv.by("imodocid",imodocid)).findFirst();
			String billNo = BillNoUtils.getcDocNo(getOrgId(), "CLCK", 5);
			MaterialsOut materialsOut=new MaterialsOut();
			materialsOut.setSourceBillType("材料耗用");
			materialsOut.setSourceBillDid(iAutoId.toString());
			materialsOut.setOrganizeCode(getOrgCode());
			materialsOut.setBillNo(billNo);
			materialsOut.setBillDate(new Date());
			materialsOut.setDeptCode(modoc.get("cdepcode"));
			materialsOut.setWhcode(modoc.get("cwhcode"));
			materialsOut.setState(2);
			materialsOut.setIAuditWay(2);
			materialsOut.setDSubmitTime(new Date());
			materialsOut.setIAuditStatus(1);
			materialsOut.setIsDeleted(false);
			materialsOut.setIcreateBy(JBoltUserKit.getUserId());
			materialsOut.setCcreateName(JBoltUserKit.getUserName());
			materialsOut.setDcreateTime(new Date());
			ValidationUtils.isTrue(materialsOut.save(), "保存材料出库单失败!");

			Long outAutoID = materialsOut.getAutoID();
			List<MaterialsOutDetail> materialsOutDetails =new ArrayList<>();

			for (int i = 0; i < jBoltTableList.size(); i++) {
				JSONObject record1 = jBoltTableList.getJSONObject(i);
				//新增材料耗用明细
				MoMaterialscanusedlogd moMaterialscanusedlogd = new MoMaterialscanusedlogd();
				moMaterialscanusedlogd.setIMaterialScanUsedLogMid(iAutoId);
				moMaterialscanusedlogd.setIInventoryId(record1.getLong("iinventoryid"));
				moMaterialscanusedlogd.setCBarcode(record1.getString("cbarcode"));
				moMaterialscanusedlogd.setIQty(record1.getBigDecimal("iqty"));
				moMaterialscanusedlogd.setIScannedQty(record1.getBigDecimal("iscannedqty"));
				moMaterialscanusedlogd.setIsFinished(false);
				moMaterialscanusedlogd.setIRemainQty(record1.getBigDecimal("iremainqty"));
				moMaterialscanusedlogds.add(moMaterialscanusedlogd);
				//新增材料出库单明细
				MaterialsOutDetail materialsOutDetail=new MaterialsOutDetail();
				materialsOutDetail.setMasID(outAutoID);
				materialsOutDetail.setBarcode(record1.getString("cbarcode"));
				materialsOutDetail.setInvCode(record1.getString("cinvcode"));
				materialsOutDetail.setQty(record1.getBigDecimal("iscannedqty"));
				materialsOutDetail.setIsDeleted(false);
				materialsOutDetail.setIcreateby(JBoltUserKit.getUserId());
				materialsOutDetail.setCcreatename(JBoltUserKit.getUserName());
				materialsOutDetail.setDcreatetime(new Date());
				materialsOutDetails.add(materialsOutDetail);
			}
			moMaterialscanusedlogdService.batchSave(moMaterialscanusedlogds);
			materialsOutDetailService.batchSave(materialsOutDetails);
			return true;
		});
		return null;
	}

}