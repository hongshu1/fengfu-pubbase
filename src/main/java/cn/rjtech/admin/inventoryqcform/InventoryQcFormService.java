package cn.rjtech.admin.inventoryqcform;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.JboltFile;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.JBoltFileService;
import cn.jbolt.core.ui.jbolttable.JBoltTable;
import cn.jbolt.core.util.JBoltArrayUtil;
import cn.jbolt.core.util.JBoltCamelCaseUtil;
import cn.jbolt.core.util.JBoltUploadFileUtil;
import cn.rjtech.admin.qcform.QcFormService;
import cn.rjtech.model.momdata.QcForm;
import cn.rjtech.util.ValidationUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import java.io.File;
import com.jfinal.plugin.activerecord.IAtom;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.jbolt.core.poi.excel.*;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.InventoryQcForm;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * 质量建模-检验适用标准
 * @ClassName: InventoryQcFormService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-03 14:20
 */
public class InventoryQcFormService extends BaseService<InventoryQcForm> {
	private final InventoryQcForm dao=new InventoryQcForm().dao();
	@Override
	protected InventoryQcForm dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

    @Inject
    private JBoltFileService jboltFileService;
	/**
	 * 后台管理数据查询
	 * @param pageNumber 第几页
	 * @param pageSize   每页几条数据
	 * @param keywords   关键词
	 * @param sortColumn  排序列名
	 * @param sortType  排序方式 asc desc
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
     * @param machineType 机型
     * @param inspectionType 检验类型
	 * @return
	 */
	public Page<InventoryQcForm> getAdminDatas(int pageNumber, int pageSize, String keywords, String sortColumn, String sortType, Boolean IsDeleted, String machineType, String inspectionType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        sql.eq("machineType",machineType);
        sql.eq("inspectionType",inspectionType);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cCreateName", "cUpdateName", "iQcFormName", "iInventoryName", "componentName");
        //排序
        sql.orderBy(sortColumn,sortType);
		return paginate(sql);
	}

	/**
	 * 列表数据源
	 * @param pageNumber
	 * @param pageSize
	 * @param kv
	 * @return
	 */
	public Page<InventoryQcForm> getAdminDatas(int pageNumber, int pageSize, Kv kv) {
		Page<InventoryQcForm> paginate = daoTemplate("inventoryqcform.pageList", kv).paginate(pageNumber, pageSize);
		System.out.println("paginate===>"+paginate.getList());
		return paginate;
	}

