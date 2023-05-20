package cn.rjtech.admin.rdstyle;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.bean.JsTreeBean;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.enums.SourceEnum;
import cn.rjtech.model.momdata.RdStyle;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收发类别 Service
 * @ClassName: RdStyleService
 * @author: WYX
 * @date: 2023-03-24 09:48
 */
public class RdStyleService extends BaseService<RdStyle> {

	private final RdStyle dao = new RdStyle().dao();

	@Override
	protected RdStyle dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<RdStyle> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param rdStyle
	 * @return
	 */
	public Ret save(RdStyle rdStyle) {
		if(rdStyle==null || isOk(rdStyle.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		ValidationUtils.assertNull(findBycSTCode(rdStyle.getCRdCode()), "收发类型编码重复");

		setRdStyleClass(rdStyle);
		//if(existsName(rdStyle.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rdStyle.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(rdStyle.getIautoid(), JBoltUserKit.getUserId(), rdStyle.getName());
		}
		return ret(success);
	}

	/**
	 * 查找收发类型编码
	 * @param cRdCode
	 * @return
	 */
	public RdStyle findBycSTCode(String cRdCode) {
		return findFirst(Okv.by("cRdCode", cRdCode).set("isDeleted", false), "iautoid", "asc");
	}

	/**
	 * 设置参数
	 * @param rdStyle
	 * @return
	 */
	private RdStyle setRdStyleClass(RdStyle rdStyle){
		rdStyle.setIsDeleted(false);
		Long userId = JBoltUserKit.getUserId();
		rdStyle.setICreateBy(userId);	//创建人ID
		rdStyle.setIUpdateBy(userId);	//更新人ID
		String userName = JBoltUserKit.getUserName();
		rdStyle.setCCreateName(userName);	//创建人名称
		rdStyle.setCUpdateName(userName);	//更新人名称
		Date date = new Date();
		rdStyle.setDCreateTime(date);	//创建时间
		rdStyle.setDUpdateTime(date);	//更新时间
        rdStyle.setISource(SourceEnum.MES.getValue());
		return rdStyle;
	}

	/**
	 * 更新
	 * @param rdStyle
	 * @return
	 */
	public Ret update(RdStyle rdStyle) {
		if(rdStyle==null || notOk(rdStyle.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		RdStyle dbRdStyle=findById(rdStyle.getIAutoId());
		if(dbRdStyle==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(rdStyle.getName(), rdStyle.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=rdStyle.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(rdStyle.getIautoid(), JBoltUserKit.getUserId(), rdStyle.getName());
		}
		return ret(success);
	}

	/**
	 * 删除 指定多个ID
	 * @param ids
	 * @return
	 */
	public Ret deleteByBatchIds(String ids) {
		return deleteByIds(ids,true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		return deleteById(id,true);
	}

	/**
	 * 删除数据后执行的回调
	 * @param rdStyle 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(RdStyle rdStyle, Kv kv) {
		//addDeleteSystemLog(rdStyle.getIautoid(), JBoltUserKit.getUserId(),rdStyle.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rdStyle 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(RdStyle rdStyle, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(rdStyle, kv);
	}

	/**
	 * 设置返回二开业务所属的关键systemLog的targetType 
	 * @return
	 */
	@Override
	protected int systemLogTargetType() {
		return ProjectSystemLogTargetType.NONE.getValue();
	}

	/**
	 * 切换bretail属性
	 */
	public Ret toggleBRetail(Long id) {
		return toggleBoolean(id, "bRetail");
	}

	/**
	 * 切换brdflag属性
	 */
	public Ret toggleBRdFlag(Long id) {
		return toggleBoolean(id, "bRdFlag");
	}

	/**
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "IsDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param rdStyle 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(RdStyle rdStyle,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(RdStyle rdStyle, String column, Kv kv) {
		//addUpdateSystemLog(rdStyle.getIautoid(), JBoltUserKit.getUserId(), rdStyle.getName(),"的字段["+column+"]值:"+rdStyle.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param rdStyle model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(RdStyle rdStyle, Kv kv) {
		//这里用来覆盖 检测RdStyle是否被其它表引用
		return null;
	}

	/**
	 * 得到后台分类数据树 包含所有数据
	 *
	 * @param checkedId 默认选中节点
	 * @param openLevel 打开级别
	 */
	public List<JsTreeBean> getMgrTree(Long checkedId, int openLevel) {
		List<RdStyle> uomclassList = find("select * from Bd_Rd_Style");
		List<JsTreeBean> jsTreeBeanList = new ArrayList<>();
		JsTreeBean parent = new JsTreeBean("1", "#", "收发类别", null, "", false);
		jsTreeBeanList.add(parent);
		for (RdStyle rdStyle : uomclassList) {
			Long id = rdStyle.getIAutoId();
			Object pid = rdStyle.getIPid();
			String text = "[" + rdStyle.getCRdCode() + "]" + rdStyle.getCRdName();
			String type = rdStyle.getCRdCode();
			JsTreeBean jsTreeBean = new JsTreeBean(id, pid, text, type, "", false);
			jsTreeBeanList.add(jsTreeBean);
		}
		return jsTreeBeanList;
	}

	public List<RdStyle> getTreeDatas(boolean onlyEnable, boolean asOptions) {
		List<RdStyle> rdStylesList = getMgrList();
		return this.convertToModelTree(rdStylesList, "iAutoId", "iPid", (p) -> this.notOk(p.getIPid()));
	}

	/**
	 * 获取分类数据中的所有后端分类数据
	 */
	public List<RdStyle> getMgrList() {
		return getrdstyle(true);
	}

	/**
	 * 获取分类数据
	 *
	 * @param isEnabled 启用禁用状态
	 */
	public List<RdStyle> getrdstyle(Boolean isEnabled) {
		Okv kv = Okv.by("isDeleted", false);
		return getCommonList(kv, "iAutoId", "asc");
	}

	/**
	 * 销售类型_新增_出库
	 */
	public List<RdStyle> getSaleType(boolean onlyEnable, boolean asOptions) {
		List<RdStyle> rdStylesList = getSaleTypeList();
		return this.convertToModelTree(rdStylesList, "iAutoId", "iPid", (p) -> this.notOk(p.getIPid()));
	}

	/**
	 * 获取分类数据中的所有后端分类数据
	 */
	public List<RdStyle> getSaleTypeList() {
		return getSaleType(true);
	}

	/**
	 * 获取分类数据
	 *
	 * @param isEnabled 启用禁用状态
	 */
	public List<RdStyle> getSaleType(Boolean isEnabled) {
		Okv kv = Okv.by("isDeleted", false).set("bRdFlag",0);
		return getCommonList(kv, "iAutoId", "asc");
	}

	/**
	 * 采购类型_新增_入库
	 */
	public List<RdStyle> getPurchaseType(boolean onlyEnable, boolean asOptions) {
		List<RdStyle> rdStylesList = getPurchaseTypeList();
		return this.convertToModelTree(rdStylesList, "iAutoId", "iPid", (p) -> this.notOk(p.getIPid()));
	}

	/**
	 * 获取分类数据中的所有后端分类数据
	 */
	public List<RdStyle> getPurchaseTypeList() {
		return getPurchaseType(true);
	}

	/**
	 * 获取分类数据
	 *
	 * @param isEnabled 启用禁用状态
	 */
	public List<RdStyle> getPurchaseType(Boolean isEnabled) {
		Okv kv = Okv.by("isDeleted", false).set("bRdFlag",1);
		return getCommonList(kv, "iAutoId", "asc");
	}


}