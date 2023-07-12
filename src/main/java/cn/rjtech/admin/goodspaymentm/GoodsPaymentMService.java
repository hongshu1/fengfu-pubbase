package cn.rjtech.admin.goodspaymentm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.inventory.InventoryService;
import cn.rjtech.admin.weekorderd.WeekOrderDService;
import cn.rjtech.admin.weekorderm.WeekOrderMService;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.enums.WeekOrderStatusEnum;
import cn.rjtech.model.momdata.*;
import cn.rjtech.service.approval.IApprovalService;
import cn.rjtech.util.BillNoUtils;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 货款核对表
 * @ClassName: GoodsPaymentMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-30 00:06
 */
public class GoodsPaymentMService extends BaseService<GoodsPaymentM> implements IApprovalService {

    private final GoodsPaymentM dao = new GoodsPaymentM().dao();

    @Override
    protected GoodsPaymentM dao() {
        return dao;
    }

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private InventoryService inventoryservice;
    @Inject
	private WeekOrderDService weekorderdservice;
    @Inject
	private WeekOrderMService weekordermservice;
    @Inject
    private GoodsPaymentDService goodspaymentdservice;
    @Inject
    private GoodsPaymentDService goodsPaymentDservice;

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
        return dbTemplate("goodspaymentm.recpor", kv).paginate(pageNumber, pageSize);
	}

	/**
	 * 保存
	 */
	public Ret save(GoodsPaymentM goodsPaymentM) {
		if(goodsPaymentM==null || isOk(goodsPaymentM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(goodsPaymentM.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=goodsPaymentM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(goodsPaymentM.getIAutoId(), JBoltUserKit.getUserId(), goodsPaymentM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(GoodsPaymentM goodsPaymentM) {
		if(goodsPaymentM==null || notOk(goodsPaymentM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		GoodsPaymentM dbGoodsPaymentM=findById(goodsPaymentM.getIAutoId());
		if(dbGoodsPaymentM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(goodsPaymentM.getName(), goodsPaymentM.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=goodsPaymentM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(goodsPaymentM.getIAutoId(), JBoltUserKit.getUserId(), goodsPaymentM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param goodsPaymentM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(GoodsPaymentM goodsPaymentM, Kv kv) {
		//addDeleteSystemLog(goodsPaymentM.getIAutoId(), JBoltUserKit.getUserId(),goodsPaymentM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param goodsPaymentM model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(GoodsPaymentM goodsPaymentM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(GoodsPaymentM goodsPaymentM, String column, Kv kv) {
		//addUpdateSystemLog(goodsPaymentM.getIAutoId(), JBoltUserKit.getUserId(), goodsPaymentM.getName(),"的字段["+column+"]值:"+goodsPaymentM.get(column));
		/*
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			String[] split = ids.split(",");
			for(String s : split){
				updateColumn(s, "isdeleted", true);
				update("update SM_GoodsPaymentD  set  IsDeleted = 1 where  iGoodsPaymentMid = ?",s);
			}
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		tx(() -> {
			updateColumn(id, "isdeleted", true);
			update("update SM_GoodsPaymentD  set  IsDeleted = 1 where  iGoodsPaymentMid = ?",id);
			return true;
		});
		return SUCCESS;
	}

	/**
	 * 读取excel文件
	 */
	public Ret importExcel(File file, String cformatName) {
		//使用字段配置维护
        List<Record> importData = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
        ValidationUtils.notNull(importData, "导入数据不能为空");
        
		//执行批量操作
		boolean success=tx(() -> {
//				goodsPaymentDservice.batchSave(goodsPaymentDS);
            return true;
        });

		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
        return SUCCESS;
	}

	/**
	 * 执行JBoltTable表格整体提交
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		GoodsPaymentM goodspaymentm = jBoltTable.getFormModel(GoodsPaymentM.class,"goodspaymentm");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(goodspaymentm.getIAutoId() == null){
				goodspaymentm.setCOrgCode(getOrgCode());
				goodspaymentm.setCOrgName(getOrgName());
				goodspaymentm.setCGoodsPaymentNo(BillNoUtils.genCode(getOrgCode(), table()));
				goodspaymentm.setICreateBy(user.getId());
				goodspaymentm.setCCreateName(user.getName());
				goodspaymentm.setDCreateTime(now);
				goodspaymentm.setIUpdateBy(user.getId());
				goodspaymentm.setCUpdateName(user.getName());
				goodspaymentm.setDUpdateTime(now);
				goodspaymentm.setIsDeleted(false);
				//主表新增
				ValidationUtils.isTrue(goodspaymentm.save(), ErrorMsg.SAVE_FAILED);
			}else{
				goodspaymentm.setIUpdateBy(user.getId());
				goodspaymentm.setCUpdateName(user.getName());
				goodspaymentm.setDUpdateTime(now);
				//主表修改
				ValidationUtils.isTrue(goodspaymentm.update(), ErrorMsg.UPDATE_FAILED);
			}
			//从表的操作
			// 获取保存数据（执行保存，通过 getSaveRecordList）
			saveTableSubmitDatas(jBoltTable,goodspaymentm);
			//获取修改数据（执行修改，通过 getUpdateRecordList）
			updateTableSubmitDatas(jBoltTable,goodspaymentm);
			//获取删除数据（执行删除，通过 getDelete）
			deleteTableSubmitDatas(jBoltTable);
			return true;
		});
		return successWithData(goodspaymentm.keep("autoid"));
	}

	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,GoodsPaymentM rcvplanm){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		User user = JBoltUserKit.getUser();
		ArrayList<GoodsPaymentD> goodspaymentd = new ArrayList<>();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			GoodsPaymentD goodsPaymentD = new GoodsPaymentD();
			goodsPaymentD.setIGoodsPaymentMid(rcvplanm.getIAutoId());
			//通过存货编码去查询存货id
			ValidationUtils.notNull(row.getStr("cinvcode"),"存货编码不能为空");
			Inventory cinvcode = inventoryservice.findFirst("select * from Bd_Inventory where cInvCode = ?", row.getStr("cinvcode"));
			ValidationUtils.notNull(cinvcode,"存货编码:" +row.getStr("cinvcode")+",查不到对应的id");
			goodsPaymentD.setIInventoryId(cinvcode.getIAutoId().toString());
			goodsPaymentD.setCBarcode(row.getStr("cbarcode"));
			goodsPaymentD.setCVersion(row.getStr("cversion"));
			goodsPaymentD.setIQty(row.getBigDecimal("iqty"));
			goodsPaymentD.setDAccountingTime(row.getDate("daccountingtime"));
			goodsPaymentD.setDWarehousingTime(row.getDate("dwarehousingtime"));
			goodsPaymentD.setIsDeleted(false);
			goodsPaymentD.setICreateBy(user.getId());
			goodsPaymentD.setCCreateName(user.getName());
			goodsPaymentD.setDCreateTime(now);
			goodsPaymentD.setIUpdateBy(user.getId());
			goodsPaymentD.setCUpdateName(user.getName());
			goodsPaymentD.setDUpdateTime(now);

			goodspaymentd.add(goodsPaymentD);
		}
		goodsPaymentDservice.batchSave(goodspaymentd);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,GoodsPaymentM rcvplanm){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		User user = JBoltUserKit.getUser();
		ArrayList<GoodsPaymentD> goodspaymentd = new ArrayList<>();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			GoodsPaymentD goodsPaymentD = new GoodsPaymentD();
			goodsPaymentD.setIGoodsPaymentMid(rcvplanm.getIAutoId());
			goodsPaymentD.setIInventoryId(row.getStr("iinventoryid"));
			goodsPaymentD.setCBarcode(row.getStr("cbarcode"));
			goodsPaymentD.setCVersion(row.getStr("cversion"));
			goodsPaymentD.setIQty(row.getBigDecimal("iqty"));
			goodsPaymentD.setDAccountingTime(row.getDate("daccountingtime"));
			goodsPaymentD.setDWarehousingTime(row.getDate("dwarehousingtime"));
			goodsPaymentD.setIsDeleted(false);
			goodsPaymentD.setIUpdateBy(user.getId());
			goodsPaymentD.setCUpdateName(user.getName());
			goodsPaymentD.setDUpdateTime(now);

			goodspaymentd.add(goodsPaymentD);
		}
		goodsPaymentDservice.batchUpdate(goodspaymentd);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			goodsPaymentDservice.deleteById(id);
		}
	}

	/**
	 * 获取条码列表
	 * 通过关键字匹配
	 * autocomplete组件使用
	 */
	public List<Record> getBarcodeDatas(String q, Integer limit, String orgCode, String iCustomerId1) {
		return dbTemplate("goodspaymentm.getBarcodeDatas", Kv.by("q", q).set("limit", limit).set("orgCode", orgCode).set("icustomerid", iCustomerId1)).find();
	}

	/**
	 * todo  审核通过
	 */
	@Override
	public String postApproveFunc(long formAutoId, boolean isWithinBatch) {
		return this.passagetwo(formAutoId);
	}

	@Override
	public String postRejectFunc(long formAutoId, boolean isWithinBatch) {
		return null;
	}

	@Override
	public String preReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		return null;
	}

	/**
	 * todo 实现反审之后的其他业务操作, 如有异常返回错误信息
	 */
	@Override
	public String postReverseApproveFunc(long formAutoId, boolean isFirst, boolean isLast) {
		if(isLast){
			this.delectbelowtwo(formAutoId);
		}
		return null;
	}

	@Override
	public String preSubmitFunc(long formAutoId) {
		return null;
	}

	@Override
	public String postSubmitFunc(long formAutoId) {
		return null;
	}

	@Override
	public String postWithdrawFunc(long formAutoId) {
		return null;
	}

	@Override
	public String withdrawFromAuditting(long formAutoId) {
		return null;
	}

	/**
	 *  todo 从已审核，撤回到已保存，前置业务实现，如有异常返回错误信息
	 */
	@Override
	public String preWithdrawFromAuditted(long formAutoId) {
		return null;
	}

	/**
	 * todo 从已审核，撤回到已保存，业务实现，如有异常返回错误信息
	 */
	@Override
	public String postWithdrawFromAuditted(long formAutoId) {
		return this.delectbelowtwo(formAutoId);
	}


	/**
	 * todo 批量审核（审批）通过，后置业务实现
	 */
	@Override
	public String postBatchApprove(List<Long> formAutoIds) {
		return this.passagetwo(formAutoIds);
	}

	@Override
	public String postBatchReject(List<Long> formAutoIds) {
		return null;
	}

	/**
	 * todo 批量撤销审批，后置业务实现，需要做业务出来
	 */
	@Override
	public String postBatchBackout(List<Long> formAutoIds) {
		return this.delectbelowtwo(formAutoIds);
	}

	//审核通过后
	public String passagetwo(long formAutoId){
		//查出所有的从表数据，给遍历使用
		List<GoodsPaymentD> getall = goodspaymentdservice.getall();
		//根据主表id 查出从表所以数据
		List<GoodsPaymentD> datas = goodspaymentdservice.findDatas(formAutoId);
		if(CollectionUtil.isEmpty(datas))return "";
		for (GoodsPaymentD s : datas){
			//根据以上字段去查询周间客户从表;存货id,传票号,版本号
			WeekOrderD weekOrderD = weekorderdservice.getWeekOrderD(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
			if(Objects.nonNull(weekOrderD)){
				//获取此单据的所有从表数据
				List<WeekOrderD> weekOrderDdatas = weekorderdservice.getWeekOrderDdatas(weekOrderD.getIWeekOrderMid());
				if(CollectionUtil.isNotEmpty(weekOrderDdatas)){
					//todo 产品设计不合理，所以代码也就这样循环判断(数据量上来直接oom)；查出此条周间客户从表数据，判断货款核对从表是否全部包含周间客户从表数据，包含则修改周间客户主表 字段
					for ( WeekOrderD d : weekOrderDdatas){
						for (GoodsPaymentD gd : getall){
							if(!d.getIInventoryId().equals(gd.getIInventoryId()) || !d.getCCode().equals(gd.getCBarcode()) || !d.getCVersion().equals(gd.getCVersion())){
								break;
							}
						}
						//查询周间客户主表
						WeekOrderM byId = weekordermservice.findById(d.getIWeekOrderMid());
						byId.setIOrderStatus(WeekOrderStatusEnum.OK.getValue());
						weekordermservice.updateOneColumn(byId);
					}
				}
			}
		}

		return "";
	}

	//反审后的操作
	public String delectbelowtwo(long formAutoId){
		//根据主表id 查出从表所以数据
		List<GoodsPaymentD> datas = goodspaymentdservice.findDatas(formAutoId);
		if(CollectionUtil.isEmpty(datas))return "";
		for (GoodsPaymentD s : datas){
			//根据以上字段去查询周间客户从表;存货id,传票号,版本号
			WeekOrderD weekOrderD = weekorderdservice.getWeekOrderD(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
			if(Objects.isNull(weekOrderD)) break;
			WeekOrderD weekOrderDIOrderStatus = weekorderdservice.getWeekOrderDIOrderStatus(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
			if(Objects.isNull(weekOrderDIOrderStatus)) break;
			WeekOrderM byId = weekordermservice.findById(weekOrderDIOrderStatus.getIWeekOrderMid());
			byId.setIOrderStatus(WeekOrderStatusEnum.DELIVERIED.getValue());
			weekordermservice.updateOneColumn(byId);
		}
		return "";
	}

	//批量审核通过后
	public String passagetwo(List<Long> formAutoId){
		//查出所有的从表数据，给遍历使用
		List<GoodsPaymentD> getall = goodspaymentdservice.getall();
		//根据主表id 查出从表所以数据
		for ( Long l :  formAutoId) {
			List<GoodsPaymentD> datas = goodspaymentdservice.findDatas(l);
			if (CollectionUtil.isEmpty(datas)) return "";
			for (GoodsPaymentD s : datas) {
				//根据以上字段去查询周间客户从表;存货id,传票号,版本号
				WeekOrderD weekOrderD = weekorderdservice.getWeekOrderD(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
				if (Objects.nonNull(weekOrderD)) {
					//获取此单据的所有从表数据
					List<WeekOrderD> weekOrderDdatas = weekorderdservice.getWeekOrderDdatas(weekOrderD.getIWeekOrderMid());
					if (CollectionUtil.isNotEmpty(weekOrderDdatas)) {
						//todo 产品设计不合理，所以代码也就这样循环判断(数据量上来直接oom)；查出此条周间客户从表数据，判断货款核对从表是否全部包含周间客户从表数据，包含则修改周间客户主表 字段
						for (WeekOrderD d : weekOrderDdatas) {
							for (GoodsPaymentD gd : getall) {
								if (!d.getIInventoryId().equals(gd.getIInventoryId()) || !d.getCCode().equals(gd.getCBarcode()) || !d.getCVersion().equals(gd.getCVersion())) {
									break;
								}
							}
							//查询周间客户主表
							WeekOrderM byId = weekordermservice.findById(d.getIWeekOrderMid());
							byId.setIOrderStatus(WeekOrderStatusEnum.OK.getValue());
							weekordermservice.updateOneColumn(byId);
						}
					}
				}
			}
		}
		return "";
	}


	//批量反审后的操作
	public String delectbelowtwo(List<Long>  formAutoId){
		for(Long l : formAutoId) {
			//根据主表id 查出从表所以数据
			List<GoodsPaymentD> datas = goodspaymentdservice.findDatas(l);
			if (CollectionUtil.isEmpty(datas)) return "";
			for (GoodsPaymentD s : datas) {
				//根据以上字段去查询周间客户从表;存货id,传票号,版本号
				WeekOrderD weekOrderD = weekorderdservice.getWeekOrderD(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
				if (Objects.isNull(weekOrderD)) break;
				WeekOrderD weekOrderDIOrderStatus = weekorderdservice.getWeekOrderDIOrderStatus(s.getIInventoryId(), s.getCBarcode(), s.getCVersion());
				if (Objects.isNull(weekOrderDIOrderStatus)) break;
				WeekOrderM byId = weekordermservice.findById(weekOrderDIOrderStatus.getIWeekOrderMid());
				byId.setIOrderStatus(WeekOrderStatusEnum.DELIVERIED.getValue());
				weekordermservice.updateOneColumn(byId);
			}
		}
		return "";
	}

	/**
	 * 设置参数
	 */
	private GoodsPaymentD setGoodsPaymentDModel(GoodsPaymentD goodsPaymentDModel,Long iAutoId){
		String cInvCode = goodsPaymentDModel.getCInvCode();
		Inventory inventory = inventoryservice.findFirst("select * from bd_inventory where cInvCode= ? ", cInvCode);
		System.out.println("cInvCode===="+cInvCode);
		goodsPaymentDModel.setIAutoId(JBoltSnowflakeKit.me.nextId());
		goodsPaymentDModel.setIGoodsPaymentMid(iAutoId);
		goodsPaymentDModel.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		goodsPaymentDModel.setIInventoryId(StrUtil.toString(inventory.getIAutoId()));
		goodsPaymentDModel.setCInvCode(cInvCode);
		goodsPaymentDModel.setICreateBy(userId);
		goodsPaymentDModel.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		goodsPaymentDModel.setCCreateName(userName);
		goodsPaymentDModel.setCUpdateName(userName);
		Date date = new Date();
		goodsPaymentDModel.setDCreateTime(date);
		goodsPaymentDModel.setDUpdateTime(date);
		return goodsPaymentDModel;
	}


}