package cn.rjtech.admin.exch;

import cn.hutool.core.util.StrUtil;
import com.jfinal.plugin.activerecord.Page;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.Exch;
/**
 * 汇率档案
 * @ClassName: ExchService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-05-29 13:45
 */
public class ExchService extends BaseService<Exch> {
	private final Exch dao=new Exch().dao();
	@Override
	protected Exch dao() {
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
     * @param itype 汇率类型
     * @param isDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<Exch> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer itype, Boolean isDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("itype",itype);
        sql.eqBooleanToChar("isDeleted",isDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cexch_name", "cCreateName", "cUpdateName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * toggle操作执行后的回调处理
	 */
	@Override
	protected String afterToggleBoolean(Exch exch, String column, Kv kv) {
		//addUpdateSystemLog(exch.getIAutoId(), JBoltUserKit.getUserId(), exch.getName(),"的字段["+column+"]值:"+exch.get(column));
		/**
		switch(column){
		    case "isDeleted":
		        break;
		}
		*/
		return null;
	}
	
	
	public Exch getNameByLatestExch(Long orgId, String name){
		if (StrUtil.isBlank(name)){
			return null;
		}
		String sqlStr ="SELECT TOP 1 " +
				"ex.iYear, " +
				"ex.iperiod, " +
				"ex.nflat " +
				"FROM " +
				"Bd_Exch ex " +
				"WHERE " +
				"ex.iOrgId = ? " +
				"AND ex.cexch_name = ? " +
				"AND ex.isDeleted = '0' " +
				"ORDER BY " +
				"iYear DESC, " +
				"iperiod DESC";
		return findFirst(sqlStr, orgId, name);
	}
}
