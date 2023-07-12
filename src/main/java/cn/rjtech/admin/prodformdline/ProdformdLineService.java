package cn.rjtech.admin.prodformdline;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.ProdformdLine;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 制造管理-生产表单明细列值
 * @ClassName: ProdformdLineService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-28 18:59
 */
public class ProdformdLineService extends BaseService<ProdformdLine> {
	private final ProdformdLine dao=new ProdformdLine().dao();
	@Override
	protected ProdformdLine dao() {
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
	public Page<ProdformdLine> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param prodformdLine
	 * @return
	 */
	public Ret save(ProdformdLine prodformdLine) {
		if(prodformdLine==null || isOk(prodformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=prodformdLine.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(prodformdLine.getIAutoId(), JBoltUserKit.getUserId(), prodformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param prodformdLine
	 * @return
	 */
	public Ret update(ProdformdLine prodformdLine) {
		if(prodformdLine==null || notOk(prodformdLine.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		ProdformdLine dbProdformdLine=findById(prodformdLine.getIAutoId());
		if(dbProdformdLine==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=prodformdLine.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(prodformdLine.getIAutoId(), JBoltUserKit.getUserId(), prodformdLine.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param prodformdLine 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(ProdformdLine prodformdLine, Kv kv) {
		//addDeleteSystemLog(prodformdLine.getIAutoId(), JBoltUserKit.getUserId(),prodformdLine.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param prodformdLine model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(ProdformdLine prodformdLine, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public List<ProdformdLine> findByProdFormDId(Long formDIAutoId) {
		return  find("select * from PL_ProdFormD_Line where iProdFormDid=?",formDIAutoId);
	}
}