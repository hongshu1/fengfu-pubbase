package cn.rjtech.admin.goodspaymentm;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.GoodsPaymentD;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 货款核对明细
 * @ClassName: GoodsPaymentDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-30 00:13
 */
public class GoodsPaymentDService extends BaseService<GoodsPaymentD> {
	private final GoodsPaymentD dao=new GoodsPaymentD().dao();
	@Override
	protected GoodsPaymentD dao() {
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
	public Page<GoodsPaymentD> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param goodsPaymentD
	 * @return
	 */
	public Ret save(GoodsPaymentD goodsPaymentD) {
		if(goodsPaymentD==null || isOk(goodsPaymentD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(goodsPaymentD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=goodsPaymentD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(goodsPaymentD.getIAutoId(), JBoltUserKit.getUserId(), goodsPaymentD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param goodsPaymentD
	 * @return
	 */
	public Ret update(GoodsPaymentD goodsPaymentD) {
		if(goodsPaymentD==null || notOk(goodsPaymentD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		GoodsPaymentD dbGoodsPaymentD=findById(goodsPaymentD.getIAutoId());
		if(dbGoodsPaymentD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(goodsPaymentD.getName(), goodsPaymentD.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=goodsPaymentD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(goodsPaymentD.getIAutoId(), JBoltUserKit.getUserId(), goodsPaymentD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param goodsPaymentD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(GoodsPaymentD goodsPaymentD, Kv kv) {
		//addDeleteSystemLog(goodsPaymentD.getIAutoId(), JBoltUserKit.getUserId(),goodsPaymentD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param goodsPaymentD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(GoodsPaymentD goodsPaymentD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<Record> findEditTableDatas(Kv para) {
		ValidationUtils.notNull(para.getLong("goodspaymentmid"), JBoltMsg.PARAM_ERROR);
		List<Record> records = dbTemplate("goodspaymentm.dList", para).find();
		return records;
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		updateColumn(id, "isdeleted", true);
		return SUCCESS;
	}
	public Ret deleteByIdsRm(String ids) {
		String[] split = ids.split(",");
		for(String s : split){
			updateColumn(s, "isdeleted", true);
		}
		return SUCCESS;
	}

	public List<GoodsPaymentD> findDatas(Long mid){
		List<GoodsPaymentD> goodsPaymentDS = find("select * from SM_GoodsPaymentD where isDeleted='0' and iGoodsPaymentMid = ?", mid);
		return goodsPaymentDS;
	}
	public List<GoodsPaymentD> getall(){
		List<GoodsPaymentD> goodsPaymentDS = find("select * from SM_GoodsPaymentD where isDeleted='0' ");
		return goodsPaymentDS;
	}
}