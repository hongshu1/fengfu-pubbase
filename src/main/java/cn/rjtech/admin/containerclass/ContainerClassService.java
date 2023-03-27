package cn.rjtech.admin.containerclass;

import cn.jbolt.core.kit.JBoltUserKit;
import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.ContainerClass;

import java.util.Date;


/**
 * 分类管理
 * @ClassName: ContainerClassService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-03-22 16:16
 */
public class ContainerClassService extends BaseService<ContainerClass> {
	private final ContainerClass dao=new ContainerClass().dao();
	@Override
	protected ContainerClass dao() {
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
	public Page<ContainerClass> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().le("IsDeleted",0).page(pageNumber,pageSize);

        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param containerClass
	 * @return
	 */
	public Ret save(ContainerClass containerClass) {
		if(containerClass==null || isOk(containerClass.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}

		ValidationUtils.assertNull(findByConClassCode(containerClass.getCContainerClassCode()),"分类编码重复！");
		//if(existsName(containerClass.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		//创建信息
		containerClass.setICreateBy(JBoltUserKit.getUserId());
		containerClass.setCCreateName(JBoltUserKit.getUserName());
		containerClass.setDCreateTime(new Date());

		//更新信息
		containerClass.setIUpdateBy(JBoltUserKit.getUserId());
		containerClass.setCUpdateName(JBoltUserKit.getUserName());
		containerClass.setDUpdateTime(new Date());

		//组织信息
		containerClass.setCOrgCode(getOrgCode());
		containerClass.setCOrgName(getOrgName());
		containerClass.setIOrgId(getOrgId());
		boolean success=containerClass.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(), containerClass.getName());
		}
		return ret(success);
	}

	/**
	 * 查找容器类别编码
	 * @param cContainerClassCode
	 * @return
	 */
	public ContainerClass findByConClassCode(String cContainerClassCode) {
		return findFirst(Okv.by("cContainerClassCode", cContainerClassCode).set("isDeleted", false), "iautoid", "asc");

	}

	/**
	 * 更新
	 * @param containerClass
	 * @return
	 */
	public Ret update(ContainerClass containerClass) {
		if(containerClass==null || notOk(containerClass.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ContainerClass dbContainerClass=findById(containerClass.getIAutoId());
		if(dbContainerClass==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}


		containerClass.setIUpdateBy(JBoltUserKit.getUserId());
		containerClass.setCUpdateName(JBoltUserKit.getUserName());
		containerClass.setDUpdateTime(new Date());

		//查询
		ContainerClass byConClassCode = findByConClassCode(containerClass.getCContainerClassCode());

		//编码重复判断
		if (byConClassCode != null && !containerClass.getIAutoId().equals(byConClassCode.getIAutoId())) {
			ValidationUtils.assertNull(byConClassCode.getCContainerClassCode(), "分类编码重复！");
		}
		//if(existsName(containerClass.getName(), containerClass.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=containerClass.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(), containerClass.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param containerClass 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ContainerClass containerClass, Kv kv) {
		//addDeleteSystemLog(containerClass.getIAutoId(), JBoltUserKit.getUserId(),containerClass.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param containerClass model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ContainerClass containerClass, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public Ret delete(Long id) {
		return toggleBoolean(id, "isDeleted");

	}
}