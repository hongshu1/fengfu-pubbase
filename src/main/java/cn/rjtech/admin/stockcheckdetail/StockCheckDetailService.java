package cn.rjtech.admin.stockcheckdetail;


import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltModelKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.StockCheckDetail;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * null管理
 * @ClassName: StockCheckDetailService
 * @author: demo15
 * @date: 2023-05-03 14:26
 */
public class StockCheckDetailService extends BaseService<StockCheckDetail> {
	private final StockCheckDetail dao=new StockCheckDetail().dao();
	@Override
	protected StockCheckDetail dao() {
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
	public Page<StockCheckDetail> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param stockCheckDetail
	 * @return
	 */
	public Ret save(StockCheckDetail stockCheckDetail) {
		if(stockCheckDetail==null || isOk(stockCheckDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(stockCheckDetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckDetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(stockCheckDetail.getAutoid(), JBoltUserKit.getUserId(), stockCheckDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param stockCheckDetail
	 * @return
	 */
	public Ret update(StockCheckDetail stockCheckDetail) {
		if(stockCheckDetail==null || notOk(stockCheckDetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		StockCheckDetail dbStockCheckDetail=findById(stockCheckDetail.getAutoID());
		if(dbStockCheckDetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(stockCheckDetail.getName(), stockCheckDetail.getAutoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=stockCheckDetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(stockCheckDetail.getAutoid(), JBoltUserKit.getUserId(), stockCheckDetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param stockCheckDetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(StockCheckDetail stockCheckDetail, Kv kv) {
		//addDeleteSystemLog(stockCheckDetail.getAutoid(), JBoltUserKit.getUserId(),stockCheckDetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param stockCheckDetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(StockCheckDetail stockCheckDetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}


	/**
	 * 通过sourceid查找
	 * */
	public List<StockCheckDetail> findBySourceId(String sourceid) {
		List<StockCheckDetail> list = daoTemplate("stockcheckdetail.findBySourceId", Kv.by("sourceid", sourceid)).find();
		return list;
	}

	/**
	 *通过masid 查找
	 * */
	public List<StockCheckDetail> findByMasid(String masid) {
		List<StockCheckDetail> list = daoTemplate("stockcheckdetail.findByMasid", Kv.by("masid", masid)).find();
		return list;
	}

	/**
	 *通过masid 查找
	 * */
	public List<StockCheckDetail> findByMasid_app(String masid) {
		List<StockCheckDetail> list = daoTemplate("stockcheckdetail.findByMasid_app", Kv.by("masid", masid)).find();
		return list;
	}



	/**
	 *通过masid 查找
	 * */
	public List<Record> findRecordByMasid(String masid) {
		List<Record> list = dbTemplate("stockcheckdetail.findRecordByMasid", Kv.by("masid", masid)).find();
		return list;
	}

	/**
	 * 通过盘点单号查询
	 * */
	public List<StockCheckDetail> findByMBillno(String billno) {
		List<StockCheckDetail> list = daoTemplate("stockcheckdetail.findByMBillno", Kv.by("masid", billno)).find();
		return list;
	}




	/**
	 *调整
	 * */
	public Ret adjust(Kv kv) {
		tx(() -> {
			String datas = kv.getStr("datas");

			String ids = kv.getStr("sourceid");

			List<Record> rows = JBoltModelKit.getFromRecords(datas);

			List<JSONObject> jsonObjects = JSONArray.parseArray(datas, JSONObject.class);
			for (JSONObject jsonObject : jsonObjects) {
				String source = jsonObject.getString("source");
				if(source.equals("app")){
					Integer autoid = jsonObject.getInteger("autoid");
					StockCheckDetail dbModel = findById(autoid);
					ValidationUtils.notNull(dbModel,"查无此数据!");

					BigDecimal adjustqty2 = jsonObject.getBigDecimal("adjustqty2");
					ValidationUtils.notNull(adjustqty2,"调整数量为空!");

					dbModel.setAdjustQty(adjustqty2);
					boolean update = dbModel.update();
					ValidationUtils.isTrue(update,"更新失败!");
				}else if(source.equals("web")){
					String sourceid = kv.getStr("sourceid");

					//分保存和更新两个方向
					Integer autoid = jsonObject.getInteger("autoid");
					String barcode = jsonObject.getString("barcode");
					BigDecimal qty = jsonObject.getBigDecimal("qty");
					BigDecimal realqty = jsonObject.getBigDecimal("realqty2");

					String invcode = jsonObject.getString("invcode");
					Date createdate = jsonObject.getDate("createdate");

					BigDecimal adjustqty2 = jsonObject.getBigDecimal("adjustqty2");
					ValidationUtils.notNull(adjustqty2,"调整数量为空!");

					if(autoid==null){
						//新增
						StockCheckDetail model=new StockCheckDetail();
						model.setAdjustQty(adjustqty2);
						model.setSourceID(sourceid);
						model.setBarcode(barcode);
						model.setQty(qty);
						model.setRealQty(realqty);
						model.setGenerateType("web");
						model.setInvCode(invcode);
						model.setCreateDate(createdate);

						ValidationUtils.isTrue(model.save(),"保存失败!");

					}else{
						//更新
						StockCheckDetail dbModel = findById(autoid);
						ValidationUtils.notNull(dbModel,"查无此数据!");
						dbModel.setAdjustQty(adjustqty2);
						boolean update = dbModel.update();
						ValidationUtils.isTrue(update,"更新失败!");
					}
				}
			}


			return true;
		});

		return SUCCESS.set("AutoID", kv.getStr("sourceid"));
	}
}