package cn.rjtech.admin.fitem;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.Fitem;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础档案-项目大类
 * @ClassName: FitemService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-03 11:02
 */
public class FitemService extends BaseService<Fitem> {
	private final Fitem dao=new Fitem().dao();
	@Override
	protected Fitem dao() {
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
	public Page<Record> getAdminDatas(int pageNumber, int pageSize, Kv paras) {

          Kv kv= Kv.by("iautoid",paras.getLong("fitemid"))
				.set("citem_name",paras.getStr("citemname"))
				.set("citem_class",paras.getStr("citemclass"));
		return  dbTemplate("fitem.selectFitem", kv).paginate(pageNumber,pageSize);
	}



	public List<Record> selectFitem(){
	     return dbTemplate("fitem.selectFitem").find();
	}



	/**
	 * 得到后台分类数据树 包含所有数据
	 *
	 *
	 * @param openLevel 打开级别
	 */
	public List<JsTreeBean> getMgrTree(int openLevel, String sn) {

		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
		List<Record> records = dbTemplate("fitem.selectFitem", Kv.by("sn", sn)).find();

		for (Record record : records) {
			Long id = record.getLong("iautoid");
			String text = record.getStr("citem_name");
			String type =  record.getStr("citem_name");
			JsTreeBean jsTreeBean = new JsTreeBean(id, "#", text, type, "", false);
			jsTreeBeanList.add(jsTreeBean);
		}
		System.out.println(jsTreeBeanList);
		return jsTreeBeanList;
	}


	/**
	 * 保存
	 * @param fitem
	 * @return
	 */
	public Ret save(Fitem fitem) {
		if(fitem==null || isOk(fitem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=fitem.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(fitem.getIAutoId(), JBoltUserKit.getUserId(), fitem.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param fitem
	 * @return
	 */
	public Ret update(Fitem fitem) {
		if(fitem==null || notOk(fitem.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Fitem dbFitem=findById(fitem.getIAutoId());
		if(dbFitem==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=fitem.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(fitem.getIAutoId(), JBoltUserKit.getUserId(), fitem.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param fitem 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(Fitem fitem, Kv kv) {
		//addDeleteSystemLog(fitem.getIAutoId(), JBoltUserKit.getUserId(),fitem.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitem model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(Fitem fitem, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

/*	public List<fitem.sql> getTreeDatas(){
		List<fitem.sql> datas=find(selectSql());
		return convertToModelTree(datas, "iAutoId", "pid", (p)->notOk(p.));
	}*/

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Fitem fitem, String column, Kv kv) {
		//addUpdateSystemLog(fitem.getIAutoId(), JBoltUserKit.getUserId(), fitem.getName(),"的字段["+column+"]值:"+fitem.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}

}