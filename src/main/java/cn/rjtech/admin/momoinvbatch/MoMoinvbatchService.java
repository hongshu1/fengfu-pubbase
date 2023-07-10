package cn.rjtech.admin.momoinvbatch;

import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.cache.JBoltGlobalConfigCache;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.modoc.MoDocService;
import cn.rjtech.admin.momoinvbatch.vo.ModocResVo;
import cn.rjtech.admin.momoinvbatch.vo.SubPrintReqVo;
import cn.rjtech.admin.sysmaterialsprepare.SysMaterialsprepareService;
import cn.rjtech.admin.workregionm.WorkregionmService;
import cn.rjtech.admin.workshiftm.WorkshiftmService;
import cn.rjtech.model.momdata.*;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.DateUtils;
import cn.rjtech.util.Util;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	@Inject
	private WorkshiftmService workshiftmService; //班次

	@Inject
	private WorkregionmService workregionmService; //产线


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
				moMoinvbatch.setCVersion("00");//版本号
				moMoinvbatch.setIsEffective(true);
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
				moMoinvbatch.setCVersion("00");//版本号
				moMoinvbatch.setIsEffective(true);
				moMoinvbatch.save();

			}


		}
		return SUCCESS;
	}

	/**
	 * 获取最后工序操作人员
	 * @param imodocid
	 * @return
	 */
	public  StringBuffer   getModocLastProcessPerson(Long imodocid){
		StringBuffer stringBuffer=new StringBuffer();
		List<Record> rows=dbTemplate("momoinvbatch.findProcessPerson",Okv.create().set("imodocid",imodocid)).find();
		if(!rows.isEmpty()){
			if(rows.size()==1){
			  Record record1=dbTemplate("momoinvbatch.findLastProcess",Okv.create().set("imodocid",imodocid)).findFirst();
			  if(record1!=null){
				  //比较
				Long personiMoRoutingConfigId=rows.get(0).getLong("imoroutingconfigid");
				Long processiMoRoutingConfigId=record1.getLong("iautoid");
				if(personiMoRoutingConfigId.equals(processiMoRoutingConfigId)){
					stringBuffer.append(rows.get(0).getStr("jobanme"));
				}
			  }
			}else{
				for(Record record:rows){
					Record record1=dbTemplate("momoinvbatch.findLastProcess",Okv.create().set("imodocid",imodocid)).findFirst();
					if(record1!=null) {
						Long personiMoRoutingConfigId = record.getLong("imoroutingconfigid");
						Long processiMoRoutingConfigId = record1.getLong("iautoid");
						if (personiMoRoutingConfigId.equals(processiMoRoutingConfigId)) {
							stringBuffer.append(record.getStr("jobanme"));
							stringBuffer.append(",");
						}
					}
				}
			}
		}
      return stringBuffer;
	}
  public Ret subPrint(SubPrintReqVo subPrintReqVo ){
	  if(subPrintReqVo==null || notOk(subPrintReqVo.getIautoid())) {
		  return fail(JBoltMsg.PARAM_ERROR);
	  }
	  //更新时需要判断数据存在
	  MoMoinvbatch dbMoMoinvbatch=findById(subPrintReqVo.getIautoid());
	  if(dbMoMoinvbatch==null)
	  {return fail(JBoltMsg.DATA_NOT_EXIST);}
	  //查询当前
	  Sql sql=selectSql().select(MoMoinvbatch.IAUTOID,MoMoinvbatch.IPRINTSTATUS).from(table()).
			  eq(MoMoinvbatch.IMODOCID,dbMoMoinvbatch.getIAutoId())
			  .eq(MoMoinvbatch.ISEQ,dbMoMoinvbatch.getISeq()-1);

	  MoMoinvbatch frontMoMoinvbatch=findFirst(sql);
	  if(frontMoMoinvbatch!=null){
		  if(frontMoMoinvbatch.getIPrintStatus().equals(1)){
			  return fail("请按顺序打印");
		  }
	  }
	  ModocResVo modocResVo=getModoc(dbMoMoinvbatch.getIMoDocId());
	  //处理返回打印数据
	  int sum=subPrintReqVo.getIqty().intValue();
	  SubPrintReqVo subPrintReqVo1;
	  List<SubPrintReqVo> subPrintReqVos=new ArrayList<>();
	  for(int i=0;i<sum;i++){
		    subPrintReqVo1=new SubPrintReqVo();
		  subPrintReqVo1.setCbarcode(dbMoMoinvbatch.getCBarcode()+"-"+dbMoMoinvbatch.getCVersion());
		  subPrintReqVo1.setNum("0"+(i+1));
		  subPrintReqVo1.setTotal(sum);
		  subPrintReqVo1.setWorkheader(subPrintReqVo.getWorkheader());
		  subPrintReqVo1.setJobname(subPrintReqVo.getJobname());
		  subPrintReqVo1.setCinvname1(modocResVo.getCinvname1());
		  subPrintReqVo1.setCinvcode1(modocResVo.getCinvcode1());
		  subPrintReqVo1.setCworkname(modocResVo.getCworkname());
		  subPrintReqVo1.setCworkshiftname(modocResVo.getCworkshiftname());
		  subPrintReqVo1.setProduceddate(modocResVo.getProduceddate());
		  subPrintReqVo1.setPlaniqty(modocResVo.getPlaniqty());
		  subPrintReqVo1.setMemo(subPrintReqVo.getMemo());
		  subPrintReqVos.add(subPrintReqVo1);
	  }


   return successWithData(subPrintReqVos);

  }
	public Ret subListPrint(String ids, Long imodocid, String workleader, String jobname) {
        if (StrUtil.isBlank(ids)) {
            return fail("未选中数据");
        }
		if(notOk(imodocid)){
			return  fail("缺少工单ID");
		}
		ModocResVo modocResVo=getModoc(imodocid);
		List<MoMoinvbatch> moMoinvbatches=find("SELECT * FROM Mo_MoInvBatch where iAutoId in "+ Util.getInSqlByIds(ids));
		SubPrintReqVo subPrintReqVo1;
		List<SubPrintReqVo> subPrintReqVos=new ArrayList<>();
		if(!moMoinvbatches.isEmpty()){
			for(MoMoinvbatch moMoinvbatch:moMoinvbatches){
				int sum=moMoinvbatch.getIQty().intValue();
				for(int i=0;i<sum;i++){
					subPrintReqVo1=new SubPrintReqVo();
					subPrintReqVo1.setCbarcode(moMoinvbatch.getCBarcode()+"-"+moMoinvbatch.getCVersion());
					subPrintReqVo1.setNum("0"+(i+1));
					subPrintReqVo1.setTotal(sum);
					subPrintReqVo1.setWorkheader(workleader);
					subPrintReqVo1.setJobname(jobname);
					subPrintReqVo1.setCinvname1(modocResVo.getCinvname1());
					subPrintReqVo1.setCinvcode1(modocResVo.getCinvcode1());
					subPrintReqVo1.setCworkname(modocResVo.getCworkname());
					subPrintReqVo1.setCworkshiftname(modocResVo.getCworkshiftname());
					subPrintReqVo1.setProduceddate(modocResVo.getProduceddate());
					subPrintReqVo1.setPlaniqty(modocResVo.getPlaniqty());

					subPrintReqVos.add(subPrintReqVo1);
				}
			}
		}
		return successWithData(subPrintReqVos);
	}

	/**
	 * 获取工单上的信息
	 * @param imodocid
	 * @return
	 */
	private ModocResVo getModoc(Long imodocid){
		ModocResVo modocResVo=new ModocResVo();
		MoDoc moDoc=moDocService.findById(imodocid);
		if(moDoc.getDPlanDate()!=null){
		modocResVo.setProduceddate(DateUtils.formatDate(moDoc.getDPlanDate(),"yyyy-MM-dd"));
		}
		if(moDoc.getIQty()!=null){
			modocResVo.setPlaniqty(moDoc.getIQty().intValue());
		}

		if(isOk(moDoc.getIInventoryId())){
			//存货
			Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
			if(inventory!=null){


				//客户部番

				modocResVo.setCinvcode1(inventory.getCInvCode1());
				//部品名称
				modocResVo.setCinvcode1(inventory.getCInvName1());



			}
			//工艺路线


		}

		//产线
		if(isOk(moDoc.getIWorkRegionMid())){
			Workregionm workregionm=workregionmService.findById(moDoc.getIWorkRegionMid());
			if(workregionm!=null){
				modocResVo.setCworkname(workregionm.getCWorkName());

			}
		}
		//班次
		if(isOk(moDoc.getIWorkShiftMid())){
			Workshiftm  workshiftm=workshiftmService.findById(moDoc.getIWorkShiftMid());
			if(workshiftm!=null){
				modocResVo.setCworkshiftname(workshiftm.getCworkshiftname());
			}
		}
	   return modocResVo;
	}

   public  Ret printdataList(){
	   SubPrintReqVo subPrintReqVo1;
	   List<SubPrintReqVo> subPrintReqVos=new ArrayList<>();
	   for(int i=0;i<10;i++){
		   subPrintReqVo1=new SubPrintReqVo();
		   subPrintReqVo1.setCbarcode("11111");
		   subPrintReqVo1.setNum("0"+(i+1));
		   subPrintReqVo1.setTotal(10);
		   subPrintReqVo1.setWorkheader("111");
		   subPrintReqVo1.setJobname("jobname");
		   subPrintReqVo1.setCinvname1("ddd");
		   subPrintReqVo1.setCinvcode1("ddd");
		   subPrintReqVo1.setCworkname("ddd");
		   subPrintReqVo1.setCworkshiftname("dddd");
		   subPrintReqVo1.setProduceddate("ddd");
		   subPrintReqVo1.setPlaniqty(10);
		   subPrintReqVo1.setMemo("备注");
		   subPrintReqVos.add(subPrintReqVo1);
	   }
	   return successWithData(subPrintReqVos);
   }

    public Ret updateStatus(Long iautoid, BigDecimal iqty) {
		if(notOk(iautoid)){
			return fail(JBoltMsg.PARAM_ERROR);
		}
		MoMoinvbatch dbMoMoinvbatch=findById(iautoid);
		if(dbMoMoinvbatch==null) {
			return fail(JBoltMsg.DATA_NOT_EXIST);}
		MoDoc moDoc = moDocService.findById(dbMoMoinvbatch.getIMoDocId());
		if (moDoc == null) {
			return fail("工单信息不存在");
		}

		if(notOk(iqty)){
			return fail("缺少数量");
		}
		if(iqty.compareTo(dbMoMoinvbatch.getIQty())==1){
			return fail("不允许超原数量");
		}
		if(iqty.compareTo(dbMoMoinvbatch.getIQty())==-1) {
           BigDecimal a=dbMoMoinvbatch.getIQty().subtract(iqty);
		    int num=a.intValue();
			MoMoinvbatch moMoinvbatch;
			Date now=new Date();


				moMoinvbatch = new MoMoinvbatch();
				moMoinvbatch.setIOrgId(getOrgId());
				moMoinvbatch.setCOrgCode(getOrgCode());
				moMoinvbatch.setCOrgName(getOrgName());
				moMoinvbatch.setIMoDocId(moDoc.getIAutoId());
				//moMoinvbatch.seti
				String barcode = BillNoUtils.genCassiGnOrderNo(getOrgId(), "CP", 7);
				moMoinvbatch.setISeq(1); //X

				moMoinvbatch.setIQty(a);
				moMoinvbatch.setCBarcode(barcode);
				moMoinvbatch.setIPrintStatus(1);
				moMoinvbatch.setIStatus(0);
				moMoinvbatch.setICreateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCCreateName(JBoltUserKit.getUserUserName());
				moMoinvbatch.setDCreateTime(now);
				moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
				moMoinvbatch.setDUpdateTime(now);
				moMoinvbatch.setCVersion("00");//版本号
				moMoinvbatch.setIsEffective(true);
				moMoinvbatch.save();

		}
		return  SUCCESS;
    }
	public Ret updateWork(MoMoinvbatch moMoinvbatch){
		if(moMoinvbatch.getIStatus().equals(1)){
			return  fail("未打印现品票");
		}
		moMoinvbatch.setIStatus(1);
		moMoinvbatch.setDUpdateTime(new Date());
		moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
		moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
		moMoinvbatch.update();
		return  SUCCESS;

	}

	/**
	 * 生成现品票
	 * @param imodocid
	 * @return
	 */
	public Ret createMomoinvbatch(Long imodocid) {
		ValidationUtils.notNull(imodocid, "生产订单主键不允许为空");
		ValidationUtils.isTrue(findByiMoDocId(imodocid).size() == 0, "已生成现品票");
		// 获取工单信息
		MoDoc moDoc = moDocService.findById(imodocid);
		// 获取存货数量
		Inventory inventory = inventoryService.findById(moDoc.getIInventoryId());
		// 计划数量
		Integer iqty = Double.valueOf(Double.parseDouble(moDoc.getIQty().toString())).intValue();
		// 包装数量
		Integer iPkgQty = Optional.ofNullable(inventory.getIPkgQty()).orElse(iqty);
		List<MoMoinvbatch> moMoinvbatches = new ArrayList<>();
		Integer sunNumber = 0;

		for (int i = 1; i * iPkgQty <= iqty; i++) {
			sunNumber +=  iPkgQty;
			MoMoinvbatch moMoinvbatch = new MoMoinvbatch();
			moMoinvbatch.setIOrgId(getOrgId());
			moMoinvbatch.setCOrgCode(getOrgCode());
			moMoinvbatch.setCOrgName(getOrgName());
			moMoinvbatch.setIMoDocId(imodocid);
			moMoinvbatch.setIInventoryId(inventory.getIAutoId());
			moMoinvbatch.setIPkgQty(iPkgQty);
			moMoinvbatch.setISeq(i);
			moMoinvbatch.setCVersion("00");
			moMoinvbatch.setCBarcode(BillNoUtils.genRoutingReportNo());
			if (sunNumber + iPkgQty > iqty) {
				moMoinvbatch.setIQty(new BigDecimal(iqty - sunNumber));
			} else {
				moMoinvbatch.setIQty(new BigDecimal(iPkgQty.toString()));
			}
			moMoinvbatch.setIPrintStatus(1);
			moMoinvbatch.setIStatus(0);
			moMoinvbatch.setIsEffective(true);
			moMoinvbatch.setICreateBy(JBoltUserKit.getUserId());
			moMoinvbatch.setCCreateName(JBoltUserKit.getUserName());
			moMoinvbatch.setDCreateTime(new Date());
			moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
			moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
			moMoinvbatch.setDUpdateTime(new Date());
			moMoinvbatch.setIsDeleted(false);
			moMoinvbatch.setCCompleteBarcode(moMoinvbatch.getCBarcode() + "-" + moMoinvbatch.getCVersion());
			moMoinvbatches.add(moMoinvbatch);
		}

		batchSave(moMoinvbatches);
		return SUCCESS;
	}

	public List<MoMoinvbatch> findByiMoDocId(Long imodocid) {
		return find(selectSql().eq("iMoDocId", imodocid));
	}

	/**
	 * 批量报工
	 *
	 * @param imodocid
	 * @param ids
	 * @return
	 */
	public Ret workByIds(String imodocid, String ids) {
		ValidationUtils.notNull(ids, "参数错误,无法打印!");
		ValidationUtils.notNull(imodocid, "参数错误,无法打印!");
		tx(() -> {
			List<MoMoinvbatch> moMoinvbatches = getListByIds(ids);
			MoDoc moDoc = moDocService.findById(imodocid);
			BigDecimal compQty = BigDecimal.ZERO;
			for (MoMoinvbatch moMoinvbatch : moMoinvbatches) {
				moMoinvbatch.setIStatus(1);
				compQty = compQty.add(moMoinvbatch.getIQty());
			}

			moDoc.setICompQty(compQty.add(Optional.ofNullable(moDoc.getICompQty()).orElse(BigDecimal.ZERO)));
			ValidationUtils.isTrue(moDoc.update(),"更新制造工单信息失败");
			ValidationUtils.isTrue(batchUpdate(moMoinvbatches).length>0,"更新现品票信息失败");
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 撤回
	 *
	 * @param iautoid
	 * @return
	 */
	public Ret withdraw(Long iautoid) {
		tx(()->{
			MoMoinvbatch moinvbatch = findById(iautoid);
			moinvbatch.setIPrintStatus(1);
			// 已报工状态撤回要删除制造工单完工数量
			if(moinvbatch.getIStatus() == 1)
			{
				MoDoc moDoc = moDocService.findById(moinvbatch.getIMoDocId());
				moDoc.setICompQty(moDoc.getICompQty().subtract(moinvbatch.getIQty()));
				moinvbatch.setIStatus(0);
				ValidationUtils.isTrue(moDoc.update(), "更新制造工单信息失败");
			}
			ValidationUtils.isTrue(moinvbatch.update(), "处理失败,无法打印!");
			return true;
		});
		return SUCCESS;
	}

	public Ret updateNumber(Long iautoid, BigDecimal newQty) {
		tx(()-> {
			MoMoinvbatch moinvbatch = findById(iautoid);
			// 获取最大序列号
			Integer maxSeq = getMaxSeqByMoDocId(moinvbatch.getIMoDocId());
			BigDecimal oldQty = moinvbatch.getIQty();
			// 修改原对象
			moinvbatch.setIQty(newQty);
			Integer version = Integer.valueOf(moinvbatch.getCVersion());
			Integer newVersion = version + 1;
			moinvbatch.setCVersion(newVersion >= 10 ? newVersion.toString() : "0" + newVersion);
			moinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
			moinvbatch.setCUpdateName(JBoltUserKit.getUserName());
			moinvbatch.setDUpdateTime(new Date());
			ValidationUtils.isTrue(moinvbatch.update(), "修改原现品票失败");

			// 生成新对象
			MoMoinvbatch newMoinvbatch = new MoMoinvbatch();
			newMoinvbatch.put(moinvbatch);
			newMoinvbatch.setIAutoId(null);
			newMoinvbatch.setIQty(oldQty.subtract(newQty));
			newMoinvbatch.setISeq(maxSeq + 1);
			newMoinvbatch.setCVersion("00");
			newMoinvbatch.setCBarcode(BillNoUtils.genRoutingReportNo());
			newMoinvbatch.setCCompleteBarcode(newMoinvbatch.getCBarcode() + newMoinvbatch.getCVersion());
			ValidationUtils.isTrue(newMoinvbatch.save(), "生成新现品票失败");
			return true;
		});

		return SUCCESS;
	}

	/**
	 * 获取生产订单最大序号
	 * @param iMoDocId
	 * @return
	 */
	private Integer getMaxSeqByMoDocId(Long iMoDocId) {
		return queryColumn(selectSql().max("iSeq").eq("iMoDocId", iMoDocId));
	}

	/**
	 * 批量打印
	 *
	 * @param imodocid
	 * @param ids
	 * @return
	 */
	public Ret batchPrint(String imodocid, String ids) {
		ValidationUtils.notNull(ids, "参数错误,无法打印!");
		ValidationUtils.notNull(imodocid, "参数错误,无法打印!");
		tx(() -> {
			Boolean printWorkOrderSpotTicketsInAdvance = JBoltGlobalConfigCache.me.getBooleanConfigValue("print_work_order_spot_tickets_in_advance");
			List<MoMoinvbatch> moinvbatches = getListByIds(ids);
			MoDoc moDoc = moDocService.findById(imodocid);
			BigDecimal compQty = BigDecimal.ZERO;
			for (MoMoinvbatch moMoinvbatch:moinvbatches) {
				moMoinvbatch.setIPrintStatus(2);
				moMoinvbatch.setIUpdateBy(JBoltUserKit.getUserId());
				moMoinvbatch.setCUpdateName(JBoltUserKit.getUserName());
				moMoinvbatch.setDUpdateTime(new Date());
				if (!printWorkOrderSpotTicketsInAdvance) {
					moMoinvbatch.setIStatus(1);
					compQty = compQty.add(moMoinvbatch.getIQty());
				}
			}
			moDoc.setICompQty(compQty.add(Optional.ofNullable(moDoc.getICompQty()).orElse(BigDecimal.ZERO)));
			ValidationUtils.isTrue(moDoc.update(),"更新制造工单信息失败");
			ValidationUtils.isTrue(batchUpdate(moinvbatches).length > 0, "处理失败,无法打印!");
			return true;
		});
		return SUCCESS;
	}
}