	/**
	 * 保存
	 * @param inventoryQcForm
	 * @return
	 */
	public Ret save(InventoryQcForm inventoryQcForm) {
		if(inventoryQcForm==null || isOk(inventoryQcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(inventoryQcForm.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcForm.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param inventoryQcForm
	 * @return
	 */
	public Ret update(InventoryQcForm inventoryQcForm) {
		if(inventoryQcForm==null || notOk(inventoryQcForm.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryQcForm dbInventoryQcForm=findById(inventoryQcForm.getIAutoId());
		if(dbInventoryQcForm==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryQcForm.getName(), inventoryQcForm.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryQcForm.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryQcForm 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(InventoryQcForm inventoryQcForm, Kv kv) {
		//addDeleteSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(),inventoryQcForm.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryQcForm model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(InventoryQcForm inventoryQcForm, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
     * 生成excel导入使用的模板
     * @return
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
                //创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create()
                        //设置列映射 顺序 标题名称 不处理别名
                        .setHeaders(1,false,
                                JBoltExcelHeader.create("创建人名称",15),
                                JBoltExcelHeader.create("更新人名称",15),
                                JBoltExcelHeader.create("检验表格名称",15),
                                JBoltExcelHeader.create("机型",15),
                                JBoltExcelHeader.create("存货编码",15),
                                JBoltExcelHeader.create("存货名称",15),
                                JBoltExcelHeader.create("客户部番",15),
                                JBoltExcelHeader.create("部品名称",15),
                                JBoltExcelHeader.create("规格",15),
                                JBoltExcelHeader.create("主计量单位",15),
                                JBoltExcelHeader.create("检验类型",15)
                                )
                    );
    }

    /**
	 * 读取excel文件
	 * @param file
	 * @return
	 */
	public Ret importExcel(File file) {
		StringBuilder errorMsg=new StringBuilder();
		JBoltExcel jBoltExcel=JBoltExcel
		//从excel文件创建JBoltExcel实例
		.from(file)
		//设置工作表信息
		.setSheets(
				JBoltExcelSheet.create()
				//设置列映射 顺序 标题名称
                .setHeaders(1,
                        JBoltExcelHeader.create("cCreateName","创建人名称"),
                        JBoltExcelHeader.create("cUpdateName","更新人名称"),
                        JBoltExcelHeader.create("iQcFormName","检验表格名称"),
                        JBoltExcelHeader.create("machineType","机型"),
                        JBoltExcelHeader.create("iInventoryCode","存货编码"),
                        JBoltExcelHeader.create("iInventoryName","存货名称"),
                        JBoltExcelHeader.create("CustomerManager","客户部番"),
                        JBoltExcelHeader.create("componentName","部品名称"),
                        JBoltExcelHeader.create("specs","规格"),
                        JBoltExcelHeader.create("unit","主计量单位"),
                        JBoltExcelHeader.create("inspectionType","检验类型")
                        )
				//从第三行开始读取
				.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<InventoryQcForm> inventoryQcForms=JBoltExcelUtil.readModels(jBoltExcel,1, InventoryQcForm.class,errorMsg);
		if(notOk(inventoryQcForms)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		//执行批量操作
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(inventoryQcForms);
				return true;
			}
		});

		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}

    /**
	 * 生成要导出的Excel
	 * @return
	 */
	public JBoltExcel exportExcel(List<InventoryQcForm> datas) {
	    return JBoltExcel
			    //创建
			    .create()
    		    //设置工作表
    		    .setSheets(
    				//设置工作表 列映射 顺序 标题名称
    				JBoltExcelSheet
    				.create()
    				//表头映射关系
                    .setHeaders(1,
                            JBoltExcelHeader.create("cDcCode","设变号",15),
                            JBoltExcelHeader.create("cMeasure","测定理由",15),
                            JBoltExcelHeader.create("cCreateName","创建人名称",15),
                            JBoltExcelHeader.create("cUpdateName","更新人名称",15)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setModelDatas(2,datas)
    		    );
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(InventoryQcForm inventoryQcForm, String column, Kv kv) {
		//addUpdateSystemLog(inventoryQcForm.getIAutoId(), JBoltUserKit.getUserId(), inventoryQcForm.getName(),"的字段["+column+"]值:"+inventoryQcForm.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		    case "iQcFormName":
		        break;
		    case "machineType":
		        break;
		    case "iInventoryCode":
		        break;
		    case "iInventoryName":
		        break;
		    case "CustomerManager":
		        break;
		    case "componentName":
		        break;
		    case "specs":
		        break;
		    case "unit":
		        break;
		    case "inspectionType":
		        break;
		}
		*/
		return null;
	}

	/**
	 * 获取存货数据
	 * @param kv
	 * @return
	 */
	public Page<Record> resourceList(Kv kv, int pageNum, int pageSize){
		Page<Record> recordPage = dbTemplate("inventoryqcform.resourceList", kv).paginate(pageNum, pageSize);
		JBoltCamelCaseUtil.keyToCamelCase(recordPage);
		return recordPage;
	}

	/**
	 * 可编辑表格提交
	 * @param jBoltTable 编辑表格提交内容
	 * @return
	 */
	public Ret submitByJBoltTable(JBoltTable jBoltTable) {
		//当前操作人员  当前时间
		User user = JBoltUserKit.getUser();
		Date nowDate = new Date();
		Long orgId = getOrgId();
		String orgCode = getOrgCode();
		String orgName = getOrgName();
		System.out.println("saveTable===>"+jBoltTable.getSave());
		System.out.println("updateTable===>"+jBoltTable.getUpdate());
		System.out.println("deleteTable===>"+jBoltTable.getDelete());
		System.out.println("form===>"+jBoltTable.getForm());

		Db.use(dataSourceConfigName()).tx(() -> {
        JSONObject formBean = jBoltTable.getForm();
		String iQcFormId = formBean.getString("inventoryQcForm.iQcFormId");
		String iQcFormName = formBean.getString("inventoryQcForm.iQcFormName");

        if (jBoltTable.saveIsNotBlank()){
            List<InventoryQcForm> saveModelList = jBoltTable.getSaveModelList(InventoryQcForm.class);
			List<InventoryQcForm> saveList = new ArrayList<>();
			saveModelList.forEach(inventoryQcForm -> {
            	inventoryQcForm.setIQcFormId(Long.parseLong(iQcFormId));
            	inventoryQcForm.setIQcFormName(iQcFormName);
            	inventoryQcForm.setICreateBy(user.getId());
            	inventoryQcForm.setCCreateName(user.getName());
            	inventoryQcForm.setDCreateTime(nowDate);
				inventoryQcForm.setIUpdateBy(user.getId());
				inventoryQcForm.setCUpdateName(user.getName());
				inventoryQcForm.setDUpdateTime(nowDate);
            	inventoryQcForm.setIOrgId(orgId);
            	inventoryQcForm.setCOrgCode(orgCode);
            	inventoryQcForm.setCOrgName(orgName);
            	saveList.add(inventoryQcForm);
			});
            batchSave(saveList);
        }

		if (jBoltTable.updateIsNotBlank()){
			List<InventoryQcForm> updateModelList = jBoltTable.getUpdateModelList(InventoryQcForm.class);
			List<InventoryQcForm> updateList = new ArrayList<>();
			updateModelList.forEach(inventoryQcForm -> {
				inventoryQcForm.setIQcFormId(Long.parseLong(iQcFormId));
				inventoryQcForm.setIQcFormName(iQcFormName);
				inventoryQcForm.setIUpdateBy(user.getId());
				inventoryQcForm.setCUpdateName(user.getName());
				inventoryQcForm.setDUpdateTime(nowDate);
				updateList.add(inventoryQcForm);
			});
			batchUpdate(updateList);
		}

		if (jBoltTable.deleteIsNotBlank()){
			deleteByIds(jBoltTable.getDelete());
		}
			return true;
		});
		return SUCCESS;
	}

    /**
     * 检验表格数据源
     * @param kv
     * @return
     */
	public List<Record> getFormList(Kv kv){
	    return dbTemplate("inventoryqcform.getFormList", kv).find();
    }

	/**
	 *行数据源
	 * @param kv
	 * @return
	 */
	public List<Record> listData(Kv kv){
        List<Record> records = dbTemplate("inventoryqcform.listData", kv).find();
        records.forEach(record ->{
            String inspectiontype = record.getStr("inspectiontype");
            if(StrKit.notBlank(inspectiontype))
            {
                List<Record> dic = new ArrayList<>();
                JBoltArrayUtil.listFrom(inspectiontype, ",").forEach(sn ->{
                    Record dicBySn = findDicBySn(sn);
                    dic.add(dicBySn);
                });
                String inspectionName =
                        dic.stream().map(m -> m.getStr("name")).collect(Collectors.joining(","));
                record.set("inspectionname", inspectionName);
            }
        });
        return records;
	}

	public Record findDicBySn(Object sn){

	    Kv kv = new Kv();
	    kv.set("sn", sn);
	    return dbTemplate("inventoryqcform.findDicBySn", kv).findFirst();
    }

    /**
     * 保存文件 并 保存到行数据里
     * @return
     */
    public Ret saveFileAndUpdateLine(List<UploadFile> files, String uploadPath, Long lineId){
        Ret ret = new Ret();
        tx(() -> {
            List<JboltFile> retFiles = new ArrayList<JboltFile>();
            JboltFile jboltFile = null;
            StringBuilder errorMsg = new StringBuilder();
            for (UploadFile uploadFile : files) {
                jboltFile = jboltFileService.saveJBoltFile(uploadFile, uploadPath,JboltFile.FILE_TYPE_ATTACHMENT);
                if (jboltFile != null) {
                    retFiles.add(jboltFile);
                } else {
                    errorMsg.append(uploadFile.getOriginalFileName() + "上传失败;");
                }
            }
            if (retFiles.size() == 0) {
                ret.setFail().set("msg",errorMsg.toString());
                return false;
            }

            // 保存文件 获取fileId
            Long fileId = jboltFile.getId();
            InventoryQcForm inventoryQcForm = findById(lineId);
            String cPics = inventoryQcForm.getCPics();
            if (cPics != null){
                cPics = cPics.concat(",").concat(Long.toString(fileId));
            } else {
                cPics = Long.toString(fileId);
            }
            inventoryQcForm.setCPics(cPics);
            inventoryQcForm.update();

            ret.setOk().set("data",retFiles);
            return true;
        });

        return ret;
    }

    /**
     * 删除文件信息
     * @param kv
     * @return
     */
    public Ret deleteFile(Kv kv) {
        boolean result = tx(() -> {
            Long lineId = kv.getLong("lineId");
            Long fileId = kv.getLong("fileId");
            JboltFile jboltFile = jboltFileService.findById(fileId);
            if(notOk(jboltFile)){
                return false;
            }
            Ret ret = jboltFileService.deleteById(fileId);
            boolean delete = JBoltUploadFileUtil.delete(jboltFile.getLocalPath());
            if (ret.isOk() && delete){
                InventoryQcForm inventoryQcForm = findById(lineId);
                String cPics = inventoryQcForm.getCPics();
                StringBuilder warranty = new StringBuilder();
                if (cPics != null){
                    String[] split = cPics.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String s = split[i];
                        if (!s.equals(fileId.toString())){
                            warranty = warranty.length() > 0 ?warranty.append(",").append(s):warranty.append(s);
                        }
                    }
                    inventoryQcForm.setCPics(warranty.length() > 0 ?warranty.toString():null);
                    return inventoryQcForm.update();
                }
            }
            return true;
        });
        if(result){
            return SUCCESS;
        }
        return FAIL;
    }
}
