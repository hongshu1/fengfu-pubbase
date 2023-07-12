package cn.rjtech.admin.spotcheckformd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.SpotCheckFormD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 制造管理-点检表单行配置
 * @ClassName: SpotCheckFormDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-29 09:17
 */
public class SpotCheckFormDService extends BaseService<SpotCheckFormD> {
	private final SpotCheckFormD dao=new SpotCheckFormD().dao();
	@Override
	protected SpotCheckFormD dao() {
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
     * @param iType 参数录入方式：1. CPK数值 2. 文本框 3. 选择（√，/，×，△，◎） 4. 单选 5. 复选 6. 下拉列表 7. 日期 8. 时间
	 * @return
	 */
	public Page<SpotCheckFormD> getAdminDatas(int pageNumber, int pageSize, Integer iType) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
	    //sql条件处理
        sql.eq("iType",iType);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param spotCheckFormD
	 * @return
	 */
	public Ret save(SpotCheckFormD spotCheckFormD) {
		if(spotCheckFormD==null || isOk(spotCheckFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=spotCheckFormD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(spotCheckFormD.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param spotCheckFormD
	 * @return
	 */
	public Ret update(SpotCheckFormD spotCheckFormD) {
		if(spotCheckFormD==null || notOk(spotCheckFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		SpotCheckFormD dbSpotCheckFormD=findById(spotCheckFormD.getIAutoId());
		if(dbSpotCheckFormD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=spotCheckFormD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(spotCheckFormD.getIAutoId(), JBoltUserKit.getUserId(), spotCheckFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param spotCheckFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(SpotCheckFormD spotCheckFormD, Kv kv) {
		//addDeleteSystemLog(spotCheckFormD.getIAutoId(), JBoltUserKit.getUserId(),spotCheckFormD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param spotCheckFormD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(SpotCheckFormD spotCheckFormD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<SpotCheckFormD> findByPid(Long pid) {
		return  find("select * from PL_SpotCheckFormD where iSpotCheckFormMid=?",pid);
	}
}