package cn.rjtech.admin.vendor;

import static cn.hutool.core.text.StrPool.COMMA;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.plugin.activerecord.Page;

import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.kit.JBoltControllerKit;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.admin.vendoraddr.VendorAddrService;
import cn.rjtech.admin.vendorclass.VendorClassService;
import cn.rjtech.admin.warehouse.WarehouseService;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.VendorAddr;
import cn.rjtech.model.momdata.VendorClass;
import cn.rjtech.model.momdata.Warehouse;

/**
 * 往来单位-供应商档案
 * @ClassName: VendorService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-21 17:16
 */
public class VendorService extends BaseService<Vendor> {
	private final Vendor dao=new Vendor().dao();
	@Override
	protected Vendor dao() {
		return dao;
	}

	@Inject
	private VendorClassService vendorClassService;
	@Inject
	private VendorAddrService vendorAddrService;
	@Inject
	private WarehouseService warehouseService;

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isEnabled 是否启用：0. 停用 1. 启用
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<Vendor> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cVenName", "cVenAbbName", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	public Page<Record> pageList(Kv kv) {
		Page<Record> recordPage = dbTemplate("vendor.list", kv).paginate(kv.getInt("page"), kv.getInt("pageSize"));
		return recordPage;
	}

	/**
	 * 保存
	 * @param vendor
	 * @return
	 */
	public Ret save(Vendor vendor) {
		if(vendor==null || isOk(vendor.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//查询默认委外仓
		Warehouse warehouse = warehouseService.findById(vendor.getCOMWhCode());
		saveVendorModel(vendor,warehouse);
		return ret(true);
		//boolean success=vendor.save();
		//return ret(success);
	}

	public void saveVendorModel(Vendor vendor,Warehouse warehouse) {
		vendor.setIAutoId(JBoltSnowflakeKit.me.nextId());
		Long userId = JBoltUserKit.getUserId();
		String userName = JBoltUserKit.getUserName();
		Date date = new Date();
		vendor.setICreateBy(userId);
		vendor.setIUpdateBy(userId);
		vendor.setCCreateName(userName);
		vendor.setCUpdateName(userName);
		vendor.setDCreateTime(date);
		vendor.setDUpdateTime(date);
		//vendor.setCDCCode("");//地区编码
		vendor.setCOrgName(getOrgName());
		vendor.setCOrgCode(getOrgCode());
		vendor.setIsDeleted(false);
		vendor.setIsEnabled(true);
		vendor.setISource(1);
		vendor.setIOMWhId(warehouse.getIAutoId());//委外仓id
		vendor.setCOMWhCode(warehouse.getCWhCode());//委外仓编码
		vendor.setCCountry("中国");
		vendor.setCProvince("省份");
		vendor.setCCity("城市");
		vendor.setCCounty("区县");
	}

	/**
	 * 执行JBoltTable表格整体提交
	 * @param jBoltTable
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		if(jBoltTable==null||jBoltTable.isBlank()) {
			return fail(JBoltMsg.JBOLTTABLE_IS_BLANK);
		}
		//获取额外传递参数 比如订单ID等信息 在下面数据里可能用到
		if(jBoltTable.paramsIsNotBlank()) {
			System.out.println(jBoltTable.getParams().toJSONString());
		}
		//获取头表对应的数据--bd_vendor
		if (jBoltTable.saveIsNotBlank()){
			List<VendorAddr> vendorAddrs = jBoltTable.getSaveModelList(VendorAddr.class);
			for (VendorAddr vendorAddr : vendorAddrs) {
				//vendorAddrService.save(vendorAddr);
			}
		}
		//获取Form对应的数据--bd_vendoraddr
		if(jBoltTable.formIsNotBlank()) {
			//这里需要根据自己的需要 从Form这个JSON里去获取自己需要的数据
			Vendor vendor = jBoltTable.getFormModel(Vendor.class);
			Record record=jBoltTable.getFormRecord();
			String[] split = vendor.getCProvince().split(",");
			vendor.setCProvince(split[0]);//省份
			vendor.setCCity(split[1]);//城市
			vendor.setCCounty(split[2]);//区县
			//save(vendor);
		}
		return SUCCESS;

		//获取待保存数据 执行保存
		/*if(jBoltTable.saveIsNotBlank()) {
			vendorAddrService.batchSave(jBoltTable.getSaveModelList(VendorAddr.class));
		}*/
		/*//获取待更新数据 执行更新
		if(jBoltTable.updateIsNotBlank()) {
			batchUpdate(jBoltTable.getUpdateModelList(Demotable.class));
		}
		//获取待删除数据 执行删除
		if(jBoltTable.deleteIsNotBlank()) {
			deleteByIds(jBoltTable.getDelete());
		}*/
	}

	/**
	 * 更新
	 * @param vendor
	 * @return
	 */
	public Ret update(Vendor vendor) {
		if(vendor==null || notOk(vendor.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Vendor dbVendor=findById(vendor.getIAutoId());
		if(dbVendor==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=vendor.update();
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param vendor 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Vendor vendor, Kv kv) {
		//addDeleteSystemLog(vendor.getIAutoId(), JBoltUserKit.getUserId(),vendor.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vendor model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Vendor vendor, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Vendor vendor, String column, Kv kv) {
		//addUpdateSystemLog(vendor.getIAutoId(), JBoltUserKit.getUserId(), vendor.getName(),"的字段["+column+"]值:"+vendor.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

	public List<Record> options() {
		return dbTemplate("vendor.findColumns", Kv.of("isenabled", "true")).find();
	}

	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_Vendor WHERE iautoid IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}

}