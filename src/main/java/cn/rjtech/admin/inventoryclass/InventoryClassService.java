package cn.rjtech.admin.inventoryclass;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.poi.excel.JBoltExcel;
import cn.jbolt.core.poi.excel.JBoltExcelHeader;
import cn.jbolt.core.poi.excel.JBoltExcelSheet;
import cn.jbolt.core.poi.excel.JBoltExcelUtil;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.InventoryClass;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * @return
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
	 * @param inventoryClass
	 * @return
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
	 * @param inventoryClass
	 * @return
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
	 * @param inventoryClass
	 * @return
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
	 * @return
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
	 * @return
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
                                JBoltExcelHeader.create("分类编码",15),
                                JBoltExcelHeader.create("分类名称",15),
                                JBoltExcelHeader.create("父级Id",15)
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
		boolean success=tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				batchSave(inventoryClasss);
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
	 * @param selectId
	 * @param openLevel
	 * @return
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
	 * @param kv
	 * @return
	 */
	public List<Record> selectClassList(Kv kv) {
		return dbTemplate("inventoryclass.list",kv).find();
	}

    public List<JsTreeBean> getTreeList() {
        List<JsTreeBean> treeBeans = new ArrayList<>();
        // 根节点
        treeBeans.add(new JsTreeBean(0, "#", "存货分类档案", true, "root_opened", true));
        appendSubTree(treeBeans, 1, "0");
        return treeBeans;
    }
    private void appendSubTree(List<JsTreeBean> treeBeans, int grade, String pcode) {
        for (Record row : getSubList(grade, pcode)) {
            // 当前节点
            treeBeans.add(new JsTreeBean(row.getStr("cinvccode"), pcode, row.getStr("cinvcname") + "(" + row.getStr("cinvccode") + ")", row.getBoolean("binvcend") ? "node" : "default", null, true));
            // 追加子节点
            if (!row.getBoolean("binvcend")) {
                appendSubTree(treeBeans, grade + 1, row.getStr("cinvccode"));
            }
        }
    }
    private List<Record> getSubList(int grade, String pcode) {
        Okv para = Okv.by("iinvcgrade", grade)
                .set("cinvccode", pcode);
        return dbTemplate("inventoryclass.getSubList", para).find();
    }
}