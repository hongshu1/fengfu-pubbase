package cn.rjtech.admin.inventoryroutingsop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.consts.JBoltConst;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltRealUrlUtil;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Equipment;
import cn.rjtech.model.momdata.InventoryRoutingConfig;
import cn.rjtech.model.momdata.InventoryRoutingSop;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.text.StrPool.COMMA;

/**
 * 物料建模-存货工序作业指导书
 * @ClassName: InventoryRoutingSopService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-29 10:06
 */
public class InventoryRoutingSopService extends BaseService<InventoryRoutingSop> {
	private final InventoryRoutingSop dao=new InventoryRoutingSop().dao();
	@Override
	protected InventoryRoutingSop dao() {
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
     * @param isEnabled 是否启用
	 * @return
	 */
	public Page<InventoryRoutingSop> getAdminDatas(int pageNumber, int pageSize, String keywords, Boolean isEnabled) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("isEnabled",isEnabled);
        //关键词模糊查询
        sql.likeMulti(keywords,"cName", "cCreateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param inventoryRoutingSop
	 * @return
	 */
	public Ret save(InventoryRoutingSop inventoryRoutingSop) {
		if(inventoryRoutingSop==null || isOk(inventoryRoutingSop.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryRoutingSop.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingSop.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryRoutingSop
	 * @return
	 */
	public Ret update(InventoryRoutingSop inventoryRoutingSop) {
		if(inventoryRoutingSop==null || notOk(inventoryRoutingSop.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryRoutingSop dbInventoryRoutingSop=findById(inventoryRoutingSop.getIAutoId());
		if(dbInventoryRoutingSop==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryRoutingSop.getName(), inventoryRoutingSop.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryRoutingSop.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryRoutingSop 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryRoutingSop inventoryRoutingSop, Kv kv) {
		//addDeleteSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(),inventoryRoutingSop.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryRoutingSop model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryRoutingSop inventoryRoutingSop, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryRoutingSop inventoryRoutingSop, String column, Kv kv) {
		//addUpdateSystemLog(inventoryRoutingSop.getIAutoId(), JBoltUserKit.getUserId(), inventoryRoutingSop.getName(),"的字段["+column+"]值:"+inventoryRoutingSop.get(column));
		/**
		switch(column){
		    case "isEnabled":
		        break;
		}
		*/
		return null;
	}

	/**
	 * 保存文件底层方法
	 * @param file
	 * @param uploadPath
	 * @param fileType
	 * @return
	 */
	public InventoryRoutingSop saveJBoltFile(UploadFile file, String uploadPath, int fileType, Long configid) {
		String localPath=file.getUploadPath()+ File.separator+file.getFileName();
		String localUrl= FileUtil.normalize(JBoltRealUrlUtil.get(JFinal.me().getConstants().getBaseUploadPath()+ JBoltConst.SLASH+uploadPath+JBoltConst.SLASH+file.getFileName()));
		localPath=FileUtil.normalize(localPath);

		InventoryRoutingSop itemroutingdrawing = new InventoryRoutingSop();
		itemroutingdrawing.setIInventoryRoutingConfigId(configid);
		itemroutingdrawing.setCName(file.getOriginalFileName());
		itemroutingdrawing.setCPath(localUrl);//jboltFile.setLocalUrl(localUrl);
		File realFile=file.getFile();
		String fileSuffix= FileTypeUtil.getType(realFile);
		itemroutingdrawing.setCSuffix(fileSuffix);
		Long fileSize=FileUtil.size(realFile);
		itemroutingdrawing.setISize(fileSize.intValue());
		itemroutingdrawing.setICreateBy(JBoltUserKit.getUserId());
		itemroutingdrawing.setCCreateName(JBoltUserKit.getUserName());
		itemroutingdrawing.setDCreateTime(new Date());

		//boolean success=itemroutingdrawing.save();
		return itemroutingdrawing;
	}

	public List<InventoryRoutingSop> dataList(Kv kv) {
		
		/*if (StrUtil.isNotBlank(kv.getStr(InventoryRoutingConfig.ROUTINGSOPJSON))){
			String str = kv.getStr(InventoryRoutingConfig.ROUTINGSOPJSON);
			JSONArray jsonArray = JSONObject.parseArray(str);
			if (CollectionUtil.isEmpty(jsonArray)){
				return null;
			}
			List<Record> recordList = new ArrayList<>();
			for (int i=0; i<jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Record record = new Record();
				record.setColumns(jsonObject.getInnerMap());
				
				Long equipmentId = jsonObject.getLong("iequipmentid");
//				if (ObjectUtil.isNotNull(equipmentId)){
//					Equipment equipment = equipmentService.findById(equipmentId);
//					ValidationUtils.notNull(equipment, "未找到设备信息");
//					record.set(equipment.CEQUIPMENTCODE, equipment.getCEquipmentCode());
//				}
				recordList.add(record);
			}
			return recordList;
		}*/
		
		if (StrUtil.isBlank(kv.getStr("configid"))){
			return null;
		}
		return getCommonList(Okv.by("iInventoryRoutingConfigId",kv.getLong("configId")),"iAutoId","ASC");
	}

	public Ret saveDrawing(JBoltTable jBoltTable, Long iitemroutingconfigid) {
		tx(() -> {
			//新增
			List<InventoryRoutingSop> saveRecords = jBoltTable.getSaveModelList(InventoryRoutingSop.class);
			if (CollUtil.isNotEmpty(saveRecords)) {
				for (int i = 0; i < saveRecords.size(); i++) {
					Long autoid = JBoltSnowflakeKit.me.nextId();
					saveRecords.get(i).setIAutoId(autoid);
					saveRecords.get(i).setIInventoryRoutingConfigId(iitemroutingconfigid);
					saveRecords.get(i).setICreateBy(JBoltUserKit.getUserId());
					saveRecords.get(i).setCCreateName(JBoltUserKit.getUserName());
					saveRecords.get(i).setDCreateTime(new Date());
				}
				batchSave(saveRecords, 500);
			}

			//修改
			List<InventoryRoutingSop> updateRecords = jBoltTable.getUpdateModelList(InventoryRoutingSop.class);
			if (CollUtil.isNotEmpty(updateRecords)) {
				batchUpdate(updateRecords, 500);
			}

			// 删除
			Object[] deletes = jBoltTable.getDelete();
			if (ArrayUtil.isNotEmpty(deletes)) {
				deleteMultiByIds(deletes);
			}
			return true;
		});
		return SUCCESS;
	}
	public void deleteMultiByIds(Object[] deletes) {
		delete("DELETE FROM Bd_InventoryRoutingSop WHERE iAutoId IN (" + ArrayUtil.join(deletes, COMMA) + ") ");
	}
}
