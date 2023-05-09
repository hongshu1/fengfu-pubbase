package cn.rjtech.admin.inventoryqcformtype;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryQcForm;
import cn.rjtech.model.momdata.InventoryQcFormType;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 质量建模-检验适用标准类型
 * @ClassName: InventoryQcFormTypeService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 19:01
 */
public class InventoryQcFormTypeService extends BaseService<InventoryQcFormType> {
	private final InventoryQcFormType dao=new InventoryQcFormType().dao();
	@Override
	protected InventoryQcFormType dao() {
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
	 * @param keywords   关键词
     * @param iType 类型：1. 来料检 2. 在库检 3. 出库检
     * @param cTypeName 类型名称
	 * @return
	 */
	public Page<InventoryQcFormType> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, String cTypeName) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eq("cTypeName",cTypeName);
        //关键词模糊查询
        sql.like("cTypeName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryQcFormType
	 * @return
	 */
	public Ret save(InventoryQcFormType inventoryQcFormType) {
		if(inventoryQcFormType==null || isOk(inventoryQcFormType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryQcFormType.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcFormType.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryQcFormType.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcFormType.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryQcFormType
	 * @return
	 */
	public Ret update(InventoryQcFormType inventoryQcFormType) {
		if(inventoryQcFormType==null || notOk(inventoryQcFormType.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryQcFormType dbInventoryQcFormType=findById(inventoryQcFormType.getIAutoId());
		if(dbInventoryQcFormType==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryQcFormType.getName(), inventoryQcFormType.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcFormType.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryQcFormType.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcFormType.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryQcFormType 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryQcFormType inventoryQcFormType, Kv kv) {
		//addDeleteSystemLog(inventoryQcFormType.getIAutoId(), JBoltUserKit.getUserId(),inventoryQcFormType.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryQcFormType model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryQcFormType inventoryQcFormType, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public <T> int removeByInventoryQcFormId(T inventoryQcFormId){
		if (ObjectUtil.isNull(inventoryQcFormId)){
			return 0;
		}
		return delete("delete  Bd_InventoryQcFormType where iInventoryQcFormId=?", inventoryQcFormId);
	}

	public <T> int removeByInventoryQcFormId(List<T> inventoryQcFormIds){
		if (CollectionUtil.isEmpty(inventoryQcFormIds)){
			return 0;
		}
		int deleteCount = 0;
		for (T id : inventoryQcFormIds){
			int delCount = removeByInventoryQcFormId(id);
			deleteCount+= delCount;
		}
		return deleteCount;
	}
	
	public List<InventoryQcFormType> getQcFormTypeList(List<InventoryQcForm> qcFormList, Map<String, String> inspectionMap){
		if (CollectionUtil.isEmpty(qcFormList) || CollectionUtil.isEmpty(inspectionMap)){
			return null;
		}
		List<InventoryQcFormType> qcFormTypeList = new ArrayList<>();
		qcFormList.forEach(inventoryQcForm ->{
			for (String key :inspectionMap.keySet()){
				InventoryQcFormType inventoryQcFormType = new InventoryQcFormType();
				inventoryQcFormType.setCTypeName(inspectionMap.get(key));
				inventoryQcFormType.setIInventoryQcFormId(inventoryQcForm.getIAutoId());
				inventoryQcFormType.setIType(Integer.valueOf(key));
				qcFormTypeList.add(inventoryQcFormType);
			}
		});
		return qcFormTypeList;
	}
}
