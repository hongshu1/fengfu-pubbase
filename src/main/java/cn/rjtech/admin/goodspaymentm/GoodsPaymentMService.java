package cn.rjtech.admin.goodspaymentm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.constants.ErrorMsg;
import cn.rjtech.model.momdata.GoodsPaymentM;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 货款核对表
 * @ClassName: GoodsPaymentMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-30 00:06
 */
public class GoodsPaymentMService extends BaseService<GoodsPaymentM> {

	@Inject
	private GoodsPaymentDService goodsPaymentDservice;

	private final GoodsPaymentM dao=new GoodsPaymentM().dao();
	@Override
	protected GoodsPaymentM dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据

	 * @return
	 */
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv kv) {

		Page<Record> paginate = dbTemplate("goodspaymentm.recpor", kv).paginate(pageNumber, pageSize);

		return paginate;
	}

	/**
	 * 保存
	 * @param goodsPaymentM
	 * @return
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
	 * @param goodsPaymentM
	 * @return
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
	 * @return
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
	 * @return
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
		/**
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
	 * @param id
	 * @return
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
	 * 执行JBoltTable表格整体提交
	 *
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		GoodsPaymentM rcvplanm = jBoltTable.getFormModel(GoodsPaymentM.class,"goodspaymentm");
		//获取当前用户信息？
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		tx(()->{
			//通过 id 判断是新增还是修改
			if(rcvplanm.getIAutoId() == null){
				rcvplanm.setIOrgId(getOrgId());
				rcvplanm.setCOrgCode(getOrgCode());
				rcvplanm.setCOrgName(getOrgName());
				rcvplanm.setIStatus(1);

				rcvplanm.setICreateBy(user.getId());
				rcvplanm.setCCreateName(user.getName());
				rcvplanm.setDCreateTime(now);
				rcvplanm.setIUpdateBy(user.getId());
				rcvplanm.setCUpdateName(user.getName());
				rcvplanm.setDUpdateTime(now);
				rcvplanm.setIsDeleted(false);
				//主表新增
				ValidationUtils.isTrue(rcvplanm.save(), ErrorMsg.SAVE_FAILED);
			}else{
				rcvplanm.setIUpdateBy(user.getId());
				rcvplanm.setCUpdateName(user.getName());
				rcvplanm.setDUpdateTime(now);
				//主表修改
				ValidationUtils.isTrue(rcvplanm.update(), ErrorMsg.UPDATE_FAILED);
			}
			//从表的操作
			// 获取保存数据（执行保存，通过 getSaveRecordList）
			saveTableSubmitDatas(jBoltTable,rcvplanm);
			//获取修改数据（执行修改，通过 getUpdateRecordList）
			updateTableSubmitDatas(jBoltTable,rcvplanm);
			//获取删除数据（执行删除，通过 getDelete）
			deleteTableSubmitDatas(jBoltTable);
			return true;
		});
		return SUCCESS;
	}

	//可编辑表格提交-新增数据
	private void saveTableSubmitDatas(JBoltTable jBoltTable,GoodsPaymentM rcvplanm){
		List<Record> list = jBoltTable.getSaveRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		for (int i=0;i<list.size();i++) {
			Record row = list.get(i);
			row.set("isdeleted", "0");
			row.set("iGoodsPaymentMid", rcvplanm.getIAutoId());
			row.set("iautoid", JBoltSnowflakeKit.me.nextId());
			row.set("dcreatetime", now);
			row.set("dupdatetime", now);
			row.remove("cinvcode1");
			row.remove("cinvname1");
			row.remove("cinvcode");
			row.remove("cinvstd");
			row.remove("cuomname");
		}
		goodsPaymentDservice.batchSaveRecords(list);
	}
	//可编辑表格提交-修改数据
	private void updateTableSubmitDatas(JBoltTable jBoltTable,GoodsPaymentM rcvplanm){
		List<Record> list = jBoltTable.getUpdateRecordList();
		if(CollUtil.isEmpty(list)) return;
		Date now = new Date();
		for(int i = 0;i < list.size(); i++){
			Record row = list.get(i);
			row.set("dupdatetime", now);
			row.remove("cinvcode1");
			row.remove("cinvname1");
			row.remove("cinvcode");
			row.remove("cinvstd");
			row.remove("cuomname");
		}
		goodsPaymentDservice.batchUpdateRecords(list);
	}
	//可编辑表格提交-删除数据
	private void deleteTableSubmitDatas(JBoltTable jBoltTable){
		Object[] ids = jBoltTable.getDelete();
		if(ArrayUtil.isEmpty(ids)) return;
		for (Object id : ids) {
			goodsPaymentDservice.deleteById(id);
		}
	}
}