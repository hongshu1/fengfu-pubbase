package cn.rjtech.admin.subcontractorderdbatchversion;

import cn.hutool.core.date.DateUtil;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.kit.JBoltUserKit;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SubcontractOrderDBatchVersion;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购/委外管理-采购现品票版本记录
 * @ClassName: SubcontractOrderDBatchVersionService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-04-25 21:31
 */
public class SubcontractOrderDBatchVersionService extends BaseService<SubcontractOrderDBatchVersion> {
	private final SubcontractOrderDBatchVersion dao=new SubcontractOrderDBatchVersion().dao();
	
	@Override
	protected SubcontractOrderDBatchVersion dao() {
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
	 * @return
	 */
	public Page<SubcontractOrderDBatchVersion> getAdminDatas(int pageNumber, int pageSize, String keywords) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.like("cCreateName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param subcontractOrderDBatchVersion
	 * @return
	 */
	public Ret save(SubcontractOrderDBatchVersion subcontractOrderDBatchVersion) {
		if(subcontractOrderDBatchVersion==null || isOk(subcontractOrderDBatchVersion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(subcontractOrderDBatchVersion.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderDBatchVersion.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(subcontractOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatchVersion.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param subcontractOrderDBatchVersion
	 * @return
	 */
	public Ret update(SubcontractOrderDBatchVersion subcontractOrderDBatchVersion) {
		if(subcontractOrderDBatchVersion==null || notOk(subcontractOrderDBatchVersion.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SubcontractOrderDBatchVersion dbSubcontractOrderDBatchVersion=findById(subcontractOrderDBatchVersion.getIAutoId());
		if(dbSubcontractOrderDBatchVersion==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(subcontractOrderDBatchVersion.getName(), subcontractOrderDBatchVersion.getIAutoId())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=subcontractOrderDBatchVersion.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(subcontractOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(), subcontractOrderDBatchVersion.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param subcontractOrderDBatchVersion 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SubcontractOrderDBatchVersion subcontractOrderDBatchVersion, Kv kv) {
		//addDeleteSystemLog(subcontractOrderDBatchVersion.getIAutoId(), JBoltUserKit.getUserId(),subcontractOrderDBatchVersion.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param subcontractOrderDBatchVersion model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SubcontractOrderDBatchVersion subcontractOrderDBatchVersion, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	
	public SubcontractOrderDBatchVersion createBatchVersion(Long subcontractOrderMid, Long batchId, Long inventoryId, Date planDate, String version, String cSourceVersion, String barCode, BigDecimal qty, BigDecimal sourceQty){
		SubcontractOrderDBatchVersion subcontractOrderDBatchVersion = new SubcontractOrderDBatchVersion();
		subcontractOrderDBatchVersion.setISubcontractOrderdBatchId(batchId);
		subcontractOrderDBatchVersion.setISubcontractOrderMid(subcontractOrderMid);
		subcontractOrderDBatchVersion.setIInventoryId(inventoryId);
		subcontractOrderDBatchVersion.setDPlanDate(planDate);
		subcontractOrderDBatchVersion.setCVersion(version);
		subcontractOrderDBatchVersion.setCSourceVersion(cSourceVersion);
		subcontractOrderDBatchVersion.setCBarcode(barCode);
		subcontractOrderDBatchVersion.setCSourceBarcode(barCode);
		subcontractOrderDBatchVersion.setIQty(qty);
		subcontractOrderDBatchVersion.setISourceQty(sourceQty);
		subcontractOrderDBatchVersion.setICreateBy(JBoltUserKit.getUserId());
		subcontractOrderDBatchVersion.setCCreateName(JBoltUserKit.getUserName());
		subcontractOrderDBatchVersion.setDCreateTime(DateUtil.date());
		return subcontractOrderDBatchVersion;
	}
	
	public Page<Record> findBySubcontractOrderMid(int pageNumber, int pageSize, Kv kv){
		return dbTemplate("subcontractorderdbatchversion.findBySubcontractOrderMid", kv).paginate(pageNumber, pageSize);
	}

}
