package cn.rjtech.admin.currentstock;

import cn.jbolt._admin.user.UserService;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;

import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltDateUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.stockcheckvouchbarcode.StockCheckVouchBarcodeService;
import cn.rjtech.admin.stockcheckvouchdetail.StockCheckVouchDetailService;
import cn.rjtech.admin.stockchekvouch.StockChekVouchService;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import cn.rjtech.wms.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

/**
 * 盘点单Service
 * @ClassName: CurrentStockService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2021-11-12 16:01
 */
public class CurrentStockService  extends BaseService<StockCheckVouch> implements IApprovalService {

	private final StockCheckVouch dao = new StockCheckVouch().dao();

	@Override
	protected StockCheckVouch dao() {
		return dao;
	}

	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	Logger log = Logger.getLogger("CurrentStockService");
	@Inject
	StockChekVouchService stockChekVouchService; //盘点单主表
	@Inject
	StockCheckVouchDetailService stockCheckVouchDetailService;//T_Sys_StockCheckVouchDetail(盘点明细表)
	@Inject
	StockCheckVouchBarcodeService stockCheckVouchBarcodeService;//T_Sys_StockCheckVouchBarcode(库存盘点-条码明细)


	@Inject
	UserService userService;




	public Page<Record> datas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		Page<Record> paginate = dbTemplate("currentstock.datas", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	/**
	 * 应盘料品,已盘料品,未盘料品,盘盈料品,盘亏料品.实时计算
	 * */
	public Page<Record> datas_calculate(Integer pageNumber, Integer pageSize, Kv kv) {
		Page<Record> paginate = datas(pageNumber, pageSize, kv);
		List<Record> list = paginate.getList();
		String sqlAutoid="";
		for (Record record : list) {
			Long autoid = record.getLong("autoid");
			sqlAutoid+="'"+autoid+"',";
		}
		sqlAutoid=sqlAutoid.substring(0,sqlAutoid.length()-1);
		//物料清单
		List<Record> invList = invDatasByIds(sqlAutoid);


		return paginate;
	}


	public Page<Record> getdatas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		Page<Record> paginate = dbTemplate("currentstock.getdatas", kv).paginate(pageNumber, pageSize);
		return paginate;
	}


	public Page<Record> CurrentStockByDatas(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		Page<Record> paginate = dbTemplate("currentstock.CurrentStockByDatas", kv).paginate(pageNumber, pageSize);
		return paginate;
	}

	/**
	 * 盘点单新增时候复制细表数据
	 * */
	public List<Record> CurrentStockd(String whcode,String poscodeSql) {
		Kv kv = Kv.by("organizecode", getOrgCode()).set("whcode",whcode).set("poscodeSql",poscodeSql);
		List<Record> list = dbTemplate("currentstock.CurrentStockd", kv).find();
		return list;
	}

