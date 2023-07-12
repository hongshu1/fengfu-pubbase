package cn.rjtech.admin.inventoryclass;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltSnowflakeKit;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.cache.CusFieldsMappingdCache;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.InventoryClass;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 物料建模-存货分类
 * @ClassName: InventoryClassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-23 09:02
 */
public class InventoryClassService extends BaseService<InventoryClass> {
    
	private final InventoryClass dao=new InventoryClass().dao();
    
	@Override
	protected InventoryClass dao() {
		return dao;
	}

	@Override
    protected int systemLogTargetType() {
        return ProjectSystemLogTargetType.NONE.getValue();
    }

	/**
	 * 后台管理数据查询
	 * @param keywords   关键词
	 */
	public List<InventoryClass> getAdminDatas(String keywords) {
	    //创建sql对象
	    Sql sql = selectSql();
        //关键词模糊查询
        sql.likeMulti(keywords,"cInvCCode", "cInvCName");
        //排序
        sql.desc("iAutoId");
		return find(sql);
	}

	/**
	 * 保存
	 */
	public Ret save(InventoryClass inventoryClass) {
		if(inventoryClass==null || isOk(inventoryClass.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		setInventoryClass(inventoryClass);
		//if(existsName(inventoryClass.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryClass.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(inventoryClass.getIAutoId(), JBoltUserKit.getUserId(), inventoryClass.getName());
		}
		return ret(success);
	}

	/**
	 * 设置参数
	 */
	private InventoryClass setInventoryClass(InventoryClass inventoryClass){
		inventoryClass.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		inventoryClass.setICreateBy(userId);
		inventoryClass.setIUpdateBy(userId);
		String userName = JBoltUserKit.getUserName();
		inventoryClass.setCCreateName(userName);
		inventoryClass.setCUpdateName(userName);
		Date date = new Date();
		inventoryClass.setDCreateTime(date);
		inventoryClass.setDUpdateTime(date);
		return inventoryClass;
	}

	/**
	 * 更新
	 */
	public Ret update(InventoryClass inventoryClass) {
		if(inventoryClass==null || notOk(inventoryClass.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		InventoryClass dbInventoryClass=findById(inventoryClass.getIAutoId());
		if(dbInventoryClass==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(inventoryClass.getName(), inventoryClass.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=inventoryClass.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(inventoryClass.getIAutoId(), JBoltUserKit.getUserId(), inventoryClass.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param inventoryClass 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(InventoryClass inventoryClass, Kv kv) {
		//addDeleteSystemLog(inventoryClass.getIAutoId(), JBoltUserKit.getUserId(),inventoryClass.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param inventoryClass model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(InventoryClass inventoryClass, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<InventoryClass> getTreeDatas(){
		List<InventoryClass> datas=find(selectSql());
		return convertToModelTree(datas, "id", "pid", (p)->notOk(p.getIPid()));
	}

	/**
     * 生成excel导入使用的模板
     */
    public JBoltExcel getImportExcelTpl() {
        return JBoltExcel
                //创建
                .create()
                .setSheets(
                        JBoltExcelSheet.create()
                        //设置列映射 顺序 标题名称 不处理别名
                        .setHeaders(1,false,
                                JBoltExcelHeader.create("分类编码",15),
                                JBoltExcelHeader.create("分类名称",15),
                                JBoltExcelHeader.create("父级Id",15)
                                )
                    );
    }

    /**
	 * 读取excel文件
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
                        JBoltExcelHeader.create("cInvCCode","分类编码"),
                        JBoltExcelHeader.create("cInvCName","分类名称"),
                        JBoltExcelHeader.create("iPid","父级Id")
                        )
				//从第三行开始读取
				.setDataStartRow(2)
				);
		//从指定的sheet工作表里读取数据
		List<InventoryClass> inventoryClasss=JBoltExcelUtil.readModels(jBoltExcel,1, InventoryClass.class,errorMsg);
		if(notOk(inventoryClasss)) {
			if(errorMsg.length()>0) {
				return fail(errorMsg.toString());
			}else {
				return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
			}
		}
		for (InventoryClass classs : inventoryClasss) {
			setInventoryClass(classs);
		}
		//执行批量操作
		boolean success=tx(() -> {
            batchSave(inventoryClasss);
            return true;
        });

		if(!success) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL);
		}
		return SUCCESS;
	}

    /**
	 * 生成要导出的Excel
	 */
	public JBoltExcel exportExcel(List<InventoryClass> datas) {
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
                            JBoltExcelHeader.create("cInvCCode","分类编码",15),
                            JBoltExcelHeader.create("cInvCName","分类名称",15)
                            )
    		    	//设置导出的数据源 来自于数据库查询出来的Model List
    		    	.setModelDatas(2,datas)
    		    );
	}

	/**
	 * 获取树状数据源
	 */
	public List<JsTreeBean> getMgrTree(Long selectId, Integer openLevel) {
		List<InventoryClass> inventoryClassList = find("select * from Bd_InventoryClass where isdeleted='0' order by cInvCCode asc ");
		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
		JsTreeBean parent = new JsTreeBean("1","#","存货分类",null,"",false);
		jsTreeBeanList.add(parent);
		for (InventoryClass inventoryClass : inventoryClassList){
			Long id = inventoryClass.getIAutoId();
			String pid="1";
			String text = "["+ inventoryClass.getCInvCCode()+"]" + inventoryClass.getCInvCName();
			String type = inventoryClass.getCInvCCode();
			JsTreeBean jsTreeBean = new JsTreeBean(id,pid,text,type,"",false);
			jsTreeBean.setId(inventoryClass.getIAutoId()+"");
			if(inventoryClass.getIPid() != null){
				jsTreeBean.setParent(inventoryClass.getIPid()+"");
				//jsTreeBean.setChildren(true);
			}
			jsTreeBeanList.add(jsTreeBean);
		}
		return jsTreeBeanList;
	}

	/**
	 * 查所有
	 */
	public List<Record> selectClassList(Kv kv) {
		return dbTemplate("inventoryclass.list",kv).find();
	}

    public List<JsTreeBean> getTreeList() {
        List<JsTreeBean> treeBeans = new ArrayList<>();
        // 根节点
        treeBeans.add(new JsTreeBean(0, "#", "存货分类档案", true, "root_opened", true));
        appendSubTree(treeBeans, 0L);
        return treeBeans;
    }
    private void appendSubTree(List<JsTreeBean> treeBeans, Long pid) {
    	List<Record> subList =  getSubList(pid);
    	if(CollUtil.isEmpty(subList)) return;
        for (Record row : subList) {
            // 当前节点
            treeBeans.add(new JsTreeBean(row.getLong("iautoid"), pid, row.getStr("cinvcname") + "(" + row.getStr("cinvccode") + ")", "default", null, true));
            // 追加子节点
            appendSubTree(treeBeans, row.getLong("iautoid"));
        }
    }
    private List<Record> getSubList(Long pid) {
        Okv para = Okv.by("pid", pid);
        para.set("iorgid",getOrgId());
        return dbTemplate("inventoryclass.getSubList", para).find();
    }

	/**
	 * 从系统导入字段配置，获得导入的数据
	 */
	public Ret importExcelClass(File file) {
		List<Record> records = CusFieldsMappingdCache.ME.getImportRecordsByTableName(file, table());
		if (notOk(records)) {
			return fail(JBoltMsg.DATA_IMPORT_FAIL_EMPTY);
		}

        Date now = new Date();

		for (Record record : records) {

			if (StrUtil.isBlank(record.getStr("cInvCCode"))) {
				return fail("存货分类编码不能为空");
			}
			if (StrUtil.isBlank(record.getStr("cInvCName"))) {
				return fail("存货分类名称不能为空");
			}

            if (notNull(record.get("iPid"))) {
                InventoryClass inventoryClass = findFirst(selectSql().eq("cInvCCode", record.getStr("iPid")).or().eq("cInvCName", record.getStr("iPid")));
                Long iPid = Optional.ofNullable(inventoryClass).map(InventoryClass::getIAutoId).orElse(null);
                record.set("iPid", iPid);
            }

			record.set("iAutoId", JBoltSnowflakeKit.me.nextId());
			record.set("iOrgId", getOrgId());
			record.set("cOrgCode", getOrgCode());
			record.set("cOrgName", getOrgName());
			record.set("iSourceId", record.getStr("cInvCCode"));
			record.set("iCreateBy", JBoltUserKit.getUserId());
			record.set("dCreateTime", now);
			record.set("iSource", SourceEnum.MES.getValue());
			record.set("cCreateName", JBoltUserKit.getUserName());
			record.set("isEnabled",1);
			record.set("isDeleted",0);
			record.set("iUpdateBy", JBoltUserKit.getUserId());
			record.set("dUpdateTime", now);
			record.set("cUpdateName", JBoltUserKit.getUserName());
		}

		// 执行批量操作
		tx(() -> {
			batchSaveRecords(records);
			return true;
		});
		return SUCCESS;
	}
    
}