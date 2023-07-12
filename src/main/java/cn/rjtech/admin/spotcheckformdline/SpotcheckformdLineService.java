package cn.rjtech.admin.spotcheckformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpotcheckformdLine;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 制造管理-点检表明细列值
 * @ClassName: SpotcheckformdLineService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:17
 */
public class SpotcheckformdLineService extends BaseService<SpotcheckformdLine> {
	private final SpotcheckformdLine dao=new SpotcheckformdLine().dao();
	@Override
	protected SpotcheckformdLine dao() {
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
	public Page<SpotcheckformdLine> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param spotcheckformdLine
	 * @return
	 */
	public Ret save(SpotcheckformdLine spotcheckformdLine) {
		if(spotcheckformdLine==null || isOk(spotcheckformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotcheckformdLine.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotcheckformdLine.getIAutoId(), JBoltUserKit.getUserId(), spotcheckformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotcheckformdLine
	 * @return
	 */
	public Ret update(SpotcheckformdLine spotcheckformdLine) {
		if(spotcheckformdLine==null || notOk(spotcheckformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotcheckformdLine dbSpotcheckformdLine=findById(spotcheckformdLine.getIAutoId());
		if(dbSpotcheckformdLine==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotcheckformdLine.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotcheckformdLine.getIAutoId(), JBoltUserKit.getUserId(), spotcheckformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotcheckformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotcheckformdLine spotcheckformdLine, Kv kv) {
		//addDeleteSystemLog(spotcheckformdLine.getIAutoId(), JBoltUserKit.getUserId(),spotcheckformdLine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotcheckformdLine model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotcheckformdLine spotcheckformdLine, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}
	public SpotcheckformdLine findBySpotCheckFormDId(Long formDIAutoId) {
		return  findFirst("select * from PL_SpotCheckFormD_Line where iSpotCheckFormDid=?",formDIAutoId);
	}

}