	/**
	 * 盘点条码 明细
	 * @param kv
	 * @return
	 */
	public List<Record> getStockCheckVouchBarcodeLines(Kv kv){
		return dbTemplate("currentstock.getStockCheckVouchBarcodeLines",kv).find();
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public Record barcode(Kv kv) {
////		先查询条码是否已添加
		Record first = dbTemplate("currentstock.barcodeDatas", kv).findFirst();
		if(null == first){
			ValidationUtils.isTrue( false,"条码为：" + kv.getStr("barcode") + "该现品票没有库存！！！");
		}
		return first;
	}


	/**
	 * 盘点单物料清单列表
	 * */
	public List<Record> invDatas(Kv kv) {
		String isApp = kv.getStr("isapp");
		kv.set("orgcode",getOrgCode());

		//多个库区处理
		String poscodes = kv.getStr("poscodes");
		String[] split = poscodes.split(",");
		String str="";
		for (int i = 0; i < split.length; i++) {
			if(i==split.length-1){
				str =str+"'" + split[i] +"'";
				break;
			}
			str = str+"'" + split[i] +"',";
		}
		kv.setIfNotNull("poscode",str);
		List<Record> list= new ArrayList<>();

			list= invTotalDatas(kv);
		return list;
	}






	/**
	 * 盘点单物料清单列表
	 * */
	public List<Record> invTotalDatas(Kv kv) {
		List<Record> list = dbTemplate("currentstock.paginateAdminDatas", kv).find();
		return list;
	}



	/**
	 * 盘点单物料清单列表
	 * */
	public Page<Record> barcodeDatas(Integer pageNumber, Integer pageSize, Kv kv) {
		//是app 还是查询全部
		String isapp2 = kv.getStr("isapp2");
		Page<Record> page =null;
		if(isapp2.equals("1")){
			page = barcodeDatas_APP(pageNumber, pageSize, kv);
		}else{
			//查询全部
			page = barcodeDatas_total(pageNumber, pageSize, kv);
		}
		return page;
	}



	/**
	 * 盘点单物料清单列表
	 * */
	public Page<Record> barcodeDatas_APP(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		String whcode = kv.getStr("whcode");
		ValidationUtils.notNull(whcode,"仓库为空!");
		Long stockcheckvouchdid = kv.getLong("stockcheckvouchdid");
		StockCheckVouchDetail dbStockcheckvouchdetail = stockCheckVouchDetailService.findById(stockcheckvouchdid);
		if(dbStockcheckvouchdetail!=null){

			Page<Record> paginate = dbTemplate("currentstock.barcodeDatas", kv).paginate(pageNumber, pageSize);
			List<Record> list = paginate.getList();

			for (Record record : list) {
				//数量
				BigDecimal qty = record.getBigDecimal("qty");
				//实盘数量=数量+调整数量
				BigDecimal realqty = record.getBigDecimal("realqty");
				//调整数量=实盘数量-数量
				BigDecimal adjustqty = record.getBigDecimal("adjustqty");
				BigDecimal adjustqty2 = BigDecimal.ZERO;


				if(adjustqty!=null){
					//如果调整数量为空则
					record.set("realqty2",qty.add(adjustqty));
					adjustqty2=adjustqty;
				}else {

					adjustqty2=realqty.subtract(qty);
					record.set("realqty2",realqty);
				}
				record.set("adjustqty2",adjustqty2);
				record.set("source","APP");
			}



			return paginate;
		}else {
			return null;
		}

	}



	/**
	 * 盘点单物料清单列表
	 * */
	public Page<Record> barcodeDatas_total(Integer pageNumber, Integer pageSize, Kv kv) {
		kv.set("organizecode",getOrgCode());
		String whcode = kv.getStr("whcode");
		ValidationUtils.notNull(whcode,"仓库为空!");
		Long stockcheckvouchdid = kv.getLong("stockcheckvouchdid");
		StockCheckVouchDetail dbStockcheckvouchdetail = stockCheckVouchDetailService.findById(stockcheckvouchdid);
		if(dbStockcheckvouchdetail!=null){
			String invCode = dbStockcheckvouchdetail.getInvCode();
			String posCode = dbStockcheckvouchdetail.getPosCode();

			kv.set("invcode",invCode);
			kv.set("poscode",posCode);

			Page<Record> paginate = dbTemplate("currentstock.barcodeDatas_total", kv).paginate(pageNumber, pageSize);
			List<Record> list = paginate.getList();

			for (Record record : list) {
				String source = record.getStr("source");
				if(source.equals("app")){
					//数量
					BigDecimal qty = record.getBigDecimal("qty");
					//实盘数量=数量+调整数量
					BigDecimal realqty = record.getBigDecimal("realqty");
					//调整数量=实盘数量-数量
					BigDecimal adjustqty = record.getBigDecimal("adjustqty");
					BigDecimal adjustqty2 = BigDecimal.ZERO;


					if(adjustqty!=null){
						//如果调整数量为空则
						record.set("realqty2",qty.add(adjustqty));
						adjustqty2=adjustqty;
					}else {

						adjustqty2=realqty.subtract(qty);
						record.set("realqty2",realqty);
					}
					record.set("adjustqty2",adjustqty2);
				}else{
					//web 端
					String sourceid = record.getStr("sourceid");
					if(sourceid!=null){
						//数量
						BigDecimal qty = record.getBigDecimal("qty");
						//实盘数量=数量+调整数量
						BigDecimal realqty = record.getBigDecimal("realqty");
						//调整数量=实盘数量-数量
						BigDecimal adjustqty = record.getBigDecimal("adjustqty");
						BigDecimal adjustqty2 = BigDecimal.ZERO;


						if(adjustqty!=null){
							//如果调整数量为空则
							record.set("realqty2",qty.add(adjustqty));
							adjustqty2=adjustqty;
						}else {

							adjustqty2=realqty.subtract(qty);
							record.set("realqty2",realqty);
						}
						record.set("adjustqty2",adjustqty2);
					}
				}
			}



			return paginate;
		}else {
			return null;
		}

	}




	/**仓库*/
	public List<Record> autocompleteWareHouse(Kv kv) {
		return dbTemplate("currentstock.autocompleteWareHouse", kv).find();
	}
	/**库位*/
	public List<Record> autocompletePosition(Kv kv) {
		return dbTemplate("currentstock.autocompletePosition", kv).find();
	}
	/**盘点人*/
	public List<Record> autocompleteUser(Kv kv) {
		return dbTemplate("currentstock.autocompleteUser",kv).find();
	}


	public Ret SaveStockchekvouch(Kv kv){
		//主表
		StockCheckVouch stockchekvouch = new StockCheckVouch();
		//构造数据
		Date date = new Date();
		//创建时间
		stockchekvouch.setDCreateTime(date);
		String dateStr = JBoltDateUtil.format(date, "yyyy-MM-dd");
		stockchekvouch.setBillDate(dateStr);
		String userName = JBoltUserKit.getUserName();
		//创建人
		stockchekvouch.setCCreateName(userName);
		stockchekvouch.setICreateBy(JBoltUserKit.getUserId());
		String orgCode = getOrgCode();
		//组织编码
		stockchekvouch.setOrganizeCode(orgCode);
		String code = BillNoUtils.genCurrentNo();
		//单号
		stockchekvouch.setBillNo(code);
		//获取数据
		String whcode = kv.getStr("whcode");
		//盘点仓库
		stockchekvouch.setWhCode(whcode);
		//盘点人
		stockchekvouch.setCheckPerson(kv.getStr("id"));
		stockchekvouch.setCheckType(kv.getStr("isenabled"));
		kv.getStr("poscode");

		boolean save = stockchekvouch.save();
		return ret(save);
	}

	/**
	 * 保存
	 */
	public Ret save(StockCheckVouch stockchekvouch) {
		if (stockchekvouch == null || isOk(stockchekvouch.getAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		tx(() -> {
			// ValidationUtils.isTrue(notExists(columnName, value), JBoltMsg.DATA_SAME_NAME_EXIST);
			stockchekvouch.setOrganizeCode(getOrgCode());
			stockchekvouch.setBillNo(BillNoUtils.genCurrentNo());
			stockchekvouch.setDCreateTime(new Date());
			ValidationUtils.isTrue(stockchekvouch.save(), ErrorMsg.SAVE_FAILED);


			// TODO 其他业务代码实现

			return true;
		});
		return SUCCESS;
	}
	public Ret saveSubmit(Kv kv){
		String datas = kv.getStr("datas");
		String barcode = kv.getStr("barcode");
		List<Kv> datasList = JSON.parseArray(datas, Kv.class);
		List<Kv> barcodeList = JSON.parseArray(barcode, Kv.class);
		return SUCCESS;

	}

	/**
	 * 盘点单
	 * */
	public Ret save2(StockCheckVouch stockcheckvouch){
		final Long[] AutoIDs = {null};
		tx(() -> {
			Date now=new Date();
			String userName = JBoltUserKit.getUserName();
			stockcheckvouch.setBillDate(JBoltDateUtil.format(now,"yyyy-MM-dd"));
			stockcheckvouch.setIAuditStatus(0);
			//创建人
			stockcheckvouch.setCCreateName( JBoltUserKit.getUserName());
			stockcheckvouch.setICreateBy(JBoltUserKit.getUserId());
			stockcheckvouch.setDCreateTime(now);
			String orgCode = getOrgCode()+"";
			//组织编码
			stockcheckvouch.setOrganizeCode(orgCode);
			String code = BillNoUtils.genCurrentNo();
			//单号
			stockcheckvouch.setBillNo(code);
			ValidationUtils.isTrue(stockcheckvouch.save(),"主表保存失败!");

			AutoIDs[0] = stockcheckvouch.getAutoId();
//
//
//			String poscodes = stockcheckvouch.getPoscodes();
//
//			String poscodeSql="";
//			if(poscodes!=null){
//				String[] split = poscodes.split(",");
//				for (String poscode : split) {
//					poscodeSql+="'"+poscode+"',";
//				}
//				if(poscodeSql.length()>0){
//					poscodeSql=poscodeSql.substring(0,poscodeSql.length()-1);
//				}
//			}
//
//			if(StringUtils.isEmpty(poscodeSql)){
//				poscodeSql=null;
//			}
//			String whCode = stockcheckvouch.getWhCode();
//			List<Record> list = CurrentStockd(whCode, poscodeSql);
//			for (Record record : list) {
//				String poscode = record.getStr("poscode");
//				StockCheckVouchDetail stockcheckvouchdetail=new StockCheckVouchDetail();
//				stockcheckvouchdetail.put(record);
//				stockcheckvouchdetail.setMasID(Long.valueOf(String.valueOf(mid)));
//				stockcheckvouchdetail.setCreateDate(now);
//				stockcheckvouchdetail.setCreatePerson(userName);
//				stockcheckvouchdetail.setPosCode(poscode);
//				BigDecimal num = stockcheckvouchdetail.getNum();
//				if(num==null){
//					stockcheckvouchdetail.setNum(BigDecimal.ZERO);
//				}
//				ValidationUtils.isTrue(stockcheckvouchdetail.save(),"细表保存失败!");
//			}
			return true;
		});
		return SUCCESS.set("AutoID", AutoIDs[0]);
	}

	/**
	 * 盘点单新增
	 * */
	public Ret jboltTableSubmit(JBoltTable jBoltTable){
		tx(() -> {
			JSONObject form = jBoltTable.getForm();
			StockCheckVouch stockcheckvouch=new StockCheckVouch();
			stockcheckvouch.setCheckType(form.getString("checktype"));
			stockcheckvouch.setWhCode(form.getString("whcode"));
			//构造数据
			Date date = new Date();
			//创建时间
			stockcheckvouch.setDCreateTime(date);
			String dateStr = JBoltDateUtil.format(date, "yyyy-MM-dd");
			stockcheckvouch.setBillDate(dateStr);
			String userName = JBoltUserKit.getUserName();
			//创建人
			stockcheckvouch.setCCreateName(userName);
			stockcheckvouch.setICreateBy(JBoltUserKit.getUserId());
			String orgId = getOrgId()+"";
			//组织编码
			stockcheckvouch.setOrganizeCode(orgId);
			String code = BillNoUtils.genCurrentNo();
			//单号
			stockcheckvouch.setBillNo(code);
			//盘点人
			stockcheckvouch.setCheckPerson(userName);
			ValidationUtils.isTrue(stockcheckvouch.save(),"主表保存失败!");

			Long autoId = stockcheckvouch.getAutoId();

			List<Record> saveRecordList = jBoltTable.getSaveRecordList();
			for (Record record : saveRecordList) {
				StockCheckVouchDetail stockcheckvouchdetail = new StockCheckVouchDetail();
				stockcheckvouchdetail.setMasID(Long.valueOf(String.valueOf(autoId)));
				//库位
				String poscode = record.getStr("poscode");
				stockcheckvouchdetail.setPosCode(poscode);
				//存货编码
				String invcode = record.getStr("invcode");
				stockcheckvouchdetail.setInvCode(invcode);
				//数量
				BigDecimal qty = record.getBigDecimal("qty");
				stockcheckvouchdetail.setQty(qty);
				//件数
				BigDecimal num = record.getBigDecimal("num");
				stockcheckvouchdetail.setNum(num);
				//创建人
				stockcheckvouchdetail.setCreatePerson(userName);
				//创建时间
				stockcheckvouchdetail.setCreateDate(date);

				ValidationUtils.isTrue(stockcheckvouchdetail.save(),"细表保存失败!");

			}



			return true;
		});

		return SUCCESS;

	}

	/**
	 * 批量查找盘点物料
	 * */
	public List<Record> invDatasByIds(String ids) {
		return dbTemplate("currentstock.invDatasByIds", Kv.by("ids", ids).set("orgcode",getOrgCode())).find();
	}

	/**
	 * 处理审批通过的其他业务操作，如有异常返回错误信息，待审核->审核通过
	 * @param formAutoId    单据ID
	 * @param isWithinBatch 是否为批量处理
	 * @return
	 */
	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		StockCheckVouch stockCheckVouch = stockChekVouchService.findById(formAutoId);
		//1、TODO 盘盈要推其它入库单

		//2、TODO 盘亏要推其它出库单
		return "SUCCESS";
	}

	@Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	/**
	 * 提审前业务，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @return 错误信息
	 */
	@Override
	public String preSubmitFunc(long formAutoId) {
		return null;
	}

	/**
	 * 提审后业务处理，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @return 错误信息
	 */
	@Override
	public String postSubmitFunc(long formAutoId) {
		return null;
	}

	/**
	 * 撤回审核业务处理，如有异常返回错误信息
	 *
	 * @param formAutoId 单据ID
	 * @return 错误信息
	 */
	@Override
	public String postWithdrawFunc(long formAutoId) {
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		return null;
	}

	@Override
	public String preWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	@Override
	public String postWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		return null;
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		return null;
	}

	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return null;
	}

}