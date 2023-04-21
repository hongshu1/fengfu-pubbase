package cn.rjtech.admin.annualorderd;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;

import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.rjtech.model.momdata.AnnualOrderD;
import cn.rjtech.util.ValidationUtils;
/**
 * 年度计划订单年汇总 Service
 * @ClassName: AnnualOrderDService
 * @author: heming
 * @date: 2023-03-28 17:06
 */
public class AnnualOrderDService extends BaseService<AnnualOrderD> {

	private final AnnualOrderD dao = new AnnualOrderD().dao();

	@Override
	protected AnnualOrderD dao() {
		return dao;
	}

	/**
	 * 后台管理分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param keywords
	 * @return
	 */
	public Page<AnnualOrderD> paginateAdminDatas(int pageNumber, int pageSize, String keywords) {
		return paginateByKeywords("iAutoId","DESC", pageNumber, pageSize, keywords, "iAutoId");
	}

	/**
	 * 保存
	 * @param annualOrderD
	 * @return
	 */
	public Ret save(AnnualOrderD annualOrderD) {
		if(annualOrderD==null || isOk(annualOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(annualOrderD.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=annualOrderD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(annualOrderD.getIautoid(), JBoltUserKit.getUserId(), annualOrderD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param annualOrderD
	 * @return
	 */
	public Ret update(AnnualOrderD annualOrderD) {
		if(annualOrderD==null || notOk(annualOrderD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		AnnualOrderD dbAnnualOrderD=findById(annualOrderD.getIAutoId());
		if(dbAnnualOrderD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(annualOrderD.getName(), annualOrderD.getIautoid())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=annualOrderD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(annualOrderD.getIautoid(), JBoltUserKit.getUserId(), annualOrderD.getName());
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
	 * @param annualOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(AnnualOrderD annualOrderD, Kv kv) {
		//addDeleteSystemLog(annualOrderD.getIautoid(), JBoltUserKit.getUserId(),annualOrderD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param annualOrderD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanDelete(AnnualOrderD annualOrderD, Kv kv) {
		//如果检测被用了 返回信息 则阻止删除 如果返回null 则正常执行删除
		return checkInUse(annualOrderD, kv);
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
	 * 切换isdeleted属性
	 */
	public Ret toggleIsDeleted(Long id) {
		return toggleBoolean(id, "isDeleted");
	}

	/**
	 * 检测是否可以toggle操作指定列
	 * @param annualOrderD 要toggle的model
	 * @param column 操作的哪一列
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkCanToggle(AnnualOrderD annualOrderD,String column, Kv kv) {
		//没有问题就返回null  有问题就返回提示string 字符串
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(AnnualOrderD annualOrderD, String column, Kv kv) {
		//addUpdateSystemLog(annualOrderD.getIautoid(), JBoltUserKit.getUserId(), annualOrderD.getName(),"的字段["+column+"]值:"+annualOrderD.get(column));
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param annualOrderD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(AnnualOrderD annualOrderD, Kv kv) {
		//这里用来覆盖 检测AnnualOrderD是否被其它表引用
		return null;
	}

	public List<Record> findEditTableDatas(Kv para) {
		ValidationUtils.notNull(para.getLong("iAnnualOrderMid"), JBoltMsg.PARAM_ERROR);
		return dbTemplate("annualorderd.findEditTableDatas",para).find();
	}

    public List<AnnualOrderD> findByMid(Long iAutoId) {
		return find(selectSql().eq("iAnnualOrderMid", iAutoId));
    }
}