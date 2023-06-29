package cn.rjtech.admin.spotcheckformm;

import com.jfinal.plugin.activerecord.Page;
import java.util.Date;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.jbolt.core.service.base.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Okv;
import com.jfinal.kit.Ret;
import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.rjtech.model.momdata.SpotCheckFormM;
/**
 * 始业点检表管理
 * @ClassName: SpotCheckFormMService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:16
 */
public class SpotCheckFormMService extends BaseService<SpotCheckFormM> {
	private final SpotCheckFormM dao=new SpotCheckFormM().dao();
	@Override
	protected SpotCheckFormM dao() {
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
     * @param iType 类型;1.首末点检表 2.首中末点检表
     * @param IsDeleted 删除状态;0. 未删除 1. 已删除
	 * @return
	 */
	public Page<SpotCheckFormM> getAdminDatas(int pageNumber, int pageSize, String keywords, Integer iType, Boolean IsDeleted) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        sql.eqBooleanToChar("IsDeleted",IsDeleted);
        //关键词模糊查询
        sql.likeMulti(keywords,"cOrgName", "cOperationName", "cPersonName", "cCreateName", "cUpdateName", "cAuditName");
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param spotCheckFormM
	 * @return
	 */
	public Ret save(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || isOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormM.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormM
	 * @return
	 */
	public Ret update(SpotCheckFormM spotCheckFormM) {
		if(spotCheckFormM==null || notOk(spotCheckFormM.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormM dbSpotCheckFormM=findById(spotCheckFormM.getIAutoId());
		if(dbSpotCheckFormM==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormM.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormM.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormM 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormM spotCheckFormM, Kv kv) {
		//addDeleteSystemLog(spotCheckFormM.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormM.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormM model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormM spotCheckFormM, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}