package cn.rjtech.admin.vendoraddr;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.admin.vendor.VendorService;
import cn.rjtech.admin.vendorclass.VendorClassService;
import cn.rjtech.model.momdata.Vendor;
import cn.rjtech.model.momdata.VendorAddr;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.List;

/**
 * 供应商档案-联系地址
 * @ClassName: VendorAddrService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 17:59
 */
public class VendorAddrService extends BaseService<VendorAddr> {
	private final VendorAddr dao=new VendorAddr().dao();
	@Override
	protected VendorAddr dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
	private VendorService      vendorService;
	@Inject
	private VendorClassService vendorClassService;


	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
     * @param isEnabled 是否生效：0. 否 1. 是
     * @param isDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<VendorAddr> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cDistrictName", "cContactName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	public Page<Record> pageList(Kv kv) {
		return dbTemplate("vendoraddr.list",kv).paginate(kv.getInt("page"),kv.getInt("pageSize"));
	}

	/**
	 * 保存
	 * @param vendorAddr
	 * @return
	 */
	public Ret save(VendorAddr vendorAddr) {
		if(vendorAddr==null || isOk(vendorAddr.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		saveVendorAddrModel(vendorAddr);
		boolean success=vendorAddr.save();
		return ret(success);
	}

	public void saveVendorAddrModel(VendorAddr vendorAddr){
		vendorAddr.setIsEnabled(true);
		vendorAddr.setIsDeleted(false);
	}

	/**
	 * 更新
	 * @param vendorAddr
	 * @return
	 */
	public Ret update(VendorAddr vendorAddr) {
		if(vendorAddr==null || notOk(vendorAddr.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		VendorAddr dbVendorAddr=findById(vendorAddr.getIAutoId());
		if(dbVendorAddr==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(vendorAddr.getName(), vendorAddr.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=vendorAddr.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(vendorAddr.getIAutoId(), JBoltUserKit.getUserId(), vendorAddr.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param vendorAddr 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(VendorAddr vendorAddr, Kv kv) {
		//addDeleteSystemLog(vendorAddr.getIAutoId(), JBoltUserKit.getUserId(),vendorAddr.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param vendorAddr model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(VendorAddr vendorAddr, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(VendorAddr vendorAddr, String column, Kv kv) {
		//addUpdateSystemLog(vendorAddr.getIAutoId(), JBoltUserKit.getUserId(), vendorAddr.getName(),"的字段["+column+"]值:"+vendorAddr.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

	public List<VendorAddr> list(Kv kv){
		return find("SELECT * FROM Bd_VendorAddr WHERE ivendorid = ?", kv.get("ivendorid"));
	}

	public Ret updateEditTable(JBoltTable jBoltTable, Long userId, Date now) {
		VendorAddr vendorAddr = jBoltTable.getFormModel(VendorAddr.class, "vendoraddr");
		ValidationUtils.notNull(vendorAddr, JBoltMsg.PARAM_ERROR);

		tx(() -> {
			//修改
			if(isOk(vendorAddr.getIAutoId())){
				ValidationUtils.isTrue(vendorAddr.update(), JBoltMsg.FAIL);
			}else{
				//新增
				//编码是否存在
//				ValidationUtils.isTrue(findByCustomermCode(vendorAddr.getCcustomercode())==null, "编码重复");
				vendorAddr.setIAutoId(JBoltSnowflakeKit.me.nextId());
				ValidationUtils.isTrue(vendorAddr.save(), JBoltMsg.FAIL);
			}

			//新增
			List<Vendor> saveRecords = jBoltTable.getSaveModelList(Vendor.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (Vendor row : saveRecords) {
//					row.setICreateBy(vendorAddr.getIcreateby());
//					row.setCcreatename(vendorAddr.getCcreatename());
//					row.setDcreatetime(vendorAddr.getDcreatetime());
//					row.setIAutoId(vendorAddr.getIautoid());
				}
				vendorService.batchSave(saveRecords, 500);
			}

			//修改
			List<Vendor> updateRecords = jBoltTable.getUpdateModelList(Vendor.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				vendorService.batchUpdate(updateRecords, 500);
			}

			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				vendorService.deleteMultiByIds(deletes);
			}

			return true;
		});

		return SUCCESS;
	}
	
	public List<Record> findList(Kv kv){
		return dbTemplate("vendoraddr.findList", kv).find();
	}

	public List<VendorAddr> findByIds(List<Long> ids){
		Sql sql = selectSql().in(VendorAddr.IAUTOID, ids);
		return find(sql);
	}
}
