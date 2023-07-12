package cn.rjtech.admin.fitemss97;

import cn.hutool.core.util.ObjUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.model.User;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.Fitemss97;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目管理大类项目主目录 Service
 * @ClassName: Fitemss97Service
 * @author: heming
 * @date: 2023-03-27 09:29
 */
public class Fitemss97Service extends BaseService<Fitemss97> {

	private final Fitemss97 dao = new Fitemss97().dao();

	@Override
	protected Fitemss97 dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param
	 * @return
	 */
	public Page<Record> paginateAdminDatas(int pageNumber, int pageSize, Kv para) {
		para.set("iorgid",getOrgId());
		return dbTemplate("fitemss97.paginateAdminDatas",para).paginate(pageNumber, pageSize);
	}



	/**
	 * 得到后台分类数据树 包含所有数据
	 *
	 *
	 * @param openLevel 打开级别
	 */
	public List<JsTreeBean> getMgrTree( int openLevel,String sn) {
		/**
		 * 项目大类目录
		 */
		List<Record> records = dbTemplate("fitem.selectFitem",Kv.by("sn",sn)).find();
		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
		for (Record record:records){
			JsTreeBean parent = new JsTreeBean(record.getLong("iautoid"), "#", record.getStr("citem_name"), null, "", false);
			jsTreeBeanList.add(parent);}
			List<Record> subRecords = dbTemplate("fitemss97.findsub",Kv.by("sn",sn)).find();
			for (Record subRecord : subRecords) {
				Long id = subRecord.getLong("iAutoId");
				Object pid = subRecord.getStr("Ipid");
				String text = "[" + subRecord.getStr("citemcCode") + "]" + subRecord.getStr("citemCname");
				String type = subRecord.getStr("citemCcode");
				JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
				jsTreeBeanList.add(jsTreeBean);
			}
		return jsTreeBeanList;
	}


	public List<Fitemss97> getTreeDatas(boolean onlyEnable, boolean asOptions) {
		List<Fitemss97> fitemss97List = getMgrList();
		for (Fitemss97 fitemss97 : fitemss97List){
			String iSourceId = fitemss97.getStr("iSourceId");
			fitemss97.set("iSourceId",Long.valueOf(iSourceId));
		}
		List<Fitemss97> fitemss97s = this.convertToModelTree(fitemss97List, "iAutoId", "iSourceId", (p) -> this.notOk(Long.valueOf(p.getISourceId())));
		return fitemss97s;
	}

	/**
	 * 获取分类数据中的所有后端分类数据
	 */
	public List<Fitemss97> getMgrList() {
		return getrdstyle(true);
	}

	/**
	 * 获取分类数据
	 *
	 *
	 */
	public List<Fitemss97> getrdstyle(Boolean isEnabled) {
		Okv kv = Okv.by("isDeleted", false);
		return getCommonList( kv,"iAutoId", "asc");
	}

	public Record selectFitemss97(Kv kv) {
        Record first = dbTemplate("fitemss97.findsub", Kv.by("iautoid", kv.getStr("iautoid"))).findFirst();
        if (ObjUtil.isNull(first)) {
            return dbTemplate("fitem.selectFitem", Kv.by("iautoid", kv.getStr("iautoid"))).findFirst();
        } else {
            return first;
        }
	}

	/**
	 * 保存
	 */
	public Ret save(Fitemss97 fitemss97) {
		if(fitemss97==null || isOk(fitemss97.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		fitemss97.setIOrgId(getOrgId());
		fitemss97.setCOrgCode(getOrgCode());
		fitemss97.setCOrgName(getOrgName());
		fitemss97.setICreateBy(user.getId());
		fitemss97.setCCreateName(user.getName());
		fitemss97.setDCreateTime(now);
		fitemss97.setIUpdateBy(user.getId());
		fitemss97.setCUpdateName(user.getName());
		fitemss97.setDUpdateTime(now);
		fitemss97.setISource(SourceEnum.MES.getValue());
		fitemss97.setIsDeleted(false);
		boolean success=fitemss97.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(fitemss97.getIautoid(), JBoltUserKit.getUserId(), fitemss97.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 */
	public Ret update(Fitemss97 fitemss97) {
		if(fitemss97==null || notOk(fitemss97.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		Fitemss97 dbFitemss97=findById(fitemss97.getIAutoId());
		if(dbFitemss97==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		User user = JBoltUserKit.getUser();
		Date now = new Date();
		fitemss97.setIUpdateBy(user.getId());
		fitemss97.setCUpdateName(user.getName());
		fitemss97.setDUpdateTime(now);
		boolean success=fitemss97.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(fitemss97.getIautoid(), JBoltUserKit.getUserId(), fitemss97.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 */
	public Ret delete(Long id) {
		return updateColumn(id, "isdeleted", true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param fitemss97 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	protected String afterDelete(Fitemss97 fitemss97, Kv kv) {
		//addDeleteSystemLog(fitemss97.getIautoid(), JBoltUserKit.getUserId(),fitemss97.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97 要删除的model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanDelete(Fitemss97 fitemss97, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(fitemss97, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 切换bclose属性
	 */
	public Ret toggleBclose(Long id) {
		return toggleBoolean(id, "bclose");
	}
	/**
	 * 切换iotherused属性
	 */
	public Ret toggleIotherused(Long id) {
		return toggleBoolean(id, "iotherused");
	}
	/**
	 * 检测是否可以toggle操作指定列
	 * @param fitemss97 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkCanToggle(Fitemss97 fitemss97,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Fitemss97 fitemss97, String column, Kv kv) {
		//addUpdateSystemLog(fitemss97.getIautoid(), JBoltUserKit.getUserId(), fitemss97.getName(),"的字段["+column+"]值:"+fitemss97.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param fitemss97 model
	 * @param kv 携带额外参数一般用不上
	 */
	@Override
	public String checkInUse(Fitemss97 fitemss97, Kv kv) {
		//这里用来覆盖 检测Fitemss97是否被其它表引用
		return null;
	}

}