package cn.rjtech.admin.bommtrl;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.BommTrl;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料建模-BOM扩展
 * @ClassName: BommTrlService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-07-04 16:16
 */
public class BommTrlService extends BaseService<BommTrl> {
	private final BommTrl dao=new BommTrl().dao();
	@Override
	protected BommTrl dao() {
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
	public Page<BommTrl> getAdminDatas(int pageNumber, int pageSize, String keywords) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //关键词模糊查询
        sql.like("cDocName",keywords);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bommTrl
	 * @return
	 */
	public Ret save(BommTrl bommTrl) {
		if(bommTrl==null || isOk(bommTrl.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=bommTrl.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bommTrl.getIAutoId(), JBoltUserKit.getUserId(), bommTrl.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bommTrl
	 * @return
	 */
	public Ret update(BommTrl bommTrl) {
		if(bommTrl==null || notOk(bommTrl.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BommTrl dbBommTrl=findById(bommTrl.getIAutoId());
		if(dbBommTrl==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=bommTrl.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bommTrl.getIAutoId(), JBoltUserKit.getUserId(), bommTrl.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bommTrl 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BommTrl bommTrl, Kv kv) {
		//addDeleteSystemLog(bommTrl.getIAutoId(), JBoltUserKit.getUserId(),bommTrl.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bommTrl model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BommTrl bommTrl, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

}
