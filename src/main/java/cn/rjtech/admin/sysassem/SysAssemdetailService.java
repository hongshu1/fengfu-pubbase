package cn.rjtech.admin.sysassem;

import cn.rjtech.util.ValidationUtils;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SysAssemdetail;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * 组装拆卸及形态转换单明细
 * @ClassName: SysAssemdetailService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-09 09:47
 */
public class SysAssemdetailService extends BaseService<SysAssemdetail> {
	private final SysAssemdetail dao=new SysAssemdetail().dao();
	@Override
	protected SysAssemdetail dao() {
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
     * @param SourceType 来源类型
     * @param AssemType 转换状态;转换前 及转换后
     * @param TrackType 跟单类型
     * @param IsDeleted 删除状态：0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SysAssemdetail> getAdminDatas(int pageNumber, int pageSize, String keywords, String SourceType, String AssemType, String TrackType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("SourceType",SourceType);
        sql.eq("AssemType",AssemType);
        sql.eq("TrackType",TrackType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"WhCodeName", "WhCodeHName");
        //排序
        sql.desc("AutoID");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param sysAssemdetail
	 * @return
	 */
	public Ret save(SysAssemdetail sysAssemdetail) {
		if(sysAssemdetail==null || isOk(sysAssemdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//if(existsName(sysAssemdetail.getName())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysAssemdetail.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param sysAssemdetail
	 * @return
	 */
	public Ret update(SysAssemdetail sysAssemdetail) {
		if(sysAssemdetail==null || notOk(sysAssemdetail.getAutoID())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SysAssemdetail dbSysAssemdetail=findById(sysAssemdetail.getAutoID());
		if(dbSysAssemdetail==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		//if(existsName(sysAssemdetail.getName(), sysAssemdetail.getAutoID())) {return fail(JBoltMsg.DATA_SAME_NAME_EXIST);}
		boolean success=sysAssemdetail.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param sysAssemdetail 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SysAssemdetail sysAssemdetail, Kv kv) {
		//addDeleteSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(),sysAssemdetail.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param sysAssemdetail model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SysAssemdetail sysAssemdetail, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(SysAssemdetail sysAssemdetail, String column, Kv kv) {
		//addUpdateSystemLog(sysAssemdetail.getAutoID(), JBoltUserKit.getUserId(), sysAssemdetail.getName(),"的字段["+column+"]值:"+sysAssemdetail.get(column));
		/**
		switch(column){
		    case "IsDeleted":
		        break;
		}
		*/
		return null;
	}
	public List<Record> findEditTableDatas(Kv para) {
		ValidationUtils.notNull(para.getLong("masid"), JBoltMsg.PARAM_ERROR);
		List<Record> records = dbTemplate("sysassem.dList", para).find();
		return records;
	}

	/**
	 * 批量删除主从表
	 */
	public Ret deleteRmRdByIds(String ids) {
		tx(() -> {
			String[] split = ids.split(",");
			for(String s : split){
				updateColumn(s, "isdeleted", true);
			}
			return true;
		});
		return ret(true);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Ret delete(Long id) {
		updateColumn(id, "isdeleted", true);
		return ret(true);
	}
}