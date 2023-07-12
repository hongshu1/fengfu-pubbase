package cn.rjtech.admin.prodformd;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ProdFormD;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 制造管理-生产表单行配置
 * @ClassName: ProdFormDService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 18:59
 */
public class ProdFormDService extends BaseService<ProdFormD> {
	private final ProdFormD dao=new ProdFormD().dao();
	@Override
	protected ProdFormD dao() {
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
	public Page<ProdFormD> getAdminDatas(int pageNumber, int pageSize, Integer iType) {
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
	 * @param prodFormD
	 * @return
	 */
	public Ret save(ProdFormD prodFormD) {
		if(prodFormD==null || isOk(prodFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodFormD.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(prodFormD.getIAutoId(), JBoltUserKit.getUserId(), prodFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param prodFormD
	 * @return
	 */
	public Ret update(ProdFormD prodFormD) {
		if(prodFormD==null || notOk(prodFormD.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdFormD dbProdFormD=findById(prodFormD.getIAutoId());
		if(dbProdFormD==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodFormD.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(prodFormD.getIAutoId(), JBoltUserKit.getUserId(), prodFormD.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param prodFormD 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ProdFormD prodFormD, Kv kv) {
		//addDeleteSystemLog(prodFormD.getIAutoId(), JBoltUserKit.getUserId(),prodFormD.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param prodFormD model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ProdFormD prodFormD, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<ProdFormD> findByPid(Long pid) {
		return  find("select * from PL_ProdFormD where iProdFormMid=?",pid);
	}
}