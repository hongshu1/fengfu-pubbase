package cn.rjtech.admin.bomdata;

import cn.jbolt.core.base.JBoltMsg;
import cn.jbolt.core.db.sql.Sql;
import cn.jbolt.core.service.base.BaseService;
import cn.jbolt.extend.systemlog.ProjectSystemLogTargetType;
import cn.rjtech.model.momdata.BomData;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
/**
 * 物料建模-BOM数据
 * @ClassName: BomDataService
 * @author: 佛山市瑞杰科技有限公司
 * @date: 2023-06-15 21:37
 */
public class BomDataService extends BaseService<BomData> {
	private final BomData dao=new BomData().dao();
	@Override
	protected BomData dao() {
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
	public Page<BomData> getAdminDatas(int pageNumber, int pageSize) {
	    //创建sql对象
	    Sql sql = selectSql().page(pageNumber,pageSize);
        //排序
        sql.desc("iAutoId");
		return paginate(sql);
	}

	/**
	 * 保存
	 * @param bomData
	 * @return
	 */
	public Ret save(BomData bomData) {
		if(bomData==null || isOk(bomData.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		boolean success=bomData.save();
		if(success) {
			//添加日志
			//addSaveSystemLog(bomData.getIAutoId(), JBoltUserKit.getUserId(), bomData.getName());
		}
		return ret(success);
	}

	/**
	 * 更新
	 * @param bomData
	 * @return
	 */
	public Ret update(BomData bomData) {
		if(bomData==null || notOk(bomData.getIAutoId())) {
			return fail(JBoltMsg.PARAM_ERROR);
		}
		//更新时需要判断数据存在
		BomData dbBomData=findById(bomData.getIAutoId());
		if(dbBomData==null) {return fail(JBoltMsg.DATA_NOT_EXIST);}
		boolean success=bomData.update();
		if(success) {
			//添加日志
			//addUpdateSystemLog(bomData.getIAutoId(), JBoltUserKit.getUserId(), bomData.getName());
		}
		return ret(success);
	}

	/**
	 * 删除数据后执行的回调
	 * @param bomData 要删除的model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	protected String afterDelete(BomData bomData, Kv kv) {
		//addDeleteSystemLog(bomData.getIAutoId(), JBoltUserKit.getUserId(),bomData.getName());
		return null;
	}

	/**
	 * 检测是否可以删除
	 * @param bomData model
	 * @param kv 携带额外参数一般用不上
	 * @return
	 */
	@Override
	public String checkInUse(BomData bomData, Kv kv) {
		//这里用来覆盖 检测是否被其它表引用
		return null;
	}

	public BomData create(Long bomId, String format){
		BomData bomData = new BomData();
		bomData.setIBomMid(bomId);
		bomData.setCData(format);
		return bomData;
	}
	
	public BomData getBomData(Long bomId){
		Sql sql = selectSql().eq(BomData.IBOMMID, bomId);
		return findFirst(sql);
	}
}
